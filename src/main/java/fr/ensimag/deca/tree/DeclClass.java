package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ClassType;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;


import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 * 
 * @author gl27
 * @date 01/01/2017
 */
public class DeclClass extends AbstractDeclClass {
	
	private static final Logger LOG = Logger.getLogger(DeclClass.class);

	final private AbstractIdentifier className;
	final private AbstractIdentifier superClassName;
	final private ListDeclField listDeclField;
	final private ListDeclMethod listDeclMethod;
	
	
	/**
	 * 
	 * @param className
	 * @param superClassName
	 * @param listDeclField
	 * @param listDeclMethod
	 */
	public DeclClass(AbstractIdentifier className, AbstractIdentifier superClassName, ListDeclField listDeclField, ListDeclMethod listDeclMethod){
		Validate.notNull(className);
		Validate.notNull(superClassName);
		Validate.notNull(listDeclField);
		Validate.notNull(listDeclMethod);
		this.className = className;
		this.superClassName = superClassName;
		this.listDeclField = listDeclField;
		this.listDeclMethod = listDeclMethod;
	}
	
    @Override
    public void decompile(IndentPrintStream s) {
    	// definition decompile de class
    	s.print("class ");
    	this.className.decompile(s);
    	s.print(" ");
    	if (superClassName.getName().getName() != "Object"){
    		s.print("extends ");
    		this.superClassName.decompile(s);
    	}
    	s.println("{");
    	s.indent();
    	this.listDeclField.decompile(s);
    	this.listDeclMethod.decompile(s);
    	s.unindent();
    	s.print("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
    	if (!(compiler.getEnvType().getEnvironnement().containsKey(superClassName.getName()) && compiler.getEnvType().get(superClassName.getName()).isClass())){
    		throw new ContextualError("La classe " + superClassName.getName() + " n'existe pas (règle 1.3)",this.getLocation());
    	}
    	
		LOG.debug("verify Class: start");
    	SymbolTable.Symbol nameVerif = compiler.getTable().create(className.getName().getName());
    	
    	ClassType classType;
    	ClassDefinition classDef;
    	if (this.superClassName != null) {
    		ClassDefinition superClassDef = (ClassDefinition) compiler.getEnvType().get(superClassName.getName()); //on a son nom. On va chercher sa déf dans l'environnement (il y a au moins Object)
    		this.superClassName.setDefinition(superClassDef); //on l'attribue à l'AbstractIdentifier superClassName
    		classType = new ClassType(className.getName(),this.getLocation(),superClassDef);
    		classDef = new ClassDefinition(classType,this.getLocation(), superClassDef); 
    	} else {
    		classType = new ClassType(className.getName(),this.getLocation(),null);
    		classDef = new ClassDefinition(classType,this.getLocation(), null); 
    	}
    	
    	if (compiler.getEnvType().getEnvironnement().containsKey(className.getName())) {
    		throw new ContextualError("La classe " + className.getName() + " existe déjà (règle 1.3)", this.getLocation());
    	}
    	
    	
    	compiler.getEnvType().getEnvironnement().put(nameVerif, classDef);
    	
    	this.className.setDefinition(classDef);
    	this.className.setType(classType);
    	
    	LOG.debug("\nEnvironmentType : \n" + compiler.getEnvType().toString());
//    	LOG.debug("EnvironmentExp de " + this.superClassName.getName() + " : " + this.superClassName.getClassDefinition().getMembers().toString());
//    	LOG.debug("EnvironmentExp de " + this.superClassName.getName() + " : " + ((ClassDefinition) compiler.getEnvType().get(superClassName.getName())).getMembers().toString());
    	
		LOG.debug("verify Class: end");

    	
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) //Le profil de chaque classe contient le nom de sa super-classe et l’environnement des champs et méthodes de la classe.
            throws ContextualError {
//    	throw new UnsupportedOperationException("not yet implemented");
    	LOG.debug("verify ClassMembers: start");

    	
    	//SymbolTable.Symbol nameVerif = classDef.getMembers().getTab().create(superClassName.getName().getName()); //on met le symbole de la super classe dans la table des symboles
    	
    	// si superClassName n'est pas contenue dans l'env_type ou qu'il ne s'agit pas d'une classe
//    	if (!(compiler.getEnvType().getEnvironnement().containsValue(superClassName.getClassDefinition()))){
    	//LOG.debug("\nEnvironmentType DEBUT : \n" + compiler.getEnvType().toString());
    	if (!(compiler.getEnvType().getEnvironnement().containsKey(superClassName.getName())&& compiler.getEnvType().get(superClassName.getName()).isClass())) {
    		throw new ContextualError("La classe " + superClassName.getName() + " n'existe pas (règle 2.3)",this.getLocation());
    	}
    	else {
        
    		
        //ClassDefinition classDef = (ClassDefinition)compiler.getEnvType().getEnvironnement().get(this.className); 
//        System.out.println("la définition de la classe est : " + className.getClassDefinition());
//    	System.out.println("le nom de la superclasse est : " + superClassName.getName());
//    	System.out.println("le nom de la superclass est " + className.getClassDefinition().getNature());

        //ClassDefinition classDef = className.getClassDefinition();	//les classes ont été mises dans l'environnement lors de la passe 1
        //ClassDefinition superClassDef = superClassName.getClassDefinition();
        
    	listDeclField.verifyListDeclField(compiler, className.getClassDefinition().getMembers(), className.getClassDefinition()); //on fournit l'env_exp de la classe ici (méthodes et fields)
    	listDeclMethod.verifyListDeclMethod(compiler, className.getClassDefinition()); 
    	    	
//    	LOG.debug("\nAvant Empile: EnvironmentExp de " + className.getName() + " : \n" + className.getClassDefinition().getMembers().toString());

    	
    	className.getClassDefinition().getMembers().EmpileExp(superClassName.getClassDefinition().getMembers());
    	    	
    	compiler.getEnvType().getEnvironnement().put(className.getName(), className.getClassDefinition()); 
    	//on écrase l'ancienne définition de la super classe pour la remplacer par celle avec son environnement (qui est concaténé avec l'environnement de la nouvelle classe qui l'étend): 2.3
    	
    	}
    	LOG.debug("\nEnvironmentExp de " + className.getName() + " : \n" + className.getClassDefinition().getMembers().toString());
    	//LOG.debug("\nEnvironmentType FIN : \n" + compiler.getEnvType().toString());
    	LOG.debug("verify ClassMembers: end");

    }
    
    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
//    	throw new UnsupportedOperationException("not yet implemented");
    	LOG.debug("verify ClassBody: start");

    	if ((!this.className.getType().isClass()) || (!compiler.getEnvType().getEnvironnement().containsKey(this.className.getName()))) {
    		throw new ContextualError("La classe " + this.className.getName() + " n'existe pas (règle 3.5)", this.getLocation());

    	}
    	ClassDefinition currentClass = this.className.getClassDefinition();
    	EnvironmentExp localEnv = currentClass.getMembers();
    	LOG.debug("\nEnvironmentExp de " + this.className.getName() + " : \n" + localEnv.toString());
    	this.listDeclField.verifyListDeclFieldBody(compiler, localEnv, currentClass);
    	this.listDeclMethod.verifyListDeclMethodBody(compiler, localEnv, currentClass);
    	LOG.debug("verify ClassBody: end");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    	this.className.prettyPrint(s, prefix, false);
		this.superClassName.prettyPrint(s, prefix, false);
		this.listDeclField.prettyPrint(s, prefix, false);
		this.listDeclMethod.prettyPrint(s, prefix, true);
//        throw new UnsupportedOperationException("Not yet supported");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
    	this.className.iterChildren(f);
		this.superClassName.iterChildren(f);
		this.listDeclField.iterChildren(f);
		this.listDeclMethod.iterChildren(f);
    }
    
    // AJOUT
    
    protected void codeGenClassTable(DecacCompiler compiler, Pile p){
    	compiler.addComment("Code de la table des méthodes de " + className.getName().getName());
    	// On empile un pointeur sur la table des méthodes de la super-classe
    	// place dans la pile = adresse de la classe
    	p.empile();
    	DAddr addrClass = p.emplacement();
    	
    	// On stocke l'adresse de la table dans ClassDefinition
    	// pour plus tard
    	className.getClassDefinition().setOperand(addrClass);
    	
    	DAddr superOperand = superClassName.getClassDefinition().getOperand();
    	compiler.addInstruction(new LEA(superOperand, Register.R0));
    	compiler.addInstruction(new STORE(Register.R0, p.emplacement()));
    	
    	// Tableau des étiquettes
    	Label[] tabEtiquettes = className.getClassDefinition().getTabEtiquettes();
    	
    	// Parent : On met les étiquettes des méthodes héritées dans le tableau
    	Label [] tabEtiquettesParent = superClassName.getClassDefinition().getTabEtiquettes();
    	int tailleTableParente = tabEtiquettesParent.length;
    	for (int i = 1; i < tailleTableParente; i ++){
    		tabEtiquettes[i] = tabEtiquettesParent[i];
    	}
    	
    	// On met les étiquettes des méthodes de la classe dans le tableau
    	String nomClasse = className.getName().getName();
    	listDeclMethod.setLabelListMethod(tabEtiquettes, nomClasse);
    	
    	// On ajoute le code des étiquettes dans la pile
    	// Pour les méthodes héritées
    	for (int i = 1; i < tailleTableParente; i++){
    		p.empile();
    		LOAD inst = new LOAD(new LabelOperand(tabEtiquettes[i]), Register.R0);
    		if (tabEtiquettes[i] == tabEtiquettesParent[i]){
    			compiler.addInstruction(inst, "Héritage de la méthode de " + superClassName.getName().getName());
    		}
    		else {
    			compiler.addInstruction(inst, "Redéfinition de la méthode ");
    		}
    		compiler.addInstruction(new STORE(Register.R0, p.emplacement()));
    	}
    	// Pour les méthodes de la classe
    	for (int i = tailleTableParente; i < tabEtiquettes.length; i++){
    		p.empile();
    		compiler.addInstruction(new LOAD(new LabelOperand(tabEtiquettes[i]), Register.R0));
    		compiler.addInstruction(new STORE(Register.R0, p.emplacement()));
    	}
    }
    
    
    /**
     * Sous programme d'initialisation des champs de la classe
     * @param compiler
     * @param regs
     * @param p
     */
    protected void codeGenInitField(DecacCompiler compiler, Registres regs, Pile p){ // TODO enlever la pile
    	compiler.addComment("Initialisation des champs de " + className.getName().getName());
    	Label init = new Label("init." + className.getName().getName());
    	compiler.addLabel(init);
    	// TSTO
    	// BOV pilePleine
    	
    	// Initialiser les superChamps
    	if (superClassName.getName().getName() != "Object") {
    		// On met l’adresse de l’objet passé en paramètre dans R1
        	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
    		// Appelle à NoInitialisation des champs de la classe
        	listDeclField.init0(compiler);
        	compiler.addComment("Appel de l’initialisation des champs hérités de " + superClassName.getName().getName());
    		compiler.addInstruction(new PUSH(Register.R1));
    		compiler.addInstruction(new BSR(new Label("init." + superClassName.getName().getName())));
    		compiler.addInstruction(new SUBSP(new ImmediateInteger(1)));
    	}

    	// Initialisation des champs de la classe
    	// On met l’adresse de l’objet passé en paramètre dans R1
    	compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
    	listDeclField.codeGenListDeclField(compiler, regs);
    	compiler.addInstruction(new RTS());
    }
    
    protected void codeGenClassCodeMethod(DecacCompiler compiler, Registres regs, Pile p){
    	listDeclMethod.codeGenListMethod(compiler, regs, p, className);
    }

}
