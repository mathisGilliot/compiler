package fr.ensimag.deca.tree;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.DAddr;

public abstract class AbstractDeclParam extends Tree{
	
	/**
	 * 
	 * @param compiler
	 * @param localEnv
	 * @param currentClass
	 * @throws ContextualError
	 */
	protected abstract void verifyDeclParam(DecacCompiler compiler,
             ClassDefinition currentClass)
            throws ContextualError;

	/**
	 * 
	 * @param compiler
	 * @param localEnv
	 * @throws ContextualError
	 */
	protected abstract void verifyDeclParamBody(DecacCompiler compiler,
            EnvironmentExp localEnv)
           throws ContextualError;
	
	/**
	 * 
	 * @param compiler
	 * @param addr
	 */
	protected abstract void setOperandParam(DecacCompiler compiler, DAddr addr);
		
}
