package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;

import java.io.PrintStream;
import java.util.Iterator;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Print statement (print, println, ...).
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractPrint extends AbstractInst {

	private static final Logger LOG = Logger.getLogger(AbstractPrint.class);
	
    private boolean printHex;
    private ListExpr arguments = new ListExpr();
    
    abstract String getSuffix();

    /**
     * Verifie que les arguments ne sont pas null puis print selon en hexadecimal
     * @param printHex
     * @param arguments
     */
    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    			LOG.debug("verify inst: start");
        		Iterator<AbstractExpr> it = this.arguments.iterator();
        		while (it.hasNext()) {
        			AbstractExpr expr = it.next();
        			Type type = expr.verifyExpr(compiler, localEnv, currentClass);
        			if (!(type.isString() || type.isFloat() || type.isInt())) {
        				throw new ContextualError("Une expression de type " + type.getName() + " n'est pas imprimable (règle 3.31)", this.getLocation());
        			}
        		}
        		LOG.debug("verify inst: end");
//        		throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler, Registres regs) {
        for (AbstractExpr a : getArguments().getList()) {
            codeGenPrint(compiler, regs, printHex, a);
        }
    }
    
    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler, Registres regs, boolean printHex, AbstractExpr expr) {
    	compiler.addComment("Print");
    	Type t = expr.getType();
		assert(t != null);
    	if (t.isString()){
    		expr.codeExp(compiler, regs, null);
    	}
    	else {
    		// Évaluation de l'expression à afficher dans R1
    		compiler.addInstruction(new LOAD(expr.getDVal(compiler, regs), Register.R1));
    		if (t.isInt()){
    			compiler.addInstruction(new WINT());
    		}
    		else if (t.isFloat()){
    			if (printHex){
    				compiler.addInstruction(new WFLOATX());
    			}
    			else {
    				compiler.addInstruction(new WFLOAT());
    			}
    		}
    		else if (t.isBoolean()){
    			throw new UnsupportedOperationException("Boolean : interdiction de print");
    		}
    		else {
    			throw new UnsupportedOperationException("not yet implemented");
    		}
    	}
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("print" + getSuffix() );
        if (getPrintHex()){
        	s.print("x");
        	}
        s.print("(");
        getArguments().decompile(s);
        s.print(")");
        s.print(";");
        
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }
    
    //Extension
    @Override
    protected void codeGenInstARM(DecacCompiler compiler, Registres regs) {
    	for (AbstractExpr a : getArguments().getList()) {
            a.codeGenPrintARM(compiler, regs, printHex);
        }
    }
}
