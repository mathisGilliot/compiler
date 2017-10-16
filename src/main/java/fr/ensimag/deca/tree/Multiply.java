package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.MUL;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;
import fr.ensimag.ima.pseudocode.instructionsARM.mul;

/**
 * @author gl27
 * @date 01/01/2017
 */

public class Multiply extends AbstractOpArith {
	
	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

    // ====== AJOUT =======

    @Override
    protected void codeExpArith(DecacCompiler compiler, GPRegister r, DVal val) {
    	compiler.addInstruction(new MUL(val, r));
    	if (this.getLeftOperand().getType().isFloat() && this.getRightOperand().getType().isFloat()){
    		compiler.addInstruction(new BOV(new Label("overflow_error")));
    	}
    }
    
  @Override
    protected void codeExpArithARM(DecacCompiler compiler, Registres regs, GPRegister r, Operand val) {
	  GPRegister r2 = regs.reg_dispo();
	  compiler.addInstruction(new MOV(r2, val));
	  compiler.addInstruction(new mul(r, r, r2));
	  r2.liberer(regs);
    }

}
