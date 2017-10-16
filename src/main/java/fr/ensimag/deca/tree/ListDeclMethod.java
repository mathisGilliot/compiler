package fr.ensimag.deca.tree;

import java.util.Iterator;

import org.apache.log4j.Logger;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;

public class ListDeclMethod extends TreeList<AbstractDeclMethod>{

	private static final Logger LOG = Logger.getLogger(ListDeclMethod.class);

	@Override
	public void decompile(IndentPrintStream s) {
		for (AbstractDeclMethod m : getList()) {
			m.decompile(s);
			s.println();
		}
	}


	/**
	 * 
	 * @param compiler
	 * @param currentClass
	 * @throws ContextualError
	 */
	public void verifyListDeclMethod(DecacCompiler compiler, 
			ClassDefinition currentClass) throws ContextualError {
		LOG.debug("verify list_decl_method: start");

		int numberOfMethods = currentClass.getSuperClass().getNumberOfMethods(); //le nombre de champs qui est au moins égal au nombre de champs de la super classe
		int indexCourant = numberOfMethods + 1; //il démarre après l'indice le plus haut des champs de la super classe
		int index =indexCourant;
		Iterator<AbstractDeclMethod> it = this.iterator();
		while (it.hasNext()) {

			AbstractDeclMethod expr = it.next();

			//on parcourt la liste des Méthodes de la super classe et on regarde leurs indices
			ClassDefinition superClassDef = currentClass.getSuperClass();
			EnvironmentExp superEnvExp = superClassDef.getMembers(); //on récupère l'environnement de la super class
						
			DeclMethod exprMethod = (DeclMethod) expr;
			
			if (superEnvExp.getEnvironnement().containsKey(exprMethod.getMethodName().getName())) { //si l'environnement de la super classe contient la méthode courante
				
				MethodDefinition superEnvMeth = (MethodDefinition) superEnvExp.getEnvironnement().get(exprMethod.getMethodName().getName());
				index = superEnvMeth.getIndex();
				indexCourant--;
				numberOfMethods--;
			}
			
	    	expr.verifyDeclMethod(compiler, currentClass, index);

			indexCourant ++; 
			index = indexCourant;
			numberOfMethods++;

    	
    	
		}
		currentClass.setNumberOfMethods(numberOfMethods);//À chaque Definition de classe sont associés un nombre de méthodes
		LOG.debug("verify list_decl_method: end");
	}	

	
	public void verifyListDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, 
			ClassDefinition currentClass) throws ContextualError {
		LOG.debug("verify list_decl_method: start");
		Iterator<AbstractDeclMethod> it = this.iterator();
		while (it.hasNext()) {
			AbstractDeclMethod expr = it.next();
			expr.verifyDeclMethodBody(compiler, localEnv, currentClass);
		}
		LOG.debug("verify list_decl_method: end");
	}

	// AJOUT

	/**
	 * Met les étiquettes des méthodes déclarées
	 * dans le tableau des étiquettes
	 * @param compiler
	 * @param p
	 * @param className
	 */
	protected void setLabelListMethod(Label[] tabEtiquettes, String nomClasse){
		for (AbstractDeclMethod d : getList()) {
			d.setLabelMethodDefinition(tabEtiquettes, nomClasse);
		}
	}
	
	/**
	 * Génère le code des méhodes
	 * @param compiler
	 * @param regs
	 * @param p
	 * @param className
	 */
	protected void codeGenListMethod(DecacCompiler compiler, Registres regs, Pile p, AbstractIdentifier className){
		for (AbstractDeclMethod d : getList()) {
			d.codeGenMethod(compiler, regs, p, className);
		}
	}

}


