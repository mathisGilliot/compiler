package fr.ensimag.ima.pseudocode;


import fr.ensimag.deca.codegen.Registres;

/**
 * General Purpose Register operand (R0, R1, ... R15).
 * 
 * @author Ensimag
 * @date 01/01/2017
 */
public class GPRegister extends Register {
    /**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }

    private int number;

    GPRegister(String name, int number) {
        super(name);
        this.number = number;
    }
    
    @Override
    public void liberer(Registres regs){
    	regs.liberer(this);
    }
    
}
