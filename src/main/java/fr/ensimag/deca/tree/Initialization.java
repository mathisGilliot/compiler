package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructionsARM.STR;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import java.io.PrintStream;

/**
 * @author gl27
 * @date 01/01/2017
 */
public class Initialization extends AbstractInitialization {
	private static final Logger LOG = Logger.getLogger(Initialization.class);

	// AJOUT
	/**
	 * @param compiler
	 * @param regs
	 * @param addr
	 * @param t
	 */
	protected void codeInitialisationVar(DecacCompiler compiler, Registres regs, DAddr addr, Type t){
		/* Registre libre de stockage temporaire */
		GPRegister r = regs.reg_dispo();
		this.getExpression().codeExp(compiler, regs, r);
		compiler.addInstruction(new STORE(r, addr));
		regs.liberer(r);
	}

	/**
	 * @param compiler
	 * @param regs
	 * @param addr
	 * @param t
	 */
	protected void codeInitialisationField(DecacCompiler compiler, Registres regs, DAddr addr, Type t){
		this.getExpression().codeExp(compiler, regs, Register.R0);
		compiler.addInstruction(new STORE(Register.R0, addr));
	}

	/**
	 * 
	 * @return
	 */
	public AbstractExpr getExpression() {
        return expression;
    }

    private AbstractExpr expression;

    public void setExpression(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    /**
     * 
     * @param expression
     */
    public Initialization(AbstractExpr expression) {
        Validate.notNull(expression);
        this.expression = expression;
    }

    @Override
    protected void verifyInitialization(DecacCompiler compiler, Type t,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
    	LOG.debug("verify initialization: start");
    	if (this.getExpression() == null) {
    		throw new ContextualError("le champ expression de Initialization est null (Règle (3.8))",this.getLocation());
    	}
    	this.setExpression(this.getExpression().verifyRValue(compiler, localEnv, currentClass, t));
    	LOG.debug("verify initialization: end");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(" = ");
        expression.decompile(s);
    }

    @Override
    protected
    void iterChildren(TreeFunction f) {
        expression.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expression.prettyPrint(s, prefix, true);
    }

    //Extension
	@Override
	public void codeInitialisationVarARM(DecacCompiler compiler, Registres regs, AbstractIdentifier varName, Type t) {
		/* Registre libre de stockage temporaire */
		GPRegister r = regs.reg_dispo();
		this.getExpression().codeExpARM(compiler, regs, r);
		 //p.empile fait avant dans declVar ou declField
		compiler.addInstruction(new STR(r, new Label(varName.getName().getName(), true)));
		regs.liberer(r);
	}

    
}
