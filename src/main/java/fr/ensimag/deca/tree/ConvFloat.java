package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import org.apache.log4j.Logger;
/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl27
 * @date 01/01/2017
 */
public class ConvFloat extends AbstractUnaryExpr {
	
	private static final Logger LOG = Logger.getLogger(ConvFloat.class);
	
	/**
	 * 
	 * @param operand
	 */
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type = this.getOperand().getType();
    	SymbolTable symbTab = compiler.getTable();
    	SymbolTable.Symbol symb = symbTab.create("float");
    	FloatType floatType = new FloatType(symb);
    	this.setType(floatType);
    	LOG.debug("verify expr: end");
    	return type;
    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }
    
    // AJOUT
    @Override
    protected void codeUnaryExp(DecacCompiler compiler, Registres regs, GPRegister r) {
    	compiler.addInstruction(new FLOAT(r, r));
    }
    
    //Extension
    @Override
    protected void codeUnaryExpARM(DecacCompiler compiler, Registres regs, GPRegister r) {
    	//on ne gère pas les float
    }

}
