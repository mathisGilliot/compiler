package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import java.io.PrintStream;

/**
 * @author gl27
 * @date 01/01/2017
 */
public class DeclVar extends AbstractDeclVar {
	
	private static final Logger LOG = Logger.getLogger(DeclVar.class);
    
    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    /**
     * 
     * @param type
     * @param varName
     * @param initialization
     */
    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }
   
    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	LOG.debug("verify decl_var: start");
    	SymbolTable.Symbol nameVerif = localEnv.getTab().create(varName.getName().getName()); //met aussi la table des symboles à jour
    	Type typeVerif = this.type.verifyType(compiler);//compiler.getEnvType().get(nameVerif).getType();
    	
    	if (typeVerif.isVoid()){
    		throw new ContextualError("Déclaration d'une variable de type void impossible (règle 3.17)",this.getLocation());
    	}
    	
    	// mise à jour de l'environnement exp
    	if (localEnv.getEnvironnement().containsKey(this.varName.getName())) {
    		throw new ContextualError("La variable " + varName.getName() + " existe déjà (règle 3.17)", this.getLocation());
    	}
    	else {
    		VariableDefinition expDef = new VariableDefinition(typeVerif, this.getLocation());
    		localEnv.getEnvironnement().put(nameVerif, expDef);
    		
    		this.varName.verifyExpr(compiler, localEnv, currentClass);
    		this.varName.setDefinition(expDef);
    	}
    	// verification de l'initialisation
    	this.initialization.verifyInitialization(compiler, typeVerif, localEnv, currentClass);
    	LOG.debug("verify decl_var: end");
    	
    }

    protected void codeGenDeclVar(DecacCompiler compiler, Registres regs, Pile p){
    	// Augmente la pile d'une unité
    	p.empile();
    	compiler.addComment("Déclaration " + varName.getName().getName());
    	// On décore Définition de l'operand associée
    	varName.getVariableDefinition().setOperand(p.emplacement());
    	// Génération : Initialisation de la valeur
    	initialization.codeInitialisationVar(compiler, regs, p.emplacement(), this.varName.getType());
    }

    @Override
    public void decompile(IndentPrintStream s) {
    	this.type.decompile(s);
    	s.print(" ");
    	this.varName.decompile(s);
    	this.initialization.decompile(s);
    	s.print(";");
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }
    
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }

    //Extension
	@Override
	public void codeGenDeclVarARM(DecacCompiler compiler, Registres regs, Pile p) {
    	// On crée décore Définition de l'operand associée
    	compiler.getListLab().addListVar(varName.getName().getName());
    	// Génération : Initialisation de la valeur
    	initialization.codeInitialisationVarARM(compiler, regs, varName, this.varName.getType());
	}
}
