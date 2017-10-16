package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
//import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;

import java.io.PrintStream;


public abstract class AbstractDeclMethod extends Tree {


	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Pass 1 of [SyntaxeContextuelle]. Verify that the method declaration is OK
     * without looking at its content.
	 * @param compiler
	 * @param localEnv
	 * @param currentClass
	 * @param index
	 * @throws ContextualError
	 */
	protected abstract void verifyDeclMethod(DecacCompiler compiler,
             ClassDefinition currentClass, int index) throws ContextualError;


	/**
	 * 
	 * @param compiler
	 * @param localEnv
	 * @param currentClass
	 * @throws ContextualError
	 */
	protected abstract void verifyDeclMethodBody(DecacCompiler compiler, 
			EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError;

	// AJOUT
	
	/**
	 * 
	 * @param tabEtiquettes
	 * @param nomClasse
	 */
	protected abstract void setLabelMethodDefinition(Label[] tabEtiquettes, String nomClasse);
	
	/**
	 * 
	 * @param compiler
	 * @param regs
	 * @param p
	 * @param className
	 */
	protected abstract void codeGenMethod(DecacCompiler compiler, Registres regs, Pile p, AbstractIdentifier className);

	
}
