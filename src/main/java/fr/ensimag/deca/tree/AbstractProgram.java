package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ContextualError;

/**
 * Entry point for contextual verifications and code generation from outside the package.
 * 
 * @author gl27
 * @date 01/01/2017
 *
 */
public abstract class AbstractProgram extends Tree {
	
	/**
	 * 
	 * @param compiler
	 * @throws ContextualError
	 */
    public abstract void verifyProgram(DecacCompiler compiler) throws ContextualError;
    
    /**
     * 
     * @param compiler
     * @param regs
     * @param p
     */
    public abstract void codeGenProgram(DecacCompiler compiler, Registres regs, Pile p) ;

    
    /**
     * 
     * @param compiler
     * @param regs
     * @param p
     */
	public abstract void codeGenProgramARM(DecacCompiler compiler, Registres regs, Pile p) ;

}
