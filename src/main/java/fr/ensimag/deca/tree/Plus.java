package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructionsARM.add;

/**
 * @author gl27
 * @date 01/01/2017
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }
    
    // ====== AJOUT =======
    @Override
    protected void codeExpArith(DecacCompiler compiler, GPRegister r, DVal val) {
    	compiler.addInstruction(new ADD(val, r));
    	if (this.getLeftOperand().getType().isFloat() && this.getRightOperand().getType().isFloat()){
    		compiler.addInstruction(new BOV(new Label("overflow_error")));
    	}
    }
    
    //Extension
    @Override
    protected void codeExpArithARM(DecacCompiler compiler, Registres regs, GPRegister r, Operand val) {
    	compiler.addInstruction(new add(r, r, val));
    }
}
