package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;


import java.io.PrintStream;


public abstract class AbstractDeclField extends Tree {


	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Pass 2 of [SyntaxeContextuelle]. Verify that the class members (fields and
     * methods) are OK, without looking at method body and field initialization.
	 * @param compiler
	 * @param localEnv
	 * @param currentClass
	 * @param index
	 * @throws ContextualError
	 */
	protected abstract void verifyDeclField(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, int index)
            throws ContextualError;
	
	/**
	 * Pass 3 of [SyntaxeContextuelle]. Verify that instructions and expressions
     * contained in the class are OK.
	 * @param compiler
	 * @param currentClass
	 * @param index
	 * @throws ContextualError
	 */
	protected abstract void verifyDeclFieldBody(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
	
	protected abstract void init0(DecacCompiler compiler);
	
	protected abstract void codeGenInitField(DecacCompiler compiler, Registres regs);
	
}

