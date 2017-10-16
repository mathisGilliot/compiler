package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.FloatType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.StringInstruction;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.B;
import fr.ensimag.ima.pseudocode.instructionsARM.BL;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Single precision, floating-point literal
 *
 * @author gl27
 * @date 01/01/2017
 */
public class FloatLiteral extends AbstractExpr {

	private static final Logger LOG = Logger.getLogger(FloatLiteral.class);
	
    public float getValue() {
        return value;
    }

    private float value;

    /**
     * 
     * @param value
     */
    public FloatLiteral(float value) {
        Validate.isTrue(!Float.isInfinite(value),
                "literal values cannot be infinite");
        Validate.isTrue(!Float.isNaN(value),
                "literal values cannot be NaN");
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	SymbolTable symbTab = compiler.getTable();
    	SymbolTable.Symbol symb = symbTab.create("float");
    	FloatType floatType = new FloatType(symb);
    	this.setType(floatType);
    	LOG.debug("verify expr: end");
    	return floatType;      
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(java.lang.Float.toString(value));
    }

    @Override
    String prettyPrintNode() {
        return "Float (" + getValue() + ")";
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
    	// leaf node => nothing to do
    }

    //===== AJOUT =====

    @Override
    protected DVal getDVal(DecacCompiler compiler, Registres regs){
    	return new ImmediateFloat(this.getValue());
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.addInstruction(new LOAD(this.getDVal(compiler, regs), r));
    }
    
    //Extension
    @Override
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.addInstruction(new MOV(r, this.getDVal(compiler, regs)));
    }
    
    @Override
    protected void codeGenPrintARM(DecacCompiler compiler, Registres regs, boolean printHex){
    	Label string = new Label("F_" +value, true);
    	
    	compiler.addInstruction(new LDR(Register.SP, new Label("=stack_top", true)));
    	compiler.addInstruction(new LDR(Register.R0, new Label("=F_"+ value, true)));
    	compiler.addInstruction(new LDR(Register.R0, new Label("["+ Register.R0 + "]", true)));
    	
    	compiler.addInstruction(new BL(new Label("print_int_uart0")));
    	compiler.addInstruction(new B(new Label(".", true)));
    	compiler.addLabel(string);
    	compiler.addInstruction(new StringInstruction(".byte " + value));
    }

}
