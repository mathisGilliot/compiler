package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import org.apache.log4j.Logger;
import java.util.Iterator;



public class ListDeclParam extends TreeList<AbstractDeclParam>{
	

	private static final Logger LOG = Logger.getLogger(ListDeclParam.class);
	
	@Override
	public void decompile(IndentPrintStream s) {
		if (!(this.isEmpty())){
		Iterator<AbstractDeclParam> it = this.iterator();
		it.next().decompile(s);
		while (it.hasNext()) {
            s.print(", ");
            it.next().decompile(s);
        	}
		}
	}

	/**
	 * 
	 * @param compiler
	 * @param currentClass
	 * @throws ContextualError
	 */

Signature verifyListDeclParam(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify list_decl_param: start");
    	Signature signature = new Signature();
    	Iterator<AbstractDeclParam> it = this.iterator();
    	while (it.hasNext()) {
			AbstractDeclParam expr = it.next();
			expr.verifyDeclParam(compiler, currentClass);
			signature.add(((DeclParam) expr).getType().getType()); //on met la signature à jour (2.8)
		}
    	
    	
    	LOG.debug("verify list_decl_param: end");
    	return signature;
    }
	
	
	/**
	 * 
	 * @param compiler
	 * @param envParams
	 * @throws ContextualError
	 */
	void verifyListDeclParamBody(DecacCompiler compiler,
            EnvironmentExp envParams) throws ContextualError {
    	LOG.debug("verify list_decl_param_body: start");
    	Iterator<AbstractDeclParam> it = this.iterator();
    	while (it.hasNext()) {
			AbstractDeclParam expr = it.next();
			expr.verifyDeclParamBody(compiler, envParams);
		}
    	LOG.debug("verify list_decl_param_body: end");
    }
	
	/**
	 * Génère le code des paramètres
	 * Attention doit empiler les params de droite à gauche
	 * @param compiler
	 * @param p
	 * @param className
	 */
	protected void codeGenParams(DecacCompiler compiler, Pile p, AbstractIdentifier className){
		DAddr addr;
		 // On laisse deux places pour PC et le pointeur vers la table des méthodes
		int i = 2;
		Iterator<AbstractDeclParam> it = getList().iterator();
		while(it.hasNext()){
			AbstractDeclParam declParam = it.next();
			i++;
			addr = new RegisterOffset(-i, Register.LB);
			declParam.setOperandParam(compiler, addr);
		}
	}
	
}
