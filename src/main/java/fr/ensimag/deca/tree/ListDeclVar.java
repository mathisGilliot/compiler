package fr.ensimag.deca.tree;

import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.log4j.Logger;

/**
 * List of declarations (e.g. int x; float y,z).
 * 
 * @author gl27
 * @date 01/01/2017
 */
public class ListDeclVar extends TreeList<AbstractDeclVar> {
	
	private static final Logger LOG = Logger.getLogger(ListDeclVar.class);


	/** Génère le code des déclarations */
	/**
	 * 
	 * @param compiler
	 */
	public void codeGenListDeclVar(DecacCompiler compiler, Registres regs, Pile p) {
        for (AbstractDeclVar d : getList()) {
            d.codeGenDeclVar(compiler, regs, p);
        }
    }
	
    @Override
    public void decompile(IndentPrintStream s) {
    	for (AbstractDeclVar d : getList()) {
    		d.decompile(s);
    		s.println();
    	}
    }

    /**
     * Implements non-terminal "list_decl_var" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains the "env_types" attribute
     * @param localEnv 
     *   its "parentEnvironment" corresponds to "env_exp_sup" attribute
     *   in precondition, its "current" dictionary corresponds to 
     *      the "env_exp" attribute
     *   in postcondition, its "current" dictionary corresponds to 
     *      the "env_exp_r" attribute
     * @param currentClass 
     *          corresponds to "class" attribute (null in the main bloc).
     */    
    void verifyListDeclVariable(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify list_decl_var: start");
    	Iterator<AbstractDeclVar> it = this.iterator();
    	while (it.hasNext()) {
			AbstractDeclVar expr = it.next();
			expr.verifyDeclVar(compiler,localEnv, currentClass);
		}
    	LOG.debug("verify list_decl_var: end");
    }

    //Extension
	public void codeGenListDeclVarARM(DecacCompiler compiler, Registres regs, Pile p) {
		for (AbstractDeclVar d : getList()) {
            d.codeGenDeclVarARM(compiler, regs, p);
        }
	}


}
