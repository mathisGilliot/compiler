package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public class Equals extends AbstractOpExactCmp {

	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "==";
    }
    
    /**
     * Génère tout le code pour une condition '=='
     */
    
    @Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	// Évalue opGauche, opDroite puis génère CMP
    	super.codeCond(compiler, regs, b, e);
    	// Branchement
    	if (!b){
    		compiler.addInstruction(new BNE(e));
    	}
    	else {
    		compiler.addInstruction(new BEQ(e));
    	}
    	
    }
    
    //Extension
    @Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	// Évalue opGauche, opDroite puis génère CMP
    	super.codeCondARM(compiler, regs, b, e);
    	// Branchement
    	if (!b){
    		compiler.addInstruction(new BNE(e));
    	}
    	else {
    		compiler.addInstruction(new BEQ(e));
    	}
    	
    }
    
}
