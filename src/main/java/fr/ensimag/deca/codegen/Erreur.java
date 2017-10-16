package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.ERROR;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;

public class Erreur {
	
	public static void genCode(DecacCompiler compiler){
		// Arithmetic Overflow
		Label labArithOverflow = new Label("overflow_error");
		compiler.addLabel(labArithOverflow);
		compiler.addInstruction(new WSTR("Error: Overflow during arithmetic operation"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
		
		// Stack Overflow
		Label labStackOverflow = new Label("stack_overflow_error");
		compiler.addLabel(labStackOverflow);
		compiler.addInstruction(new WSTR("Error: Stack Overflow"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
		
		// io_error
		Label labIO = new Label("io_error");
		compiler.addLabel(labIO);
		compiler.addInstruction(new WSTR("Error: Input/Output error"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
		
		//dereferencement null 
		Label derefNull = new Label("deferencement_null");
		compiler.addLabel(derefNull);
		compiler.addInstruction(new WSTR("Error: dereferencement de null"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
		
		//dereferencement null 
		Label tasPlein = new Label("tas_plein");
		compiler.addLabel(tasPlein);
		compiler.addInstruction(new WSTR("Error: tas plein"));
		compiler.addInstruction(new WNL());
		compiler.addInstruction(new ERROR());
	}

}
