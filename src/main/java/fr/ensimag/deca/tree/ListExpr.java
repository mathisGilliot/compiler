package fr.ensimag.deca.tree;


import java.util.Iterator;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * List of expressions (eg list of parameters).
 *
 * @author gl27
 * @date 01/01/2017
 */
public class ListExpr extends TreeList<AbstractExpr> {


	@Override
	public void decompile(IndentPrintStream s) {
		if (!(this.isEmpty())){
			Iterator<AbstractExpr> it = this.iterator();
			it.next().decompile(s);
			while (it.hasNext()) {
				s.print(", ");
				it.next().decompile(s);
			}
		}
	}

	/**
	 * 
	 * @param compiler
	 * @param regs
	 * @param r
	 */
	protected void empileArgs(DecacCompiler compiler, Registres regs, GPRegister r){
		Iterator<AbstractExpr> it = this.iterator();
		int i = 0;
		while (it.hasNext()) {
			i ++;
			AbstractExpr exp = it.next();
			exp.codeExp(compiler, regs, r);
			compiler.addInstruction(new STORE(r, new RegisterOffset(-i, Register.SP)));
		}
	}
}
