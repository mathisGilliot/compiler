package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public abstract class AbstractMethodBody extends Tree {
	
	/**
	 * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
	 * @param compiler
	 * @throws ContextualError
	 */
	protected abstract void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, 
			ClassDefinition currentClass, Type returnType) throws ContextualError;

	/**
	 * 
	 * @param compiler
	 * @param regs
	 * @param p
	 */
	protected abstract void codeGenMethodBody(DecacCompiler compiler, Registres regs, Pile p);
	
	/**
	 * 
	 * @return
	 */
	protected int nbVar(){
		throw new UnsupportedOperationException("not yet implemented");
	}
}


