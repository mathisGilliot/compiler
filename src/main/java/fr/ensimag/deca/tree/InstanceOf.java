package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import org.apache.log4j.Logger;

public class InstanceOf extends AbstractBinaryExpr {

	private static final Logger LOG = Logger.getLogger(InstanceOf.class);
	
    @Override
    public AbstractIdentifier getRightOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractIdentifier)super.getRightOperand();
    }

    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	Type type1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
    	Type type2 = this.getRightOperand().verifyType(compiler);
    	if (type1.isClassOrNull() && type2.isClass()) {
    		SymbolTable symbTab = compiler.getTable();
        	SymbolTable.Symbol symb = symbTab.create("boolean");
        	BooleanType booleanType = new BooleanType(symb);
        	this.setType(booleanType);
        	LOG.debug("verify expr: end");
        	return booleanType;
    		
    	} else {
    		throw new ContextualError("Opération instanceof impossible entre des objets de type " + type1.getName() + " et " + type2.getName() + " (règle 3.40)", this.getLocation());
    	}
//        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    protected String getOperatorName() {
        return "instanceof";
    }
    
//    // ============== AJOUT ==============
//    
//    // Génère le code de Assign
//    @Override
//    public void codeGenInst(DecacCompiler compiler){
//    	// Évaluation de l'expression dans r
//    	GPRegister r = Registres.reg_dispo();
//    	this.getRightOperand().codeExp(compiler, r);
//    	// Adresse de l'identifier
//    	DAddr addr = getLeftOperand().getVariableDefinition().getOperand();
//        // STORE
//        compiler.addInstruction(new STORE(r, addr));
//        Registres.liberer(r);
//     }

}
