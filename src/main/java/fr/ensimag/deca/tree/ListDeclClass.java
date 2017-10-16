package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public class ListDeclClass extends TreeList<AbstractDeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);
    
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    /**
     * 
     * @param compiler
     * @throws ContextualError
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        Iterator <AbstractDeclClass> it = this.iterator(); 
        while(it.hasNext()){
        	AbstractDeclClass classe = it.next();
        	classe.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    /**
     * 
     * @param compiler
     * @throws ContextualError
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
    	LOG.debug("verify listClassMembers: start");
        Iterator <AbstractDeclClass> it = this.iterator(); 
        while(it.hasNext()){
        	AbstractDeclClass classe = it.next();
        	classe.verifyClassMembers(compiler);
        }
        LOG.debug("verify listClassMembers: end");
    }
    
    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    /**
     * 
     * @param compiler
     * @throws ContextualError
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
    	LOG.debug("verify listClassBody: start");
    	Iterator <AbstractDeclClass> it = this.iterator(); 
        while(it.hasNext()){
        	AbstractDeclClass classe = it.next();
        	classe.verifyClassBody(compiler);
        }
    	LOG.debug("verify listClassBody: end");
    }
    
    // AJOUT
    
    /** Génère le code de la table des méthodes */
    /**
     * 
     * @param compiler
     * @param p
     */
	public void codeGenListDeclClassTable(DecacCompiler compiler, Pile p) {
        for (AbstractDeclClass d : getList()) {
            d.codeGenClassTable(compiler, p);
        }
    }
	
	
	/** Génère le code d'initialisation des champs */
	/**
	 * 
	 * @param compiler
	 * @param regs
	 * @param p
	 */
	protected void codeGenListInitField(DecacCompiler compiler, Registres regs, Pile p) {
		for (AbstractDeclClass d : getList()) {
        	d.codeGenInitField(compiler, regs, p);
        }
	}
	
	/**
	 * 
	 * @param compiler
	 * @param regs
	 * @param p
	 */
	protected void codeGenListClassCodeMethod(DecacCompiler compiler, Registres regs, Pile p) {
		for (AbstractDeclClass d : getList()) {
        	d.codeGenClassCodeMethod(compiler, regs, p);
        }
	}

}
