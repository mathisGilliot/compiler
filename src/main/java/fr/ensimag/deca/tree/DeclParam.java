package fr.ensimag.deca.tree;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.EnvironmentExp.DoubleDefException;
import fr.ensimag.deca.context.ExpDefinition;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;


import java.io.PrintStream;


public class DeclParam extends AbstractDeclParam {
	private static final Logger LOG = Logger.getLogger(DeclParam.class);

	final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    
    /**
     * 
     * @param type
     * @param varName
     */
    public DeclParam(AbstractIdentifier type, AbstractIdentifier varName) {
    	Validate.notNull(type);
    	Validate.notNull(varName);
    	this.type = type;
    	this.varName = varName;
    }
    public AbstractIdentifier getType() {
    	return type;
    }
    
	@Override
	public void decompile(IndentPrintStream s) {
		this.type.decompile(s);
		s.print(" ");
		this.varName.decompile(s);
	}

	@Override
	protected void prettyPrintChildren(PrintStream s, String prefix) {
		type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, true);		
	}

	@Override
	protected void iterChildren(TreeFunction f) {
		type.iter(f);
        varName.iter(f);		
	}
	
	@Override
	protected void verifyDeclParam(DecacCompiler compiler,
             ClassDefinition currentClass)
            throws ContextualError {
		
		LOG.debug("verify decl_param: start");
    	
    	Type typeVerif = this.type.verifyType(compiler);
    	
    	if (typeVerif.isVoid()){
    		throw new ContextualError("Déclaration d'un paramètre de type void impossible (règle 2.9)",this.getLocation());
    	}
    	   	
    	    	  	
    	type.setType(typeVerif) ;

	}
	
	@Override
	protected void verifyDeclParamBody(DecacCompiler compiler,
            EnvironmentExp localEnv)
           throws ContextualError {
		Type t = this.type.verifyType(compiler);
		ExpDefinition paramDef = new ParamDefinition(t, this.getLocation());
		try {
			localEnv.declare(this.varName.getName(), paramDef);
			this.varName.setDefinition(paramDef);
		} catch (DoubleDefException e) {
			throw new ContextualError("Le paramètre " + varName.getName() +" existe déjà (règle 3.12)", this.getLocation());
		}
	}
	
	// Empiler le param dans la pile
	protected void setOperandParam(DecacCompiler compiler, DAddr addr){
    	varName.getExpDefinition().setOperand(addr);
	}

}
