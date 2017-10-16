package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Unary expression.
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    public AbstractExpr getOperand() {
        return operand;
    }
    
    private AbstractExpr operand;
    
    
    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }


    protected abstract String getOperatorName();
  
    @Override
    public void decompile(IndentPrintStream s) {
    	s.print(getOperatorName());
    	getOperand().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

    /**
     * Verifie que le type est OK pour une operation unaire
     * @param type
     * @return
     */
    public Type typeUnaryOp(Type type) {
    	if (this instanceof UnaryMinus) {
    		if (type.isInt() || type.isFloat()) {
    			return type;
    		} else {
    			throw new AssertionError("Le type n'est pas compatible avec un moins unaire");
    		}
    	} else if (this instanceof Not) {
    		if (type.isBoolean()) {
    			return type;
    		} else {
    			throw new AssertionError("Le type n'est pas compatible avec un not");
    		}
    	} else {
    		throw new AssertionError("Cette opération unaire n'est pas reconnue");
    	}
    }
    
    // AJOUT
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r) {
    	this.getOperand().codeExp(compiler, regs, r);
    	codeUnaryExp(compiler, regs, r);
    }
    
    protected void codeUnaryExp(DecacCompiler compiler, Registres regs, GPRegister r) {
    	throw new UnsupportedOperationException("not yet implemented");
    }
    
    //Extension
    @Override
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r) {
    	this.getOperand().codeExpARM(compiler, regs, r);
    	codeUnaryExpARM(compiler, regs, r);
    }
    
    protected void codeUnaryExpARM(DecacCompiler compiler, Registres regs, GPRegister r) {
    	throw new UnsupportedOperationException("not yet implemented");
    }
    
}
