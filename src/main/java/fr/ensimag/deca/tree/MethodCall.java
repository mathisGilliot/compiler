package fr.ensimag.deca.tree;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.SUBSP;

public class MethodCall extends AbstractExpr {
	
	private static final Logger LOG = Logger.getLogger(DeclParam.class);

	final private AbstractExpr expr;
	final private AbstractIdentifier ident;
	final private ListExpr args;
	
	/**
	 * 
	 * @param expr
	 * @param ident
	 * @param args
	 */
	public MethodCall(AbstractExpr expr, AbstractIdentifier ident, ListExpr args) {
		Validate.notNull(expr);
		Validate.notNull(ident);
		Validate.notNull(args);
		this.expr = expr;
		this.ident = ident;
		this.args = args;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		LOG.debug("verify method_call : start");
		LOG.debug(expr.getLocation());
		LOG.debug("verify method_call : startbis");
		
		if (currentClass == null) {
			if (this.expr.getLocation() == null) {
				throw new ContextualError("Une méthode doit être appliquée sur un objet (règle 3.43)", this.getLocation());
			}
		}

		Type typeClass = this.expr.verifyExpr(compiler, localEnv, currentClass);
		LOG.debug("après verifyExpr");
		
		if (!typeClass.isClass()) {
		    throw new ContextualError(typeClass.getName() + " n'est pas une classe (règle 3.71)", this.getLocation());
		}
		if (!compiler.getEnvType().getEnvironnement().containsKey(typeClass.getName())) {
		    throw new ContextualError(typeClass.getName() + " n'est pas un type (règles 3.71)", this.getLocation());
		}
		EnvironmentExp envExp2 = ((ClassDefinition) compiler.getEnvType().getEnvironnement().get(typeClass.getName())).getMembers();
		this.ident.verifyMethodIdent(compiler, envExp2, currentClass);
		Definition def = this.ident.getDefinition();
		if (!def.isMethod()) {
			throw new ContextualError(ident.getName() + " n'est pas une méthode (règle 3.71)", this.getLocation());
		}
		MethodDefinition defMethod = this.ident.getMethodDefinition();
		this.setType(defMethod.getType());
		Iterator<AbstractExpr> it = this.args.iterator();
		int compteur = 0;
		while (it.hasNext() || compteur < defMethod.getSignature().size()) {
			if (!it.hasNext() || !(compteur < defMethod.getSignature().size())) {
				throw new ContextualError("Le nombre de paramètres ne correspond pas à la signature de la méthode " + this.ident.getName() + " (règle 3.74)", this.getLocation());
			}
			AbstractExpr expr = it.next();
			Type typeSig = defMethod.getSignature().paramNumber(compteur);
			LOG.debug(typeSig.getName());
			expr.verifyRValue(compiler, localEnv, currentClass, typeSig);
			compteur ++;
		}
		LOG.debug("verify method_call : end");
		return defMethod.getType();
	}

	@Override
	public void decompile(IndentPrintStream s) {
		expr.decompile(s);
		expr.decompilePoint(s);
		ident.decompile(s);
		s.print("(");
		args.decompile(s);
		s.print(")");
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		expr.prettyPrint(s, prefix, false);
        ident.prettyPrint(s, prefix, false);
        args.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		expr.iterChildren(f);
        ident.iterChildren(f);
        args.iterChildren(f);
	}
    
    // Génère le code de MethodCall
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister ret){
    	// ADDSP
    	compiler.addInstruction(new ADDSP(1+args.size()));
    	
    	// On empile l'objet
    	GPRegister r = regs.reg_dispo();
    	compiler.addInstruction(new LOAD(expr.getAddr(compiler, r), r));
    	compiler.addInstruction(new STORE(r, new RegisterOffset(0, Register.SP)));
    	
    	// On empile les paramètres de gauche à droite
    	// On met les arguments dans les paramètres
    	regs.paramsCalledMethod(3 + args.size()); // Deux empilements par BSR + un pour l'adresse de l'objet
    	GPRegister r2 = regs.reg_dispo();
    	args.empileArgs(compiler, regs, r2);
    	regs.liberer(r2);
    	
    	// Appel de méthode
    	compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), r));
    	compiler.addInstruction(new CMP(new NullOperand(), r));
    	compiler.addInstruction(new BEQ(new Label("deferencement_null")));
    	
    	// On appelle la méthode
    	compiler.addInstruction(new LOAD(new RegisterOffset(0, r), r));
    	DAddr addr = new RegisterOffset(ident.getMethodDefinition().getIndex(), r);
    	compiler.addInstruction(new BSR(addr));
    	
    	// SUBSP
    	regs.liberer(r);
    	compiler.addInstruction(new SUBSP(1+args.size()));
    	
    	compiler.addInstruction(new LOAD(Register.R0, ret));
    }
	
		
	@Override
	protected void codeGenInstARM(DecacCompiler compiler, Registres regs) {	
	}
	
	@Override
	protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label e){
		GPRegister r = regs.reg_dispo();
    	this.codeExp(compiler, regs, r);
    	compiler.addInstruction(new LOAD(this.getDVal(compiler, regs), Register.R0));
    	compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
    	// ⟨Code(idf , vrai, E)⟩ ≡ LOAD @idf, R0
    	//							CMP #0, R0
    	//							BNE E
    	if (b){
    		compiler.addInstruction(new BNE(e));
    	}
    	else {
    		// ⟨Code(idf , faux, E)⟩ ≡ LOAD @idf, R0
    		// 							CMP #0, R0
    		// 							BEQ E
    		compiler.addInstruction(new BEQ(e));
    	}
    	//codeCond(compiler, regs, b, e);
    	regs.liberer(r);
    }
	
	
}
