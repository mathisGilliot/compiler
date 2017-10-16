package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.log4j.Logger;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractOpExactCmp extends AbstractOpCmp {
	
	private static final Logger LOG = Logger.getLogger(AbstractOpExactCmp.class);

    public AbstractOpExactCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
    		ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type type2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
    	try {
        	LOG.debug("verify expr: end");
    		return super.verifyExpr(compiler, localEnv, currentClass);
    	}
    	catch(Exception ContextualError) {
    		if (type1.isBoolean() && type2.isBoolean()) {
    			this.setType(type1);
    	    	LOG.debug("verify expr: end");
    			return type1;
    		} else if (type1.isClassOrNull() && type2.isClassOrNull()) {
    			SymbolTable symbTab = compiler.getTable();
    	    	SymbolTable.Symbol symb = symbTab.create("boolean");
    	    	BooleanType booleanType = new BooleanType(symb);
    	    	this.setType(booleanType);
    	    	LOG.debug("verify expr: end");
    	    	return booleanType;
    		} else {
    			throw new ContextualError("Comparaison entre des objets de type " + type1.getName() + " et " + type2.getName() + " impossible (règle 3.33)", this.getLocation());
    		}
    	}
    }

}
