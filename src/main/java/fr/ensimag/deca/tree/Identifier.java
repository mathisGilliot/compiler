package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Definition;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.deca.context.MethodDefinition;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.VariableDefinition;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.B;
import fr.ensimag.ima.pseudocode.instructionsARM.BL;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca Identifier
 *
 * @author gl27
 * @date 01/01/2017
 */
public class Identifier extends AbstractIdentifier {
    
	private static final Logger LOG = Logger.getLogger(Identifier.class);
	
    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * 
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     * 
     * @throws DecacInternalError
     *             if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public Symbol getName() {
        return name;
    }

    private Symbol name;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    		LOG.debug("verify expr: start");
    		LOG.debug("\nOn travaille dans l'environnement : " + localEnv.toString());

    		if (localEnv.getEnvironnement().get(name) == null) {

    			throw new ContextualError("Il n'y a pas de définition associée au nom " + name + " (règle 0.1)", this.getLocation());

    		}
    		else {
    			Type typeExp = localEnv.getEnvironnement().get(name).getType();
    			Definition definition = localEnv.getEnvironnement().get(name);
    			this.setDefinition(definition);
    			if (this.getDefinition().isMethod() || this.getDefinition().isClass()) {
    				throw new ContextualError(this.name.getName() + " ne correspond pas à un champ, à un paramètre ou à une variable (règles 3.67, 3.68 et 3.69)", this.getLocation());
    			}
    			this.setType(typeExp);
    			LOG.debug("verify expr: end");
    			return typeExp;
    		}
    		
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
    	
    	LOG.debug("verify type: start");
    	
    	if (!compiler.getEnvType().getEnvironnement().containsKey(name)) {
    		

    		throw new ContextualError("Le type " + name + " n'existe pas (règle 0.2)", this.getLocation());
    	}
    	else {
    		LOG.debug(name.getName() + " est de type " + compiler.getEnvType().get(name).getType());
    		TypeDefinition definition = new TypeDefinition(compiler.getEnvType().get(name).getType(), compiler.getEnvType().get(name).getLocation());
    		setDefinition(definition);
    		setType(compiler.getEnvType().get(name).getType());
    		LOG.debug("verify type: end");
    		return compiler.getEnvType().get(name).getType();
    	}
    }
    
    @Override
    public Type verifyMethodIdent(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    		LOG.debug("verify expr: start");
    		
    		LOG.debug("\nOn travaille dans l'environnement : " + localEnv.toString());
//    		LOG.debug("\nOn travaille dans la classe : " + currentClass.getType().getName());
    		
    		if (localEnv.getEnvironnement().get(name) == null) {

    			throw new ContextualError("Il n'y a pas de définition associée au nom " + name + " (règle 0.1)", this.getLocation());

    		}
    		else {
    			Type typeExp = localEnv.getEnvironnement().get(name).getType();
    			Definition definition = localEnv.getEnvironnement().get(name);// VariableDefinition(typeExp, getLocation());
    			this.setDefinition(definition);
        		this.setType(typeExp);
        		//this.getVariableDefinition().setOperand(localEnv.getEnvironnement().get(name).getOperand()); //TODO à décommenter pour la partie C	
            	LOG.debug("verify expr: end");
    			return typeExp;
    		}
    		
    }
    
    private Definition definition;


    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }
    
    // AJOUT
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
    	if (this.getExpDefinition().isField()){
    		int index = this.getFieldDefinition().getIndex();
    		compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), r));
    		compiler.addInstruction(new LOAD(new RegisterOffset(index, r), r));
    	}
    	else {
    		compiler.addInstruction(new LOAD(this.getDVal(compiler, regs), r));
    	}
    }

    @Override
    protected DAddr getAddr(DecacCompiler compiler, GPRegister r){
    	if (this.getExpDefinition().isField()){
    		//compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), r));
    		compiler.addInstruction(new LOAD(this.getExpDefinition().getOperand(), r));
    		int index = getFieldDefinition().getIndex();
    		// Adresse du champ
    		return new RegisterOffset(index, r);
    	}
    	return this.getExpDefinition().getOperand();
    }

    // Uniquement à utiliser avec des vraies variables
    @Override
    protected DVal getDVal(DecacCompiler compiler, Registres regs){
    	return this.getExpDefinition().getOperand();
    }
    
    //Extension
    @Override
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.addInstruction(new LDR(r, new Label("=" + this.getName().getName(), true)));
    	compiler.addInstruction(new LDR(r, new Label("[" + r + "]", true)));
    }
    
    protected void codeGenPrintARM(DecacCompiler compiler, Registres regs, boolean printHex) {
    	compiler.addInstruction(new LDR(Register.SP, new Label("=stack_top", true)));
    	compiler.addInstruction(new LDR(Register.R0, new Label("="+ getName().getName(), true)));
    	compiler.addInstruction(new LDR(Register.R0, new Label("["+ Register.R0 + "]", true)));  	
    	compiler.addInstruction(new BL(new Label("print_int_uart0")));
    	compiler.addInstruction(new B(new Label(".", true)));
    }

    @Override
    protected Operand getOperandARM(DecacCompiler compiler, Registres regs){
    	GPRegister r = regs.reg_dispo();
    	compiler.addInstruction(new LDR(r, new Label("=" + this.getName().getName(), true)));
    	compiler.addInstruction(new LDR(r, new Label("[" + r + "]", true)));
    	return r;
    }


}
