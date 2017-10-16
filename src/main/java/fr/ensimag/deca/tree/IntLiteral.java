package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.IntType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.StringInstruction;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.B;
import fr.ensimag.ima.pseudocode.instructionsARM.BL;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;
import fr.ensimag.deca.tools.SymbolTable;
import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 * Integer literal
 *
 * @author gl27
 * @date 01/01/2017
 */
public class IntLiteral extends AbstractExpr {
	
	private static final Logger LOG = Logger.getLogger(IntLiteral.class);
	
    public int getValue() {
        return value;
    }

    private int value;

    public IntLiteral(int value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	SymbolTable symbTab = compiler.getTable();
    	SymbolTable.Symbol symb = symbTab.create("int");
    	IntType intType = new IntType(symb);
    	this.setType(intType);
    	LOG.debug("verify expr: end");
    	return intType;
    }


    @Override
    String prettyPrintNode() {
        return "Int (" + getValue() + ")";
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Integer.toString(value));
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
    	return new ImmediateInteger(this.getValue());
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.addInstruction(new LOAD(this.getValue(), r));
    }
    
    //Extension
    @Override
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r){
    	compiler.addInstruction(new MOV(r, this.getValue()));
    }

    @Override
    protected void codeGenPrintARM(DecacCompiler compiler, Registres regs, boolean printHex){    	
    	compiler.addInstruction(new LDR(Register.SP, new Label("=stack_top", true)));
    	compiler.addInstruction(new LDR(Register.R0, new Label("=I_"+ value, true)));
    	compiler.addInstruction(new LDR(Register.R0, new Label("["+ Register.R0 + "]", true)));  	
    	compiler.addInstruction(new BL(new Label("print_int_uart0")));
    	compiler.addInstruction(new B(new Label(".", true)));
    	compiler.getListLab().addListInt(value);
    }
    
    @Override
    protected Operand getOperandARM(DecacCompiler compiler, Registres regs){
    	return new ImmediateInteger(this.getValue());
    }
}
