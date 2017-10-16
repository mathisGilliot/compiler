package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.LDR;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;

import java.io.PrintStream;
import org.apache.log4j.Logger;

/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public class BooleanLiteral extends AbstractExpr {
	
	private static final Logger LOG = Logger.getLogger(BooleanLiteral.class);

    private boolean value;

    public BooleanLiteral(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
    	LOG.debug("verify expr: start");
    	SymbolTable symbTab = compiler.getTable();
    	SymbolTable.Symbol symb = symbTab.create("boolean");
    	BooleanType booleanType = new BooleanType(symb);
    	this.setType(booleanType);
    	LOG.debug("verify expr: end");
    	return booleanType;
//        throw new UnsupportedOperationException("not yet implemented");
    }


    @Override
    public void decompile(IndentPrintStream s) {
        s.print(Boolean.toString(value));
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
    protected DVal getDVal(DecacCompiler compiler, Registres regs){
    	if (this.getValue()){
    		return new ImmediateInteger(1);
    	}
    	else {
    		return new ImmediateInteger(0);
    	}
    }

    @Override
    String prettyPrintNode() {
        return "BooleanLiteral (" + value + ")";
    }

    //AJOUT
    
    @Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	if (this.getValue()){
    		// ⟨Code(true, vrai, E)⟩ ≡ BRA E
    		if (b){
    			compiler.addInstruction(new BRA(label));
    		}
    		// ⟨Code(true, faux, E)⟩ ≡ ε (pas de code)
    		else {
    			// rien
    		}
    	}
    	// ⟨Code(false, b, E)⟩ ≡ ⟨Code(!true, b, E)⟩
    	else {
    		AbstractExpr expr = new Not(new BooleanLiteral(true));
    		expr.codeCond(compiler, regs, b, label);
    	}
    }
    
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r){
    	if (this.getValue()){
    		compiler.addInstruction(new LOAD(new ImmediateInteger(1), r));
    	}
    	else {
    		compiler.addInstruction(new LOAD(new ImmediateInteger(0), r));
    	}
    }
    
    //Extension
    @Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label label){
    	if (this.getValue()){
    		// ⟨Code(true, vrai, E)⟩ ≡ BRA E
    		if (b){
    			compiler.addInstruction(new BRA(label));
    		}
    		// ⟨Code(true, faux, E)⟩ ≡ ε (pas de code)
    		else {
    			// rien
    		}
    	}
    	// ⟨Code(false, b, E)⟩ ≡ ⟨Code(!true, b, E)⟩
    	else {
    		AbstractExpr expr = new Not(new BooleanLiteral(true));
    		expr.codeCondARM(compiler, regs, b, label);
    	}
    }
    
    @Override
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r){
    	if (this.getValue()){
    		compiler.addInstruction(new MOV(r, new ImmediateInteger(1)));
    	}
    	else {
    		compiler.addInstruction(new MOV(r, new ImmediateInteger(0)));
    	}
    }

}
