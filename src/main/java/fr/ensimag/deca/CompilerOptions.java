package fr.ensimag.deca;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.lang.Runtime;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fr.ensimag.deca.codegen.Registres;

/**
 * User-specified options influencing the compilation.
 *
 * @author gl27
 * @date 01/01/2017
 */
public class CompilerOptions {
    public static final int QUIET = 0;
    public static final int INFO  = 1;
    public static final int DEBUG = 2;
    public static final int TRACE = 3;
    public int getDebug() {
        return debug;
    }

    public boolean getParallel() {
        return parallel;
    }

    public boolean getPrintBanner() {
        return printBanner;
    }
    
    public List<File> getSourceFiles() {
        return Collections.unmodifiableList(sourceFiles);
    }
    
    public boolean getStopAfterParse(){
    	return stopAfterParse;
    }
    
    public boolean getStopAfterVerify(){
    	return stopAfterVerify;
    }
    
    public int getNbReg(){
    	return nbReg;
    }
    
   public boolean getOptionArm(){
	   return optionARM;
   }

    private int debug = 0;
    // gestion de la restriction de registre
    private int nbReg = 16;
    private boolean parallel = false;
    private boolean printBanner = false;
    // boolean de gestion de l'incompatibilite des options -p et -v
    private boolean incompatible = false; 
    private boolean stopAfterParse = false;
    private boolean stopAfterVerify = false;
    private List<File> sourceFiles = new ArrayList<File>();
    
    private boolean optionARM = false;

    
    public void parseArgs(String[] args) throws CLIException {
    	for(int i=0; i< args.length ;i++){
    	switch (args[i]){
	    	case "-b" :
	    		if (parallel || getStopAfterParse() || getStopAfterVerify() || (args.length != 1) || optionARM) {
	    			throw new CLIException("Incompatibilite d'option : -b doit etre utiliser seul");
	    		}
	    		printBanner = true;
	    		break;
	    	case "-p" : 
	    		if (!incompatible){
	    			incompatible = true;
	    			stopAfterParse = true;
	    		}
	    		else {
	    			throw new CLIException("Incompatibilite d'option : -v non applicable apres -p");
	    		}
	    		break;
	    	case "-v" :
	    		if (!incompatible){
	    			incompatible = true;
	    			stopAfterVerify = true;
	    		}
	    		else {
	    			throw new CLIException("Incompatibilite d'option : -p non applicable apres -v");
	    		}
	    		break;
	    	case "-n" :
	    		throw new CLIException("not yet implemented ");
	    		//break;
	    	case "-r" :
	    		{
	    		if ( i + 1 < args.length ){
	    		    nbReg = Integer.parseInt(args[i+1]);
	    		    i++;
	 	   			if (!(3<nbReg && nbReg<17 )){
	     				throw new CLIException("Commande Invalide : int compris entre 4 et 16");
		    			}
    		    	}
	    		else {
	    			throw new CLIException("Commande Invalide : -r doit etre suivi d'un int ");
	    			}
	    		}
	    		break;
	    	case "-d" :
	    		if (debug < 3){
	    			debug ++;
	    		}
	    		else {
	    			throw new CLIException("Option au maximum");
	    		}
	    		break;
	    	case "-P" :
	    		parallel = true;
	    		break;
	    	case "-ARM" : 
	    		optionARM = true;
	    		break;
	    	default:
	    		this.sourceFiles.add(new File(args[i]));
	    		break;
	    	}
    	}
        Logger logger = Logger.getRootLogger();
        
        // map command-line debug option to log4j's level.
        switch (getDebug()) {
        case QUIET: break; // keep default
        case INFO:
            logger.setLevel(Level.INFO); break;
        case DEBUG:
            logger.setLevel(Level.DEBUG); break;
        case TRACE:
            logger.setLevel(Level.TRACE); break;
        default:
            logger.setLevel(Level.ALL); break;
        }
        logger.info("Application-wide trace level set to " + logger.getLevel());

        boolean assertsEnabled = false;
        assert assertsEnabled = true; // Intentional side effect!!!
        if (assertsEnabled) {
            logger.info("Java assertions enabled");
        } else {
            logger.info("Java assertions disabled");
        }
        
    }

    protected void displayUsage() {
    	System.out.println("\n Options display : \n");
    	System.out.println(" decac [[-p | -v] [-n] [-r X] [-d]* [-P] [-w] <fichier deca>...] | [-b]\n");
    	System.out.println("\t -b (banner) \t \t: affiche une bannière indiquant le nom de l’équipe");
    	System.out.println("\t -p (parser ) \t \t: Arrête decac après l’étape de construction de l’arbre, \n \t \t \t \t et affiche la décompilation de ce dernier");
    	System.out.println("\t -v (verification) \t: arrête decac après l’étape de vérifications");
    	System.out.println("\t -n (no check) \t \t: supprime les tests de débordement à l’exécution");
    	System.out.println("\t -r X (register) \t: limite les registres banalisés disponibles à \n \t \t \t \t R0 ... R{X-1}, avec 4 <= X <= 16");
    	System.out.println("\t -d (debug) \t \t:  active les traces de debug. Répéter l’option plusieurs fois pour avoir plus de traces");
    	System.out.println("\t -P (parallel) \t \t:  lance la compilation des fichiers en parallèle\n ");
    }
}
