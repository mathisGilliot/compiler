package fr.ensimag.deca.tree;


import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public class Cast extends AbstractBinaryExpr {

private static final Logger LOG = Logger.getLogger(Cast.class);
	
    @Override
    public AbstractIdentifier getLeftOperand() {
        return (AbstractIdentifier)super.getLeftOperand();
    }

    public Cast(AbstractIdentifier leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

	@Override
	protected String getOperatorName() {
		return "cast";
	}

	@Override
	public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
			throws ContextualError {
    	LOG.debug("verify expr: start");
		Type type = this.getLeftOperand().verifyType(compiler);
		Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
		if (type2.isVoid() || !(compiler.getEnvType().assignCompatible(type, type2) || compiler.getEnvType().assignCompatible(type2, type))) {
			throw new ContextualError("Conversion d'une valeur de type " + type2.getName() + " en une valeur de type " + type.getName() + " impossible (règle 3.39)", this.getLocation());
		}
    	this.setType(type);
    	LOG.debug("verify expr: end");
		return type;
	}
	
	

}
