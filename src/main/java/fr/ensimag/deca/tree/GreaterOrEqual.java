package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;

/**
 * Operator "x >= y"
 * 
 * @author gl27
 * @date 01/01/2017
 */
public class GreaterOrEqual extends AbstractOpIneq {

	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return ">=";
    }
    
    // AJOUT
    @Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	super.codeCond(compiler, regs, b, label);
    	// Si la condition est fausse
    	if (!b){
    		compiler.addInstruction(new BLT(label));
    	}
    	else {
    		compiler.addInstruction(new BGE(label));
    	}
    	
    }
    
    //Extension
    @Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	super.codeCondARM(compiler, regs, b, label);
    	// Si la condition est fausse
    	if (!b){
    		compiler.addInstruction(new BLT(label));
    	}
    	else {
    		compiler.addInstruction(new BGE(label));
    	}
    	
    }

}
