package fr.ensimag.deca.tree;



import fr.ensimag.deca.context.Type;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * 
 * @author gl27
 * @date 01/01/2017
 */
public class ListInst extends TreeList<AbstractInst> {

	private static final Logger LOG = Logger.getLogger(ListInst.class);

    /**
     * Implements non-terminal "list_inst" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param localEnv corresponds to "env_exp" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     * @param returnType
     *          corresponds to "return" attribute (void in the main bloc).
     */    
	public void verifyListInst(DecacCompiler compiler, EnvironmentExp localEnv,
			ClassDefinition currentClass, Type returnType)
					throws ContextualError {
		LOG.debug("verify list_inst: start");
		Iterator<AbstractInst> it = this.iterator();
		while (it.hasNext()) {
			AbstractInst expr = it.next();
			expr.verifyInst(compiler, localEnv, currentClass, returnType);
		}
		LOG.debug("verify list_inst: end");
	}

	/**
	 * 
	 * @param compiler
	 * @param regs
	 */
    public void codeGenListInst(DecacCompiler compiler, Registres regs) {
        for (AbstractInst i : getList()) {
            i.codeGenInst(compiler, regs);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractInst i : getList()) {
            i.decompileInst(s);
            s.println();
        }
    }

    //Extension
	public void codeGenListInstARM(DecacCompiler compiler, Registres regs) {
		for (AbstractInst i : getList()) {
            i.codeGenInstARM(compiler, regs);

        }
		
	}
}
