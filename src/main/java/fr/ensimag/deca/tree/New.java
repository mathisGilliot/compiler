package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.BSR;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.NEW;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;
import fr.ensimag.ima.pseudocode.instructions.STORE;

public class New extends AbstractExpr {
	
	private static final Logger LOG = Logger.getLogger(DeclParam.class);

	private final AbstractIdentifier ident;
	
	/**
	 * 
	 * @param ident
	 */
	public New(AbstractIdentifier ident) {
        Validate.notNull(ident);
		this.ident = ident;
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
		LOG.debug("verify new : start");
		Type type = this.ident.verifyType(compiler);
		if (!type.isClass()) {
			throw new ContextualError(this.ident.getName() + " n'est pas un type correspondant à une classe (règle 3.42)", this.getLocation());
		}
		this.setType(type);
		LOG.debug("verify new : end");
		return type;
	}

	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub	:check this	
		s.print("new ");
		ident.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		ident.prettyPrint(s, prefix, true);		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		ident.iterChildren(f);
	}
	
	// AJOUT
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
    	// Alloue l'objet dans le tas
    	ClassDefinition def = (ClassDefinition)compiler.getEnvType().get(ident.getName());
    	//ClassDefinition def = ident.getClassDefinition();
    	// nombre total des champs  + l’adresse de la table des méthodes
    	compiler.addInstruction(new NEW(def.getNumberOfFields() + 1, r));
    	compiler.addInstruction(new BOV(new Label("tas_plein")));
    	
    	// premier mot = l’adresse de la table des méthodes
    	compiler.addInstruction(new LEA(def.getOperand(), Register.R0));
    	compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, r)));
    	
    	// Initialise les champs de l'objet avec un sous-programme
    	compiler.addInstruction(new PUSH(r));
    	compiler.addInstruction(new BSR(new Label("init." + ident.getName().getName())));
    	compiler.addInstruction(new POP(r));
    	
    }

}
