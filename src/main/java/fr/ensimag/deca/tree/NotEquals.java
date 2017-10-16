package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;


/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }


    @Override
    protected String getOperatorName() {
        return "!=";
    }
    
    // AJOUT
    @Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	// Évalue opGauche, opDroite puis génère CMP
    	super.codeCond(compiler, regs, b, e);
    	if(!b){
    		compiler.addInstruction(new BEQ(e));
    	}
    	else {
    		compiler.addInstruction(new BNE(e));
    	}
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.getCompteurEtiquette().addCompteur();;
    	Label labelFaux = new Label("faux" + compiler.getCompteurEtiquette().getCompteur());
    	Label labelRetour = new Label("retour" + compiler.getCompteurEtiquette().getCompteur());
    	codeCond(compiler, regs, false, labelFaux);
    	compiler.addInstruction(new LOAD(new ImmediateInteger(1), r));
		compiler.addInstruction(new BRA(labelRetour));
		compiler.addLabel(labelFaux);
		compiler.addInstruction(new LOAD(new ImmediateInteger(0), r));
		compiler.addLabel(labelRetour);
    }
    
    //Extension
    @Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	if(!b){
    		compiler.addInstruction(new BEQ(label));
    	}
    	else {
    		compiler.addInstruction(new BNE(label));
    	}
    }
    
    @Override
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.getCompteurEtiquette().addCompteur();;
    	Label labelFaux = new Label("faux" + compiler.getCompteurEtiquette().getCompteur());
    	Label labelRetour = new Label("retour" + compiler.getCompteurEtiquette().getCompteur());
    	codeCondARM(compiler, regs, false, labelFaux);
    	compiler.addInstruction(new LDR(r, new ImmediateInteger(1)));
		compiler.addInstruction(new BRA(labelRetour));
		compiler.addLabel(labelFaux);
		compiler.addInstruction(new LDR(r, new ImmediateInteger(0)));
		compiler.addLabel(labelRetour);
    }
}
