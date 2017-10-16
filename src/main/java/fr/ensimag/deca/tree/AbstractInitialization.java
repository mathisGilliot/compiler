package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

/**
 * Initialization (of variable, field, ...)
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractInitialization extends Tree {
	
	// AJOUT
	
	/**
	 * Génère le code propre à l'initialisation d'une variable
	 * @param compiler
	 * @param regs
	 * @param addr
	 * @param t
	 */
	protected abstract void codeInitialisationVar(DecacCompiler compiler, Registres regs, DAddr addr, Type t);
	
	/**
	 * 
	 * @param compiler
	 * @param regs
	 * @param addr
	 * @param t
	 */
	protected abstract void codeInitialisationField(DecacCompiler compiler, Registres regs, DAddr addr, Type t);
    
    /**
     * Implements non-terminal "initialization" of [SyntaxeContextuelle] in pass 3
     * @param compiler contains "env_types" attribute
     * @param t corresponds to the "type" attribute
     * @param localEnv corresponds to the "env_exp" attribute
     * @param currentClass 
     *          corresponds to the "class" attribute (null in the main bloc).
     */
    protected abstract void verifyInitialization(DecacCompiler compiler,
            Type t, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;
    

    protected abstract void codeInitialisationVarARM(DecacCompiler compiler, Registres regs, AbstractIdentifier varName, Type t);
	
}
