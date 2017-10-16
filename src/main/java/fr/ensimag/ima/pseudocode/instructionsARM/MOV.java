package fr.ensimag.ima.pseudocode.instructionsARM;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Operand;

public class MOV extends BinaryInstruction{
	
	public MOV(GPRegister op1, Operand op2) {
        super(op1, op2);
    }

    public MOV(GPRegister r, int i) {
        this(r, new ImmediateInteger(i));
    }
}

