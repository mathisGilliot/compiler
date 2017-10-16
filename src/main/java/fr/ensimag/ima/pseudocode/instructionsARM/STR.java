package fr.ensimag.ima.pseudocode.instructionsARM;

import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;

public class STR extends BinaryInstruction {
	
	public STR(Register op1, DAddr op2) {
        super(op1, op2);
    }
	
	public STR(GPRegister op1, Label op2) {
        super(op1, op2);
    }
	
	public STR(Register op1, Label op2) {
        super(op1, op2);
    }

}
