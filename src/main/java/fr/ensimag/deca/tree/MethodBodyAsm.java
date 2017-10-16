package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

public class MethodBodyAsm extends AbstractMethodBody{
	
	private StringLiteral multiLineString;
	
	/**
	 * 
	 * @param multiLineString
	 */
	public MethodBodyAsm(StringLiteral multiLineString) {
        Validate.notNull(multiLineString);
        this.multiLineString = multiLineString;
    }

	@Override
	public void decompile(IndentPrintStream s) {
		multiLineString.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		multiLineString.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		multiLineString.iterChildren(f);
		
	}
	
	@Override
	protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, 
			ClassDefinition currentClass, Type returnType) throws ContextualError {
	    this.multiLineString.verifyExpr(compiler, localEnv, currentClass);
	}
	
	protected void codeGenMethodBody(DecacCompiler compiler, Registres regs, Pile p){
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	

}
