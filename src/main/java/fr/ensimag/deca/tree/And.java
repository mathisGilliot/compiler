package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.Label;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public class And extends AbstractOpBool {

	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }

    //AJOUT
    /**
     * @param compiler
     * @param regs
     * @param b
     * @param label
     */
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	// ⟨Code(C1 && C2, vrai, E)⟩ ≡ ⟨Code(C1, faux, E_Fin.n)⟩
        //								⟨Code(C2, vrai, E)⟩
        //								E_Fin.n :
    	if (b) {
    		String eFin = "E_Fin";
    		compiler.getCompteurEtiquette().addCompteur();
        	eFin = eFin + compiler.getCompteurEtiquette().getCompteur();
        	Label labEFin = new Label(eFin);
        	this.getLeftOperand().codeCond(compiler, regs, !b, labEFin);
	    	this.getRightOperand().codeCond(compiler, regs, b, label);
	    	compiler.addLabel(labEFin);
    	}
    	// ⟨Code(C1 && C2, faux, E)⟩ ≡ ⟨Code(C1, faux, E)⟩
        //								⟨Code(C2, faux, E)⟩
    	else {
    		this.getLeftOperand().codeCond(compiler, regs, b, label);
	    	this.getRightOperand().codeCond(compiler, regs, b, label);
    	}
    }
    
    //Extension
    
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	// ⟨Code(C1 && C2, vrai, E)⟩ ≡ ⟨Code(C1, faux, E_Fin.n)⟩
        //								⟨Code(C2, vrai, E)⟩
        //								E_Fin.n :
    	if (b) {
    		String eFin = "E_Fin";
    		compiler.getCompteurEtiquette().addCompteur();
        	eFin = eFin + compiler.getCompteurEtiquette().getCompteur();
        	Label labEFin = new Label(eFin);
        	this.getLeftOperand().codeCondARM(compiler, regs, !b, labEFin);
	    	this.getRightOperand().codeCondARM(compiler, regs, b, label);
	    	compiler.addLabel(labEFin);
    	}
    	// ⟨Code(C1 && C2, faux, E)⟩ ≡ ⟨Code(C1, faux, E)⟩
        //								⟨Code(C2, faux, E)⟩
    	else {
    		this.getLeftOperand().codeCondARM(compiler, regs, b, label);
	    	this.getRightOperand().codeCondARM(compiler, regs, b, label);
    	}
    }

}
