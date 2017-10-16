package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.QUO;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public class Divide extends AbstractOpArith {
	
	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }
    
 // ====== AJOUT =======
    
    @Override
    protected void codeExpArith(DecacCompiler compiler,  GPRegister r, DVal val) {
    	if (this.getLeftOperand().getType().isInt() && this.getRightOperand().getType().isInt()){
    		compiler.addInstruction(new QUO(val, r));
    	}
    	else {
        	compiler.addInstruction(new DIV(val, r));
    	}
    	compiler.addInstruction(new BOV(new Label("overflow_error")));
    }

}
