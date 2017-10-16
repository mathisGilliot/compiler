package fr.ensimag.ima.pseudocode;


import fr.ensimag.deca.codegen.Registres;

/**
 * Operand that contains a value.
 * 
 * @author Ensimag
 * @date 01/01/2017
 */
public abstract class DVal extends Operand {
	
	// AJOUT
	// Si DVal a alloué une valeur,
	// on la libère
	public void liberer(Registres regs){

	}

}
