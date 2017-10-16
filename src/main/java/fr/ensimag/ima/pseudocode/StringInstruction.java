package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

public class StringInstruction extends Instruction {

	private String contenuLabel;
	
	
	public StringInstruction(String contenuLabel) {
		this.contenuLabel = contenuLabel;
	}

	@Override
	void displayOperands(PrintStream s) {
		//pas d'operand
		
	}

	public String getContenuLabel() {
		return contenuLabel;
	}

	public void setContenuLabel(String contenuLabel) {
		this.contenuLabel = contenuLabel;
	}
	
	@Override
	void display(PrintStream s) {
        s.print(contenuLabel);
    }

}
