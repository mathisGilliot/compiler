package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Erreur;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.StringInstruction;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import com.sun.xml.internal.xsom.impl.scd.Iterators.Map;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl27
 * @date 01/01/2017
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    
    /**
     * 
     * @param classes
     * @param main
     */
    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }
    
    public ListDeclClass getClasses() {
        return classes;
    }
    
    public AbstractMain getMain() {
        return main;
    }
    
    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        this.getClasses().verifyListClass(compiler); //Passe 1
        this.getClasses().verifyListClassMembers(compiler); //Passe 2 : on hérite de l'environnement construit au cours de la passe 1 qui contient env_types_predef, ainsi que les noms des différentes classes du programme
                                                            //le profil de chaque classe contient la super-classe, mais ne contient pas l’environnement des champs et méthodes de la classe.
        													//Lors de la passe 2, on vérifie les champs et la signature des méthodes des différentes classes.
        													//Le profil de chaque classe contient le nom de sa super-classe et l’environnement des champs et méthodes de la classe.
        this.getClasses().verifyListClassBody(compiler);
        this.getMain().verifyMain(compiler);
        LOG.debug("verify program: end");
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler, Registres regs, Pile p) {
    	
    	// Initialisation de la pile
    	p.codeGenInitPile(compiler);
    	// Appel à codegen.declClass pour table methodes
    	classes.codeGenListDeclClassTable(compiler, p);
    	// Parcours de l'arbre pour les setOperand
    	//classes.setOperandListDeclClass(compiler, p);
    	
        compiler.addComment("Main program");
        main.codeGenMain(compiler, regs, p);
        compiler.addInstruction(new HALT());
        
        // Appel aux messages d'erreurs
        Erreur.genCode(compiler);
        // Code de equals
        Label equals = new Label("code.Object.equals");
        compiler.addLabel(equals);
        // A SUPPRIMER TODO
        compiler.addInstruction(new HALT());
        // Code des classes
        compiler.addComment("Code des classes");
        classes.codeGenListInitField(compiler, regs, p);
        classes.codeGenListClassCodeMethod(compiler, regs, p);
    	
    }

    
    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }
    
    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
    
    //Extension
    public void codeGenProgramARM(DecacCompiler compiler, Registres regs, Pile p){
    	Label start = new Label("_start", true);
    	compiler.addInstruction(new StringInstruction(".global _start"));
		compiler.addLabel(start);		
		main.codeGenMainARM(compiler, regs, p);
		//Ajout des labels en fin
		Iterator<String> i = compiler.getListLab().getListString().keySet().iterator();
		while (i.hasNext())
		{
			String val = i.next();
			compiler.addLabel(compiler.getListLab().getListString().get(val));
			compiler.addInstruction(new StringInstruction(".ascii " + '"' +val+ '"'));		    
		}
		Iterator<Integer> j = compiler.getListLab().getListInt().keySet().iterator();
		while (j.hasNext())
		{
			Integer val = j.next();
			compiler.addLabel(compiler.getListLab().getListInt().get(val));
			compiler.addInstruction(new StringInstruction(".byte " +val));		    
		}
		Iterator<Label> k = compiler.getListLab().getListVar().keySet().iterator();
		while (k.hasNext())
		{
			Label val = k.next();
			compiler.addLabel(val); //compiler.getListLab().getListVar().get(val));
			compiler.addInstruction(new StringInstruction(".byte " +compiler.getListLab().getListVar().get(val)));		    
		}
    }
	
}
