package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
//import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructionsARM.B;
import fr.ensimag.ima.pseudocode.instructionsARM.BL;
//import fr.ensimag.ima.pseudocode.instructions.WINT;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import java.io.PrintStream;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractExpr extends AbstractInst {
	

	private static final Logger LOG = Logger.getLogger(AbstractExpr.class);
	
    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }
    
    /**
     * 
     * @param type
     */
    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }
    private Type type;

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
    }

    /**
     * Verify the expression for contextual error.
     * 
     * implements non-terminals "expr" and "lvalue" 
     *    of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  (contains the "env_types" attribute)
     * @param localEnv
     *            Environment in which the expression should be checked
     *            (corresponds to the "env_exp" attribute)
     * @param currentClass
     *            Definition of the class containing the expression
     *            (corresponds to the "class" attribute)
     *             is null in the main bloc.
     * @return the Type of the expression
     *            (corresponds to the "type" attribute)
     */
    public abstract Type verifyExpr(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments 
     * 
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler  contains the "env_types" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute            
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
            EnvironmentExp localEnv, ClassDefinition currentClass, 
            Type expectedType)
            throws ContextualError {
    	LOG.debug("verify rvalue: start");
    	LOG.debug("EnvExp dans verifyRValue : " + localEnv.toString());
    	Type type = this.verifyExpr(compiler, localEnv, currentClass);
    	if (!compiler.getEnvType().assignCompatible(expectedType, type)) { 
    		throw new ContextualError("Affectation à un objet de type " + expectedType.getName() + " une valeur de type " + type.getName() + " impossible (règle 3.28)", this.getLocation());
    	}
    	if (!compiler.getEnvType().subtype(type, expectedType)) {
    		if (compiler.getEnvType().assignCompatible(expectedType, type)) { 
    			// Cas où on doit faire un ConvFloat
    			AbstractExpr newExp = new ConvFloat(this);
    			Type newType = newExp.verifyExpr(compiler, localEnv, currentClass);
    			LOG.trace("newType = " + newType.getName());
    	    	LOG.trace("getType avant setType " + this.getType());
    			this.setType(newType);
    			LOG.trace("getType après setType " + this.getType());
    			LOG.debug("verify rvalue: end");
    			return newExp;
    		}
    	}
    	LOG.debug("verify rvalue: end");
    	return this;    	
    }
    
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	LOG.debug("verify inst: start");
    	this.verifyExpr(compiler, localEnv, currentClass);
    	LOG.debug("verify inst: end");
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv
     *            Environment in which the condition should be checked.
     * @param currentClass
     *            Definition of the class containing the expression, or null in
     *            the main program.
     */
    void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify condition: start");
    	Type type = this.verifyExpr(compiler, localEnv, currentClass);
    	if (!type.isBoolean()) {
    		throw new ContextualError("La condition n'est pas un booléen (règle 3.22)", this.getLocation());
    	}
    	LOG.debug("verify condition: end");
    }

    
    /**
     * Génère le code d'une instruction
     * @param compiler
     */
    protected void codeGenInst(DecacCompiler compiler, Registres regs) {
    	getDVal(compiler, regs);
    }
    
    /**
     * 
     * @param compiler
     * @param r
     */
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
    	throw new UnsupportedOperationException("not yet implemented");
    }
    
    /**
     * 
     * @param compiler
     * @param b
     * @param e
     */
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	throw new UnsupportedOperationException("not yet implemented");
    }
    
    protected DAddr getAddr(DecacCompiler compiler, GPRegister r){
    	throw new UnsupportedOperationException("not yet implemented");
    }
    
    /**
     * Une AbstractExp a forcément une valeur
     * Méthode Override dans IntLiteral, FloatLiteral, BooleanLiteral, Identifier
     * @param compiler
     * @return valeur de l'expression
     */
    protected DVal getDVal(DecacCompiler compiler, Registres regs){
    	GPRegister r = regs.reg_dispo();
    	codeExp(compiler, regs, r);
    	return r;
    }
    
    @Override
    protected void decompileInst(IndentPrintStream s) {
        decompile(s);
        s.print(";");
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
    
    protected void decompilePoint(IndentPrintStream s){
    	s.print(".");
    }
    
    // Extension
	
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label label) {
		throw new UnsupportedOperationException("not yet implemented");		
	}

	/**
     * Génère le code d'une instruction
     * @param compiler
     */
    protected void codeGenInstARM(DecacCompiler compiler, Registres regs) {
    	getOperandARM(compiler, regs);
    }

	protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r) {
		throw new UnsupportedOperationException("not yet implemented");	
	}
	
	/**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrintARM(DecacCompiler compiler, Registres regs, boolean printHex) { 
    	compiler.addInstruction(new MOV(Register.R0, this.getOperandARM(compiler, regs)));
    	if (this.getType().isInt()){	
        	compiler.addInstruction(new BL(new Label("print_int_uart0")));
        	compiler.addInstruction(new B(new Label(".", true)));  		
    	}
    }
    
    protected Operand getOperandARM(DecacCompiler compiler, Registres regs){
    	GPRegister r = regs.reg_dispo();
    	codeExpARM(compiler, regs, r);
    	return r;
    }
}
