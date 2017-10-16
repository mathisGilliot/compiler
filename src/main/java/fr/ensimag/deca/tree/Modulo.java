package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.*;
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
public class Modulo extends AbstractOpArith {
	
	private static final Logger LOG = Logger.getLogger(Assign.class);

	/**
	 * 
	 * @param leftOperand
	 * @param rightOperand
	 */
    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (type1.isInt() && type2.isInt()) {
    		this.setType(type1);
    		LOG.debug("verify expr: end");
    		return type1;
    	} else {
    		throw new ContextualError("Opération mod impossible avec des objets de type float (règle 3.33)", this.getLocation());
    	}
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }
    
    /**
     * Génère l'instruction correspondant à r = r modulo val
     */
	@Override
    protected void codeExpArith(DecacCompiler compiler, GPRegister r, DVal val) {
    	compiler.addInstruction(new REM(val, r));
    	compiler.addInstruction(new BOV(new Label("overflow_error")));
    }


}
