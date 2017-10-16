package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
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
public class Not extends AbstractUnaryExpr {
	
	private static final Logger LOG = Logger.getLogger(Not.class);

	/**
	 * 
	 * @param operand
	 */
    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (type.isBoolean()) {
    		this.setType(type);
        	LOG.debug("verify expr: end");
			return type;
		} else {
			throw new ContextualError("Négation sur un objet de type " + type.getName() + " impossible (règle 3.37)", this.getLocation());
		}
    }


    @Override
    protected String getOperatorName() {
        return "!";
    }
    
    // AJOUT
    
    // ⟨Code(!C, vrai, E)⟩ ≡ ⟨Code(C, faux, E)⟩
    // ⟨Code(!C, faux, E)⟩ ≡ ⟨Code(C, vrai, E)⟩
    @Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	this.getOperand().codeCond(compiler, regs, !b, e);
    }
    
    @Override
    protected void codeUnaryExp(DecacCompiler compiler, Registres regs, GPRegister r) {
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
    
    //Extension
    @Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	this.getOperand().codeCondARM(compiler, regs, !b, e);
    }
    
    @Override
    protected void codeUnaryExpARM(DecacCompiler compiler, Registres regs, GPRegister r) {
        	compiler.getCompteurEtiquette().addCompteur();
        	Label labelFaux = new Label("faux" + compiler.getCompteurEtiquette().getCompteur());
        	Label labelRetour = new Label("retour" + compiler.getCompteurEtiquette().getCompteur());
        	// On branche pour affecter r
        	codeCondARM(compiler, regs, false, labelFaux);
        	compiler.addInstruction(new LDR(r, new ImmediateInteger(1)));
    		compiler.addInstruction(new BRA(labelRetour));
    		compiler.addLabel(labelFaux);
    		compiler.addInstruction(new LDR(r, new ImmediateInteger(0)));
    		compiler.addLabel(labelRetour);
    }
    
    
}
