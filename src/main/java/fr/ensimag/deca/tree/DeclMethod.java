package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;


import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
	
	private static final Logger LOG = Logger.getLogger(DeclMethod.class);
	
	final private AbstractIdentifier typeReturn;
    final private AbstractIdentifier methodName;
    final private ListDeclParam params;
    final private AbstractMethodBody methodBody;
    
    /**
     * 
     * @param typeReturn
     * @param methodName
     * @param params
     * @param methodBody
     */
    public DeclMethod(AbstractIdentifier typeReturn, AbstractIdentifier methodName, ListDeclParam params, AbstractMethodBody methodBody) {
    	Validate.notNull(params);
    	Validate.notNull(typeReturn);
    	Validate.notNull(methodName);
    	Validate.notNull(methodBody);
    	this.params = params;
    	this.typeReturn = typeReturn;
    	this.methodName = methodName;
    	this.methodBody = methodBody;
    }
    
    public AbstractIdentifier getMethodName() {
    	return this.methodName;
    }

	@Override
	public void decompile(IndentPrintStream s) {
		s.print(typeReturn.getName().getName());
		s.print(" ");
		s.print(methodName.getName().getName());
		s.print(" (");
		this.params.decompile(s);
		s.println(") {");
		s.indent();
		this.methodBody.decompile(s);
		s.unindent();
		s.print("}");
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
    	this.typeReturn.prettyPrint(s, prefix, false);
    	this.methodName.prettyPrint(s, prefix, false);
    	this.params.prettyPrint(s, prefix, false);
    	this.methodBody.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
    	this.typeReturn.iterChildren(f);
    	this.methodName.iterChildren(f);
		this.params.iterChildren(f);
    	this.methodBody.iterChildren(f);
	}

	@Override
	protected void verifyDeclMethod(DecacCompiler compiler, ClassDefinition currentClass, int index)
			throws ContextualError {
		
    	LOG.debug("verify decl_method : start");

    	//Mise à jour de la table
    	SymbolTable.Symbol nameVerif = currentClass.getMembers().getTab().create(methodName.getName().getName());  	
    	
    	Type typeVerif = this.typeReturn.verifyType(compiler);
    	MethodDefinition expDef;
    	
    	
    	if (currentClass.getMembers().getEnvironnement().containsKey(this.methodName.getName())) {
    		throw new ContextualError("Le nom " + methodName.getName() + " est déjà utilisé dans la classe (règle 2.3)", this.getLocation());
    	}

    		
    	Signature signature = new Signature();
    	expDef = new MethodDefinition(typeVerif, this.getLocation(), signature , index); //voir slide 49 et p 95 pdf
    	LOG.debug("environnement avant put : " + currentClass.getMembers().toString());
    	currentClass.getMembers().getEnvironnement().put(nameVerif, expDef);
    	LOG.debug("environnement après put : " + currentClass.getMembers().toString());
    	this.methodName.setDefinition(expDef);
    	this.methodName.setType(typeVerif);



    	
    	ClassDefinition superClassDef = currentClass.getSuperClass();
//    	MethodDefinition methodDef = (MethodDefinition) currentClass.getMembers().getEnvironnement().get(nameVerif); //on récupère la définition de la méthode dans l'env de la classe courante
    	
    	LOG.debug("environnement avant mega cast : " + currentClass.getMembers().toString());
    	
    	((MethodDefinition) currentClass.getMembers().getEnvironnement().get(nameVerif)).setSignature(params.verifyListDeclParam(compiler, currentClass)); 
    	
    	LOG.debug("environnement après mega cast : " + currentClass.getMembers().toString());

    	// si la définition de la super classe est contenue dans l'env_type et que la méthode name est dans l'environnement de la super classe
    	// alors la méthode name doit avoir la même signature dans l'environnement de la classe super et le même type. 
    	// Et le type de la classe courante doit être un sous-type du
    	// type de la super classe relativement à env_type.

    	
    	
    	
    	if (compiler.getEnvType().getEnvironnement().containsValue(superClassDef) && superClassDef.getMembers().getEnvironnement().containsKey(methodName.getName())){
    		
    		MethodDefinition methodSuperDef = (MethodDefinition) currentClass.getSuperClass().getMembers().getEnvironnement().get(methodName.getName());
    		//on récupère la définition de la méthode dans l'environnement de la super classe
    	
    		
    		boolean b = compiler.getEnvType().subtype(currentClass.getType(), superClassDef.getType());
    		if (!((MethodDefinition) currentClass.getMembers().getEnvironnement().get(nameVerif)).getSignature().toString().equals(methodSuperDef.getSignature().toString()) || !b) {
    			
        		throw new ContextualError("La signature de la méthode " + methodName.getName() + " n'est pas la même que celle de la classe mère ( Règle 2.7)",this.getLocation());

    		}
    	}
    	
   
    	LOG.debug("verify decl_method : end");	
		
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((methodBody == null) ? 0 : methodBody.hashCode());
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((params == null) ? 0 : params.hashCode());
		result = prime * result
				+ ((typeReturn == null) ? 0 : typeReturn.hashCode());
		return result;
	}

	@Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeclMethod other = (DeclMethod) obj;
		if (methodBody == null) {
			if (other.methodBody != null)
				return false;
		} else if (!methodBody.equals(other.methodBody))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (params == null) {
			if (other.params != null)
				return false;
		} else if (!params.equals(other.params))
			return false;
		if (typeReturn == null) {
			if (other.typeReturn != null)
				return false;
		} else if (!typeReturn.equals(other.typeReturn))
			return false;
		return true;
	}
	
	@Override
	protected void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
		Type t = this.typeReturn.verifyType(compiler);
		EnvironmentExp envParams = new EnvironmentExp(currentClass.getMembers());
		this.params.verifyListDeclParamBody(compiler, envParams);
		this.methodBody.verifyMethodBody(compiler, envParams, currentClass, t);
	}

	/**
	 * On met le label dans la définition de la méthode
	 * (utile pour les classes filles)
	 * @param compiler
	 * @param nomClasse
	 */
	protected void setLabelMethodDefinition(Label[] tabEtiquettes, String nomClasse){
		Label e = new Label("code." + nomClasse + "." + this.getMethodName().getName().getName());
		this.methodName.getMethodDefinition().setLabel(e);
		int index = this.getMethodName().getMethodDefinition().getIndex();
		tabEtiquettes[index] = this.methodName.getMethodDefinition().getLabel();
	}
	
	// Génère le code de la méthode
	protected void codeGenMethod(DecacCompiler compiler, Registres regs, Pile p, AbstractIdentifier className){
		
		// Création d'un programme pour pouvoir utiliser
		// addFirst de IMAProgram pour TSTO
		IMAProgram program;
		IMAProgram progMethod = new IMAProgram();
		
		// On parcourt la liste des paramètres de droite à gauche
		// On empile les paramètres dans la pile
		params.codeGenParams(compiler, p, className);
		
		Registres regsUtilises = new Registres(regs.getRMax());
		
		// Sauvegarde le program du compiler
		program = compiler.getProgram();
		
		compiler.add(new Line("Code de la méthode " + this.getMethodName().getName().getName()));
		compiler.add(new Line(new Label("Code." + className.getName() + "." + this.getMethodName().getName().getName())));
		
		// Change le program du compiler
		compiler.setProgram(progMethod);
		methodBody.codeGenMethodBody(compiler, regsUtilises, p);
		
		// tsto = params + varLoc + regsSauv + temporaires
		int nbParams = regsUtilises.getMaxParamCalled();
		int nbVarLoc = methodBody.nbVar();
		int regMaxSauv = regsUtilises.getRegMax();
		int maxTemp = regsUtilises.getMaxTemp();
		int nbRegsSauv = regMaxSauv - 1;
		int tsto = nbParams + nbVarLoc + nbRegsSauv + maxTemp;

		// Sauvegarde des registres qu'on utilise
		program.addComment("Sauvegarde des registres");
		for (int i = regMaxSauv; i > 1; i--){
			progMethod.addFirst(new PUSH(Register.getR(i)));
		}
		progMethod.addFirst(new ADDSP(nbVarLoc));
		progMethod.addFirst(new BOV(new Label("stack_overflow_error")));
		progMethod.addFirst(new TSTO(tsto));
		
		// On restaure le program du compiler
		compiler.setProgram(program);
		
		compiler.getProgram().append(progMethod);
		
		compiler.addLabel(new Label("fin." + className.getName() + "." + this.getMethodName().getName().getName()));
		compiler.addComment("Restauration des registres");
		for (int i = regMaxSauv; i > 1 ; i--){
			compiler.addInstruction(new POP(Register.getR(i)));
		}
		
		if (!this.getMethodName().getMethodDefinition().getType().isVoid()){
			if (typeReturn.getType().isVoid()){
				throw new UnsupportedOperationException("Il manque un return");
			}
		}
		compiler.addInstruction(new RTS());
	}
	

}
