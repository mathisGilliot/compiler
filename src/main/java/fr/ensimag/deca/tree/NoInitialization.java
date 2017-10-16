package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * Absence of initialization (e.g. "int x;" as opposed to "int x =
 * 42;").
 *
 * @author gl27
 * @date 01/01/2017
 */
public class NoInitialization extends AbstractInitialization {
	
	private static final Logger LOG = Logger.getLogger(NoInitialization.class);
	
	// AJOUT
	protected void codeInitialisationVar(DecacCompiler compiler, Registres regs, DAddr addr, Type t){
		// Pas d'initialisation pour les variables globales
		// Pour les classes : on met null
		if (t.isClass()){
			compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
			compiler.addInstruction(new STORE(Register.R0, addr));
		}
    	
	}
	
	/**
	 * @param compiler
	 * @param regs
	 * @param addr
	 * @param t
	 */
	protected void codeInitialisationField(DecacCompiler compiler, Registres regs, DAddr addr, Type t){
		if (t.isBoolean()){
			compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
		}
		else if (t.isInt()){
			compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
		}
		else if (t.isFloat()){
			compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));
		}
		else {
			throw new UnsupportedOperationException("not yet implemented");
		}
		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
		compiler.addInstruction(new STORE(Register.R0, addr));
	}

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	LOG.debug("verify initialization: start");
    	LOG.debug("verify initialization: end");
//        throw new UnsupportedOperationException("not yet implemented");
    }


    /**
     * Node contains no real information, nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    //Extension
	@Override
	public void codeInitialisationVarARM(DecacCompiler compiler, Registres regs, AbstractIdentifier varName, Type t) {
		// Pas d'initialisation		
	}

}
