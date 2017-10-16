package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.B;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {
	
	private static final Logger LOG = Logger.getLogger(AbstractOpBool.class);


    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    
    // Pour une expression booléenne
    // on met le resultat de la condition dans r
    @Override
    protected void codeBinExp(DecacCompiler compiler, Registres regs, GPRegister r, DVal val) {
    	compiler.getCompteurEtiquette().addCompteur();
    	Label labelFaux = new Label("faux" + compiler.getCompteurEtiquette().getCompteur());
    	Label labelRetour = new Label("retour" + compiler.getCompteurEtiquette().getCompteur());
    	// On branche pour affecter r
    	codeCond(compiler, regs, false, labelFaux);
    	compiler.addInstruction(new LOAD(new ImmediateInteger(1), r));
		compiler.addInstruction(new BRA(labelRetour));
		compiler.addLabel(labelFaux);
		compiler.addInstruction(new LOAD(new ImmediateInteger(0), r));
		compiler.addLabel(labelRetour);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
    		ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (type1.isBoolean() && type2.isBoolean()) {
    		this.setType(type1);
    		LOG.debug("verify expr: end");
    		return type1;
    	} else {
    		throw new ContextualError("Opération logique impossible entre des objets de type " + type1.getName() + " et " + type2.getName() + " (règle 3.33)", this.getLocation());
    	}
    }
    
    //Extension
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.getCompteurEtiquette().addCompteur();
    	Label labelFaux = new Label("faux" + compiler.getCompteurEtiquette().getCompteur());
    	Label labelRetour = new Label("retour" + compiler.getCompteurEtiquette().getCompteur());
    	// On branche pour affecter r
    	codeCondARM(compiler, regs, false, labelFaux);
    	compiler.addInstruction(new MOV(r, new ImmediateInteger(1)));
		compiler.addInstruction(new B(labelRetour));
		compiler.addLabel(labelFaux);
		compiler.addInstruction(new MOV(r, new ImmediateInteger(0)));
		compiler.addLabel(labelRetour);
    }
}

