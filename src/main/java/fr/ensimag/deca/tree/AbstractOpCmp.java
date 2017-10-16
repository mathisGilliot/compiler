package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.B;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;
import fr.ensimag.ima.pseudocode.instructionsARM.cmp;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.BooleanType;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

	private static final Logger LOG = Logger.getLogger(AbstractOpCmp.class);
	
    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
    		ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if ((type1.isInt() || type1.isFloat()) && (type2.isInt() || type2.isFloat())) {
    		if (type1.isFloat() && type2.isInt()) {
        		this.setRightOperand(new ConvFloat(this.getRightOperand()));
        		this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    		}
    		if (type1.isInt() && type2.isFloat()) {
        		this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
        		this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    		}
    		SymbolTable symbTab = compiler.getTable();
    		SymbolTable.Symbol symb = symbTab.create("boolean");
    		BooleanType booleanType = new BooleanType(symb);
    		this.setType(booleanType);
        	LOG.debug("verify expr: end");
    		return booleanType;
    	} else  {
    		throw new ContextualError("Comparaison entre des objets de type " + type1.getName() + " et " + type2.getName() +" impossible (règle 3.33)", this.getLocation());
    	}
    }
    

    // AJOUT
    @Override
    protected void codeCondCMP(DecacCompiler compiler, GPRegister r, DVal val) {
    	compiler.addInstruction(new CMP(val, r));
    }
    
    /** Pour Assign, Initialisation d'une expression booléenne
    * 	On met le resultat de la condition dans r
    */
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
    
    //Extension
    @Override
    protected void codeCondCMPARM(DecacCompiler compiler, GPRegister r, Operand val) {
    	compiler.addInstruction(new cmp(r, val));
    }
    
    @Override
    protected void codeBinExpARM(DecacCompiler compiler, Registres regs, GPRegister r, Operand val) {
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
