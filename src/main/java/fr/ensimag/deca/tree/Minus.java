package fr.ensimag.deca.tree;



import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.instructionsARM.sub;

/**
 * @author gl27
 * @date 01/01/2017
 */
public class Minus extends AbstractOpArith {
	
	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    
// ====== AJOUT =======
    @Override
    protected void codeExpArith(DecacCompiler compiler, GPRegister r, DVal val) {
    	compiler.addInstruction(new SUB(val, r));
    	if (this.getLeftOperand().getType().isFloat() && this.getRightOperand().getType().isFloat()){
    		compiler.addInstruction(new BOV(new Label("overflow_error")));
    	}
    }
    
    //Extension
   @Override
    protected void codeExpArithARM(DecacCompiler compiler, Registres regs, GPRegister r, Operand val) {
    	compiler.addInstruction(new sub(r, r, val));
    }


    
}
