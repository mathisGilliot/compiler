package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
import fr.ensimag.ima.pseudocode.instructionsARM.STR;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl27
 * @date 01/01/2017
 */
public class Assign extends AbstractBinaryExpr {

	private static final Logger LOG = Logger.getLogger(Assign.class);
	
    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue)super.getLeftOperand();
    }

    /**
     * 
     * @param leftOperand
     * @param rightOperand
     */
    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	this.setRightOperand(this.getRightOperand().verifyRValue(compiler, localEnv, currentClass, type));
    	this.setType(type);
    	LOG.debug("verify expr: end");
    	return type;
    }


    @Override
    protected String getOperatorName() {
        return "=";
    }

    // ============== AJOUT ==============

    /**
     * Génère le code de Assign
     * Trouve l'adresse du leftOperand : addr
     * Alloue un registre r
     * Évalue l'opérande de droite dans r
     * STORE r, addr
     * Libère l'adresse si elle a utilisé un registre
     */
    @Override
    public void codeGenInst(DecacCompiler compiler, Registres regs){
    	compiler.addComment("Affectation");
    	
    	GPRegister r2 = null;
    	// Trouve l'adresse du leftOperand : addr
    	// Si c'est un champ -> on alloue un registre pour pointer le champ
    	if (this.getLeftOperand().getExpDefinition().isField()){
    		r2 = regs.reg_dispo();
    	}
    	// Trouve l'adresse du leftOperand : addr
    	DAddr addr = this.getLeftOperand().getAddr(compiler, r2);

    	// Évaluation de l'expression dans r
    	GPRegister r = regs.reg_dispo();
    	this.getRightOperand().codeExp(compiler, regs, r);
    	// STORE
    	compiler.addInstruction(new STORE(r, addr));
    	regs.liberer(r);
    	if(this.getLeftOperand().getExpDefinition().isField()){
    		regs.liberer(r2);
    	}
    	regs.liberer(r);
    }
    
    //Extension
    
    @Override
    public void codeGenInstARM(DecacCompiler compiler, Registres regs){
    	// Adresse de l'identifier
    	GPRegister r = regs.reg_dispo();
    	String name = ((AbstractIdentifier)this.getLeftOperand()).getName().getName();
    	compiler.addInstruction(new LDR(r, new Label("="+name, true)));
    	compiler.addInstruction(new LDR(r, new Label("["+r+"]", true)));
    	this.getRightOperand().codeExpARM(compiler, regs, r);
    	compiler.addInstruction(new STR(r, new Label(name, true)));
    	regs.liberer(r);
    }

}
