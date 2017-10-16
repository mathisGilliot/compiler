package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.OPP;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

/**
 * @author gl27
 * @date 01/01/2017
 */
public class UnaryMinus extends AbstractUnaryExpr {

	private static final Logger LOG = Logger.getLogger(UnaryMinus.class);

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type = getOperand().verifyExpr(compiler, localEnv, currentClass);
    	if (type.isInt() || type.isFloat()) {
        	LOG.debug("verify expr: end");
    		this.setType(type);
			return type;
		} else {
			throw new ContextualError("Moins unaire sur un objet de type " + type.getName() + " impossible (règle 3.37)", this.getLocation());
		}
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    
    @Override
    protected void codeUnaryExp(DecacCompiler compiler, Registres regs, GPRegister r) {
    	compiler.addInstruction(new OPP(r, r));
    }
    
    //Extension
    @Override
    protected void codeUnaryExpARM(DecacCompiler compiler, Registres regs, GPRegister r) {
    	//trouver l'opérateur pour opp arm
    	//compiler.addInstruction(new OPP(r, r));
    }

}
