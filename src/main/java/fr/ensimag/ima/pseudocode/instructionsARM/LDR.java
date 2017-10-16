package fr.ensimag.ima.pseudocode.instructionsARM;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;

public class LDR extends BinaryInstruction{
	
	
	public LDR(GPRegister op1, Operand op2) {
        super(op1, op2);
    }
	
	public LDR(Register op1, Operand op2) {
        super(op1, op2);
    }

    public LDR(GPRegister r, int i) {
        this(r, new ImmediateInteger(i));
    }
}
