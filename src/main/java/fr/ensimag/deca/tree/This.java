package fr.ensimag.deca.tree;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import org.apache.log4j.Logger;


public class This extends AbstractExpr{
	
	private static final Logger LOG = Logger.getLogger(Selection.class);
	
	public This(boolean b){
		this.b = b;
	}
			
	private boolean b;
	
//	@Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify this : start");
    	if (currentClass == null) {
    		throw new ContextualError("Utilisation de this impossible dans un main (règle 3.43)", this.getLocation());
    	}
    	this.setType(currentClass.getType());
    	LOG.debug("verify this : end");
    	return currentClass.getType();
    }
    
    public boolean getBool() {
    	return this.b;
    }


    @Override
    String prettyPrintNode() {
        return "This ";
    }
    
    @Override
    public void decompilePoint(IndentPrintStream s){
    	if (!getBool()){
    		s.print(".");
    	}
    } 
    
    @Override
    public void decompile(IndentPrintStream s) {
    	if (!getBool()){
    		s.print("this");
    	}
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    

}
