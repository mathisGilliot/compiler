package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Return extends AbstractInst {

	private final AbstractExpr expr;
	
	/**
	 * 
	 * @param expr
	 */
	public Return(AbstractExpr expr) {
        Validate.notNull(expr);
		this.expr = expr;
	}

	@Override
	protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass,
			Type returnType) throws ContextualError {
		if (returnType.isVoid()) {
			throw new ContextualError("Une méthode de type void ne peut pas renvoyer d'objet (règle 3.24)", this.getLocation());
		}
		this.expr.verifyRValue(compiler, localEnv, currentClass, returnType);
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler, Registres regs) {
		GPRegister r = regs.reg_dispo();
    	expr.codeExp(compiler, regs, r);
		compiler.addInstruction(new LOAD(r, Register.R0));
		regs.liberer(r);
	}

	@Override
	public void decompile(IndentPrintStream s) {
		s.print("return ");
		expr.decompile(s);
		s.print(";");
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		expr.prettyPrint(s, prefix, true);
		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		expr.iterChildren(f);
	}

	@Override
	protected void codeGenInstARM(DecacCompiler compiler, Registres regs) {
		
	}

}
