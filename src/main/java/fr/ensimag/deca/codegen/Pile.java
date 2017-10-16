package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.Program;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Line;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.ADDSP;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;

public class Pile {
	private int taillePile = 2; // places pour null et codeObject
	
	public void empile() {
		taillePile++;
	}

	public void initTSTO(DecacCompiler compiler, Registres regs) {
		IMAProgram program = compiler.getProgram();
		program.addFirst(new ADDSP(new ImmediateInteger(taillePile)));
		program.addFirst(new BOV(new Label("stack_overflow_error")));
		int tsto = taillePile + regs.getMaxParamCalled() + regs.getMaxTemp();
		program.addFirst(new TSTO(new ImmediateInteger(tsto)));

	}

	public void codeGenInitPile(DecacCompiler compiler) {
		// Mettre null en 1 (GB) PARTIE OBJET
		compiler.addComment("Code de la table des méthodes de Object");
		compiler.addInstruction(new LOAD(new NullOperand(), Register.R0)); // LOAD #null,  R0
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(1, Register.GB))); // STORE R0, 1(GB)
		
		// Mettre @Object.equals en 2 (GB) PARTIE OBJET
		Label equals = new Label("code.Object.equals");
		// TabEtiquttes géré dans EnvironmentType
		compiler.addInstruction(new LOAD(new LabelOperand(equals), Register.R0)); // LOAD code.Object.equals, R0
		compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(2, Register.GB))); // STORE R0, 2(GB)

	}

	public RegisterOffset emplacement() {
		return new RegisterOffset(taillePile, Register.GB);
	}

}
