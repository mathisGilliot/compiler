package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.FieldDefinition;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructionsARM.MOV;
import fr.ensimag.ima.pseudocode.instructionsARM.cmp;

/**
 * Left-hand side value of an assignment.
 * 
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractLValue extends AbstractExpr {

	public abstract ExpDefinition getExpDefinition();
	
	protected abstract DAddr getAddr(DecacCompiler compiler, GPRegister r);

	public FieldDefinition getFieldDefinition(){
		throw new UnsupportedOperationException("not yet implemented");
	}

	@Override
	protected void codeGenInst(DecacCompiler compiler, Registres regs) {
    	// On ne fait rien
    }
	
	//Extension
	@Override
	protected void codeGenInstARM(DecacCompiler compiler, Registres regs) {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	@Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label label){
		compiler.addInstruction(new LOAD(this.getDVal(compiler, regs), Register.R0));
    	compiler.addInstruction(new CMP(new ImmediateInteger(0), Register.R0));
    	// ⟨Code(idf , vrai, E)⟩ ≡ LOAD @idf, R0
    	//							CMP #0, R0
    	//							BNE E
    	if (b){
    		compiler.addInstruction(new BNE(label));
    	}
    	else {
    		// ⟨Code(idf , faux, E)⟩ ≡ LOAD @idf, R0
    		// 							CMP #0, R0
    		// 							BEQ E
    		compiler.addInstruction(new BEQ(label));
    	}
	}
	
	//Extension
	@Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label label){
		compiler.addInstruction(new MOV(Register.R0, this.getOperandARM(compiler, regs)));
    	compiler.addInstruction(new cmp(Register.R0, new ImmediateInteger(0)));
    	// ⟨Code(idf , vrai, E)⟩ ≡ LOAD @idf, R0
    	//							CMP #0, R0
    	//							BNE E
    	if (b){
    		compiler.addInstruction(new BNE(label));
    	}
    	else {
    		// ⟨Code(idf , faux, E)⟩ ≡ LOAD @idf, R0
    		// 							CMP #0, R0
    		// 							BEQ E
    		compiler.addInstruction(new BEQ(label));
    	}
	}
}
