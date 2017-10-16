package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;

/**
 * Binary expressions.
 *
 * @author gl27
 * @date 01/01/2017
 * 
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {
	/**
	 * 
	 * @return
	 */
    abstract protected String getOperatorName();

	/**
	 * 
	 * @return
	 */
    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    /**
     * 
     * @return
     */
    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    /**
     * 
     * @param leftOperand
     */
    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    /**
     * 
     * @param rightOperand
     */
    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    /**
     * 
     * @param leftOperand
     * @param rightOperand
     */
    public AbstractBinaryExpr(AbstractExpr leftOperand,
            AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    /**
     * @param s
     */
    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        getLeftOperand().decompile(s);
        s.print(" " + getOperatorName() + " ");
        getRightOperand().decompile(s);
        s.print(")");
    }


    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }
    
    /**
     * Génère le code commun aux expressions binaires
     * ie :
     * Réserve un registre (si besoin)
     * Évalue opDroit dans regisre
     * Libère le registre (si besoin)
     * @param compiler
     * @param r
     */
    @Override
    protected void codeExp(DecacCompiler compiler, Registres regs, GPRegister r) {
    	this.getLeftOperand().codeExp(compiler, regs, r);
    	 // Si r = RMax
    	if (r.getNumber() == regs.getRMax()){
    		compiler.addComment("Sauvegarde de R" + r.getNumber());
    		compiler.addInstruction(new PUSH(r));
    		regs.incrNbTemp();
    		regs.liberer(r);
    		getRightOperand().codeExp(compiler, regs, r);
    		compiler.addInstruction(new LOAD(r, Register.R0));
    		compiler.addComment("Restauration de R" + r.getNumber());
    		compiler.addInstruction(new POP(r));
    		regs.decrNbTemp();
    		codeBinExp(compiler, regs, r, Register.R0);
    	}
    	else {
    		DVal val = this.getRightOperand().getDVal(compiler, regs);
    		this.codeBinExp(compiler, regs, r, val);
    		val.liberer(regs);
    	}
    }
    
    /**
     * Génère le code de l'expression binaire
     * soit expArith, soit expBool
     * @param compiler
     * @param r
     * @param val
     */
    protected void codeBinExp(DecacCompiler compiler, Registres regs, GPRegister r, DVal val) {
    	throw new UnsupportedOperationException("not yet implemented");
    }

    /**
     * Code commun à tous les codeCond définis
     * dans les filles :
     * Évalue opGauche, opDroite puis génère CMP
     * @param compiler
     * @param b
     * @param e
     */
    @Override
    protected void codeCond(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	GPRegister r  = regs.reg_dispo();
    	this.getLeftOperand().codeExp(compiler, regs, r);
    	DVal val = this.getRightOperand().getDVal(compiler, regs);
    	codeCondCMP(compiler, r, val);
    	regs.liberer(r);
    }

    /**
     * Génère l'instruction CMP(val, r)
     * @param compiler
     * @param r
     * @param val
     */
    protected void codeCondCMP(DecacCompiler compiler, GPRegister r, DVal val) {
    	throw new UnsupportedOperationException("not yet implemented");
    }


    //Extension
    
    @Override
    protected void codeCondARM(DecacCompiler compiler, Registres regs, boolean b, Label e){
    	GPRegister r  = regs.reg_dispo();
    	this.getLeftOperand().codeExpARM(compiler, regs, r);
    	Operand val = this.getRightOperand().getOperandARM(compiler, regs);
    	codeCondCMPARM(compiler, r, val);
    	regs.liberer(r);
    }
    
	protected void codeBinExpARM(DecacCompiler compiler, Registres regs, GPRegister r, Operand val) {		
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	protected void codeCondCMPARM(DecacCompiler compiler, GPRegister r, Operand val) {
    	throw new UnsupportedOperationException("not yet implemented");
    }
	
	/**
     * Génère le code commun aux expressions binaires
     * ie :
     * Réserve un registre (si besoin)
     * Évalue opDroit dans regisre
     * Libère le registre (si besoin)
     * @param compiler
     * @param r
     */
	
    @Override
    protected void codeExpARM(DecacCompiler compiler, Registres regs, GPRegister r) {
    	this.getLeftOperand().codeExpARM(compiler, regs, r);
//    	 // Si r = RMax
//    	if (r.getNumber() == regs.getRMax()){
//    		compiler.addComment("Sauvegarde de R" + r.getNumber());
//    		compiler.addInstruction(new PUSH(r));
//    		regs.liberer(r);
//    		getRightOperand().codeExpARM(compiler, regs, r);
//    		compiler.addInstruction(new LOAD(r, Register.R0));
//    		compiler.addComment("Restauration de R" + r.getNumber());
//    		compiler.addInstruction(new POP(r));
//    		codeBinExpARM(compiler, regs, r, Register.R0);
//    	}
//    	else {
    	Operand val = this.getRightOperand().getOperandARM(compiler, regs);
    	this.codeBinExpARM(compiler, regs, r, val);
//    	}
    }
}
