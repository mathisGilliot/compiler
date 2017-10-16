package fr.ensimag.deca.tree;

import java.io.PrintStream;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Pile;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public class MethodBody extends AbstractMethodBody {

	private static final Logger LOG = Logger.getLogger(DeclParam.class);

	private ListDeclVar declVariables;
    private ListInst insts;
    
    /**
     * 
     * @param declVariables
     * @param insts
     */
    
    public MethodBody(ListDeclVar declVariables, ListInst insts) {
        Validate.notNull(declVariables);
        Validate.notNull(insts);
        this.declVariables = declVariables;
        this.insts = insts;
    }

	@Override
	public void decompile(IndentPrintStream s) {
		declVariables.decompile(s);
		s.println();
		insts.decompile(s);
		
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		declVariables.prettyPrint(s, prefix, false);
        insts.prettyPrint(s, prefix, true);
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		declVariables.iter(f);
        insts.iter(f);
	}
	
	// Utile pour TSTO
	@Override
	protected int nbVar(){
		return this.declVariables.size();
	}
	
	@Override
	protected void verifyMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError{
		LOG.debug("verifyMethodBody: start");
		this.declVariables.verifyListDeclVariable(compiler, localEnv, currentClass);
		LOG.debug("Env des var et params : " + localEnv.toString());
		LOG.debug("Env de la classe " + currentClass.getType().getName() + " : " + currentClass.getMembers().toString());
		localEnv.EmpileExp(currentClass.getMembers());
		LOG.debug("Env des var et params / env class : " + localEnv.toString());
		this.insts.verifyListInst(compiler, localEnv, currentClass, returnType);    	
    	LOG.debug("verifyMethodBody: end");
	}
	
	/**
	 * @param compiler
	 * @param regs
	 * @param p
	 */
	protected void codeGenMethodBody(DecacCompiler compiler, Registres regs, Pile p){
		declVariables.codeGenListDeclVar(compiler, regs, p); 
		insts.codeGenListInst(compiler, regs);
	}

}
