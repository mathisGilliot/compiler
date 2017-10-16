package fr.ensimag.deca.tree;

import java.util.Iterator;
import org.apache.log4j.Logger;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField>{

	private static final Logger LOG = Logger.getLogger(ListDeclField.class);

	
	@Override
	public void decompile(IndentPrintStream s) {
		for (AbstractDeclField f : getList()) {
            f.decompile(s);
            s.println();
        }
	}
	
	/**
	 * 
	 * @param compiler
	 * @param localEnv
	 * @param currentClass
	 * @throws ContextualError
	 */
	public void verifyListDeclField(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify list_decl_field: start");
    	int numberOfFields = currentClass.getSuperClass().getNumberOfFields(); //le nombre de champs qui est au moins égal au nombre de champs de la super classe
    	int index= numberOfFields + 1; //il démarre après l'indice le plus haut des champs de la super classe
    	
    	Iterator<AbstractDeclField> it = this.iterator();
    	while (it.hasNext()) {
			AbstractDeclField expr = it.next();
			expr.verifyDeclField(compiler,localEnv, currentClass, index);
			index ++; 
			numberOfFields ++;
	    	  
		}
    	
    	
    	currentClass.setNumberOfFields(numberOfFields); //À chaque Definition de classe sont associés un nombre de champs (slide 49)
    	LOG.debug("verify list_decl_field: end");
    }
	

	
	
	/**
	 * 
	 * @param compiler
	 * @param localEnv
	 * @param currentClass
	 * @throws ContextualError
	 */
	public void verifyListDeclFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, 
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify list_decl_field: start");
    	Iterator<AbstractDeclField> it = this.iterator();
    	while (it.hasNext()) {
			AbstractDeclField expr = it.next();
			expr.verifyDeclFieldBody(compiler, localEnv, currentClass);
		}
    	LOG.debug("verify list_decl_field: end");
    }
	
	/**
	 * 
	 * @param compiler
	 */
	protected void init0(DecacCompiler compiler){
		for (AbstractDeclField d : getList()) {
            d.init0(compiler);
        }
	}
	
	/**
	 * 
	 * @param compiler
	 * @param regs
	 */
	public void codeGenListDeclField(DecacCompiler compiler, Registres regs) {
		for (AbstractDeclField d : getList()) {
            d.codeGenInitField(compiler, regs);
        }
	}

}
