package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * Empty main Deca program
 *
 * @author gl27
 * @date 01/01/2017
 */
public class EmptyMain extends AbstractMain {
	
	private static final Logger LOG = Logger.getLogger(EmptyMain.class);
	
    @Override
    protected void verifyMain(DecacCompiler compiler) throws ContextualError {
    }

    @Override
    protected void codeGenMain(DecacCompiler compiler, Registres regs, Pile p) {
    	// On ne fait aucune instruction
    }

    /**
     * Contains no real information => nothing to check.
     */
    @Override
    protected void checkLocation() {
        // nothing
    }
    
    @Override
    public void decompile(IndentPrintStream s) {
        // no main program => nothing
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
  //Extension
    @Override
    protected void codeGenMainARM(DecacCompiler compiler, Registres regs, Pile p) {
    	// On ne fait aucune instruction
    			throw new UnsupportedOperationException( "not yet implemented");
    
    }
}
