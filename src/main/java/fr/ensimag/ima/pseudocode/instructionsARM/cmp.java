package fr.ensimag.ima.pseudocode.instructionsARM;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Operand;

public class cmp extends BinaryInstruction {

	public cmp(GPRegister op1, Operand op2) {
		super(op1, op2);
	}

}
