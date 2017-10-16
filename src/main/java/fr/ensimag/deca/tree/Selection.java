package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


public class Selection extends AbstractLValue {
	
	private static final Logger LOG = Logger.getLogger(Selection.class);

	final private AbstractExpr expr;
	final private AbstractIdentifier ident;
	
	/**
	 * 
	 * @param expr
	 * @param ident
	 */
	public Selection(AbstractExpr expr, AbstractIdentifier ident) {
        Validate.notNull(expr);
        Validate.notNull(ident);
		this.expr = expr;
		this.ident = ident;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		LOG.debug("verify selection : start");
		Type typeClass = this.expr.verifyExpr(compiler, localEnv, currentClass);
		if (!compiler.getEnvType().getEnvironnement().containsKey(typeClass.getName())) {
		    throw new ContextualError(typeClass.getName() + " n'est pas un type (règles 3.65 et 3.66)", this.getLocation());
		}
		if (!typeClass.isClass()) {
		    throw new ContextualError("Le type " + typeClass.getName() + " ne correspond pas à une classe (règles 3.65 et 3.66)", this.getLocation());
		}
		EnvironmentExp envExp2 = ((ClassDefinition) compiler.getEnvType().getEnvironnement().get(typeClass.getName())).getMembers();
		Type type = ident.verifyExpr(compiler, envExp2, currentClass);
		Definition def = this.ident.getDefinition();
		if (!def.isField()) {
			throw new ContextualError(ident.getName() + " n'est pas à un champ (règle 3.70)", this.getLocation());
		}
		FieldDefinition defField = this.ident.getFieldDefinition();
		if (defField.getVisibility() == Visibility.PROTECTED) {
		    if (currentClass == null || !(currentClass.getType().isClass() && ident.getFieldDefinition().getContainingClass().getType().isClass())) {
			throw new ContextualError("Accès au champ " + ident.getName() + " non-autorisé (règle 3.66)", this.getLocation());
		    }
		    if (!(compiler.getEnvType().subtype(typeClass, currentClass.getType())
			  && compiler.getEnvType().subtype(currentClass.getType(), ident.getFieldDefinition().getContainingClass().getType()))) {
			throw new ContextualError("Accès au champ " + ident.getName() + " non-autorisé (règle 3.66)", this.getLocation());
		    }
		}
		this.setType(type);
		LOG.debug("verify selection : end");
		return type;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub
		expr.decompile(s);
		if (!(ident.equals(null))){
			s.print(".");
			ident.decompile(s);
		}
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		expr.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		expr.iterChildren(f);
		ident.iterChildren(f);
	}

	@Override
	public ExpDefinition getExpDefinition() {
		return ident.getExpDefinition();
	}
	
	@Override
	public FieldDefinition getFieldDefinition() {
		return ident.getFieldDefinition();
	}
	
	
	/**
	 * renvoie l'adresse du champ sélectionné
	 */
	@Override
	protected DAddr getAddr(DecacCompiler compiler, GPRegister r){
		// LOAD addrObjet, r
		if (!(expr instanceof This)){
			compiler.addInstruction(new LOAD(expr.getAddr(compiler, r), r));
		}
		else {
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), r));
		}
		// Vérification objet non null
		compiler.addInstruction(new CMP(new NullOperand(), r));
		compiler.addInstruction(new BEQ(new Label("deferencement_null")));
		// On récupère l'index du champ dans le tas
		int index = this.ident.getFieldDefinition().getIndex();
		// Adresse du champ dans le tas
		return new RegisterOffset(index, r);
	}

	// TODO Code à factoriser entre getAddr et codeExp
	
	@Override
	protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
		// LOAD addrObjet, r
		if (!(expr instanceof This)){
			compiler.addInstruction(new LOAD(expr.getAddr(compiler, r), r));
		}
		else {
			compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), r));
		}
		// Vérification objet non null
    	compiler.addInstruction(new CMP(new NullOperand(), r));
    	compiler.addInstruction(new BEQ(new Label("deferencement_null")));
    	// On récupère l'index du champ dans le tas
		int index = this.ident.getFieldDefinition().getIndex();
		// Adresse du champ dans le tas
    	compiler.addInstruction(new LOAD(new RegisterOffset(index, r), r));
	}
	
	
}
