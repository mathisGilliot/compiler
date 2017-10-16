package fr.ensimag.ima.pseudocode.instructionsARM;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.TriInstruction;

public class add extends TriInstruction {
	
	public add(GPRegister op1, GPRegister op2, Operand op3) {
        super(op1, op2, op3);
    }
}
