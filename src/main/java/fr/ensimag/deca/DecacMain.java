package fr.ensimag.deca;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

/**
 * Main class for the command-line Deca compiler.
 *
 * @author gl27
 * @date 01/01/2017
 */
public class DecacMain {
    private static Logger LOG = Logger.getLogger(DecacMain.class);
    
    public static void main(String[] args) {
        // example log4j message.
        LOG.info("Decac compiler started");
        boolean error = false;
        final CompilerOptions options = new CompilerOptions();
        try {
            options.parseArgs(args);
        } catch (CLIException e) {
            System.err.println("Error during option parsing:\n"
                    + e.getMessage());
            options.displayUsage();
            System.exit(1);
        }
        if (options.getPrintBanner()) {
        	System.out.println("+------------------------------------+");
        	System.out.println("|            Equipe n°27             |");
        	System.out.println("+------------------------------------+");

        }
        if (options.getSourceFiles().isEmpty()) {
        	if (!options.getPrintBanner()) {
        		options.displayUsage();
        	}
        	else {
        		System.exit(0);
        	}
        }
        if (options.getParallel()) {
        	ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        	for (File source : options.getSourceFiles()){
            	DecacCompiler compiler = new DecacCompiler(options, source);
            	Future<Boolean> submit = executor.submit(compiler);
            	try {
					submit.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
        	}
        	executor.shutdown();
        	
        } else {
            for (File source : options.getSourceFiles()) {
                DecacCompiler compiler = new DecacCompiler(options, source);
                if (compiler.compile()) {
                    error = true;
                }
            }
        }
        System.exit(error ? 1 : 0);
    }
}
