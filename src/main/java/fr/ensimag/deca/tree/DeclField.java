package fr.ensimag.deca.tree;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import org.apache.log4j.Logger;

public class DeclField extends AbstractDeclField {
	
	private static final Logger LOG = Logger.getLogger(DeclParam.class);
    
	final private Visibility vis;
	final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;
    
    /**
     * 
     * @param vis
     * @param type
     * @param varName
     * @param initialization
     */
    public DeclField(Visibility vis,AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization ) {
    	Validate.notNull(vis);
    	Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.vis = vis;
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }
    
    public AbstractIdentifier getVarName() {
    	return this.varName;
    }


	@Override
	public void decompile(IndentPrintStream s) {
		// TODO Auto-generated method stub : check this
			if (vis.equals(Visibility.PROTECTED)){
				s.print("protected ");
			}
			this.type.decompile(s);
			s.print(" ");
			this.varName.decompile(s);
			this.initialization.decompile(s);
			s.print(";");
	}

	@Override
    protected String prettyPrintNode() {
        return "[visibility=" + getVisibility() + "] " + super.prettyPrintNode();
    }
	
	private Visibility getVisibility() {
		return vis;
	}




	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);	
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		type.iter(f);
        varName.iter(f);
        initialization.iter(f);	
	}
	
	@Override
	protected void verifyDeclField(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, int index)
            throws ContextualError {
		
		LOG.debug("verify decl_field: start");
		
		/* mise a jour de la table des symboles*/
		
    	SymbolTable.Symbol nameVerif = currentClass.getMembers().getTab().create(varName.getName().getName()); 
    	
    	
  

    	Type typeVerif = this.type.verifyType(compiler);
    	
    	if (typeVerif.isVoid()){
    		throw new ContextualError("Déclaration d'un champ de type void impossible (règle 2.5)",this.getLocation());
    	}
    	
    	
    	
    	//dans la classe courante, si un nom de champ, qui a le même nom qu'un objet de la classe super, existe, alors cet objet doit être un champ
    	ClassDefinition superClassDef = currentClass.getSuperClass(); //on récupère la définition de la super classe
    	EnvironmentExp envExpSuper = superClassDef.getMembers(); // et son environnement
    	
    	ClassDefinition classCour = currentClass; //on va parcourir toutes les classes au-dessus dans la hiérarchie
    	
    	while (superClassDef != null) { //tant qu'on peut remonter la hiérarchie
    		
    		//on met à jour
    		
    		envExpSuper = superClassDef.getMembers();
    		superClassDef = classCour.getSuperClass();
    		classCour = superClassDef;
    		
    		if (envExpSuper.getEnvironnement().containsKey(nameVerif) && envExpSuper.getEnvironnement().get(nameVerif).isMethod()) { //si un objet, ayant pour nom celui de notre champ, existe dans l'environnement de la super classe et que c'est un nom de méthode
    			throw new ContextualError("Le nom " + nameVerif + " ne correspond pas à un champ dans la classe mère (règle 2.5)", this.getLocation());
    		}
    		
    		
    		 
    	}
    	
    	/* mise a jour de environnement exp */
    	if (currentClass.getMembers().getEnvironnement().containsKey(this.varName.getName())) {
    		throw new ContextualError("le nom du field " + varName.getName() +" est déjà utilisé", this.getLocation());
    	}
    	
    	
    	if (!(compiler.getEnvType().getEnvironnement().containsKey(currentClass.getSuperClass().getType().getName()) )){ //on sait déjà que c'est un nom de classe
    		throw new ContextualError("la classe " + currentClass.getSuperClass() + " n'existe pas ! ( Règle 2.5)",this.getLocation());
    	}
    	else {
    		
    		
    		
    		FieldDefinition expDef = new FieldDefinition(typeVerif, this.getLocation(), this.getVisibility() , currentClass, index); //voir slide 49 et p 95 pdf
    		currentClass.getMembers().getEnvironnement().put(nameVerif, expDef);   //on ajoute la définition du champ à l'environnement		
    		//this.varName.verifyExpr(compiler, localEnv, currentClass);
    		
    		
    		this.varName.setDefinition(expDef);
    		this.varName.setType(typeVerif);
    	}
    	
    	LOG.debug("verify decl_field: end");
	}




	@Override
	protected void verifyDeclFieldBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
		Type t = this.type.verifyType(compiler);
		this.initialization.verifyInitialization(compiler, t, localEnv, currentClass);
	}
	
	
	protected void init0(DecacCompiler compiler){
		Type type = this.getVarName().getType();
		if (type.isBoolean()){
			compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
		}
		else if (type.isInt()){
			compiler.addInstruction(new LOAD(new ImmediateInteger(0), Register.R0));
		}
		else if (type.isFloat()){
			compiler.addInstruction(new LOAD(new ImmediateFloat(0), Register.R0));
		}
		else {
			throw new UnsupportedOperationException("not yet implemented");
		}
		DAddr addr = new RegisterOffset(this.getVarName().getFieldDefinition().getIndex(), Register.R1);
		compiler.addInstruction(new STORE(Register.R0, addr));
	}

	protected void codeGenInitField(DecacCompiler compiler, Registres regs) {
		compiler.addComment("Initialisation de " + varName.getName().getName());
    	// Génération : Initialisation de la valeur
		DAddr addrField = new RegisterOffset(-2, Register.LB);
		this.getVarName().getFieldDefinition().setOperand(addrField);
		DAddr addr = new RegisterOffset(this.getVarName().getFieldDefinition().getIndex(), Register.R1);
    	initialization.codeInitialisationField(compiler, regs, addr, this.getVarName().getType());
    	
	}

	
	
}
