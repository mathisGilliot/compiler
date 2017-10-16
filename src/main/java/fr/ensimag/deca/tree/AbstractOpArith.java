package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

	private static final Logger LOG = Logger.getLogger(AbstractOpArith.class);
	
	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	this.getLeftOperand().setType(type1);
    	Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	this.getRightOperand().setType(type2);
    	if (type1.isInt() && type2.isInt()) {
    		this.setType(type1);
    		LOG.debug("verify expr: end");
    		return type1;
    	} else if (type1.isFloat() && type2.isInt()) {
    		this.setRightOperand(new ConvFloat(this.getRightOperand()));
    		this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    		this.setType(type1);
    		LOG.debug("verify expr: end");
    		return type1;
    	} else if (type1.isInt() && type2.isFloat()) {
    		this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
    		this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    		this.setType(type2);
    		LOG.debug("verify expr: end");
    		return type2;
    	} else if (type1.isFloat() && type2.isFloat()) {
    		this.setType(type1);
    		LOG.debug("verify expr: end");
    		return type1;
    	} else {
    		throw new ContextualError("Opération arithmétique entre des objets de type " + type1.getName() + " et " + type2.getName() + " impossible (règle 3.37)", this.getLocation());
    	}
    }
    
    /**
     * Verification de la regle (3.37)
     * @param type1
     * @param type2
     * @param compiler
     * @param localEnv
     * @param currentClass
     * @return
     * @throws ContextualError
     */
    public Type typeArithOp(Type type1, Type type2, DecacCompiler compiler, EnvironmentExp localEnv,
    		ClassDefinition currentClass) throws ContextualError {
    	if (type1.isInt() && type2.isInt()) {
    		return type1;
    	} else if (type1.isFloat() && type2.isInt()) {
    		this.setRightOperand(new ConvFloat(this.getRightOperand()));
    		this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    		return type1;
    	} else if (type1.isInt() && type2.isFloat()) {
    		this.setLeftOperand(new ConvFloat(this.getLeftOperand()));
    		this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    		return type2;
    	} else if (type1.isFloat() && type2.isFloat()) {
    		return type1;
    	} else {
    		throw new ContextualError("les types sont incompatibles avec un opérateur arithmétique (Règle (3.37))", this.getLocation());
    	}
    }


    // ====== AJOUT =======
    
    /**
     * Appelle la génération de code de l'instruction
     * @param compiler
     * @param r
     * @param val
     */
    @Override
    protected void codeBinExp(DecacCompiler compiler, Registres regs, GPRegister r, DVal val) {
    	this.codeExpArith(compiler, r, val);
    }

    // Définit dans les filles
    /**
     * Génère le code de l'instruction
     * @param compiler
     * @param r
     * @param val
     */
    protected void codeExpArith(DecacCompiler compiler, GPRegister r, DVal val) {
    	throw new UnsupportedOperationException("not yet implemented");
    }

    
    //Extension
    @Override
    protected void codeBinExpARM(DecacCompiler compiler, Registres regs, GPRegister r, Operand val) {
    	codeExpArithARM(compiler, regs, r, val);
    }
	protected void codeExpArithARM(DecacCompiler compiler, Registres regs, GPRegister r, Operand val) {
		throw new UnsupportedOperationException("not yet implemented");
	}

}
