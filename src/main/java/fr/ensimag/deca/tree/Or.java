package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }

    //AJOUT
    // ⟨Code(C1 || C2, b, E)⟩ ≡ ⟨Code(!(!C1 && !C2)), b, E)⟩
    @Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	AbstractExpr expr = new Not(new And(new Not(this.getLeftOperand()), new Not(this.getRightOperand())));
    	expr.codeCond(compiler, regs, b, label);
    }
    
    //Extension
    @Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	AbstractExpr expr = new Not(new And(new Not(this.getLeftOperand()), new Not(this.getRightOperand())));
    	expr.codeCondARM(compiler, regs, b, label);
    }

}