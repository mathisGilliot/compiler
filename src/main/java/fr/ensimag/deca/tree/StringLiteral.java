package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.StringType;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.StringInstruction;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import fr.ensimag.ima.pseudocode.instructionsARM.B;
import fr.ensimag.ima.pseudocode.instructionsARM.BL;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * String literal
 *
 * @author gl27
 * @date 01/01/2017
 */
public class StringLiteral extends AbstractStringLiteral {

	private static final Logger LOG = Logger.getLogger(StringLiteral.class);
	
    @Override
    public String getValue() {
        return value;
    }

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	SymbolTable symbTab = compiler.getTable();
    	SymbolTable.Symbol symb = symbTab.create("string");
    	StringType stringType = new StringType(symb);
    	this.setType(stringType);
    	LOG.debug("verify expr: end");
    	return stringType;
    }

    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
        compiler.addInstruction(new WSTR(new ImmediateString(value)));
    }

    @Override
    public void decompile(IndentPrintStream s) {
    	s.print("\""+ value +"\"");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }
    
    @Override
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }
    
    //Extension
    @Override
    protected void codeGenPrintARM(DecacCompiler compiler, Registres regs, boolean printHex){
    	compiler.addInstruction(new LDR(Register.SP, new Label("=stack_top", true)));
    	compiler.addInstruction(new LDR(Register.R0, new Label("="+ value, true)));
    	compiler.addInstruction(new BL(new Label("print_uart0")));
    	compiler.addInstruction(new B(new Label(".", true)));
    	compiler.getListLab().addListString(value);
    }
}
