package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.Registres;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructionsARM.B;

import java.io.PrintStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Full if/else if/else statement.
 *
 * @author gl27
 * @date 01/01/2017
 */
public class IfThenElse extends AbstractInst {
	
	private static final Logger LOG = Logger.getLogger(IfThenElse.class);
    
    private final AbstractExpr condition; 
    private final ListInst thenBranch;
    private ListInst elseBranch;
    
   
    /**
     * 
     * @param condition
     * @param thenBranch
     * @param elseBranch
     */
    public IfThenElse(AbstractExpr condition, ListInst thenBranch, ListInst elseBranch) {
        Validate.notNull(condition);
        Validate.notNull(thenBranch);
        Validate.notNull(elseBranch);
        this.condition = condition;
        this.thenBranch = thenBranch;
        this.elseBranch = elseBranch;
    }
    
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	LOG.debug("verify inst: start");
    	condition.verifyCondition(compiler, localEnv, currentClass);
    	thenBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    	elseBranch.verifyListInst(compiler, localEnv, currentClass, returnType);
    	LOG.debug("verify inst: end");
    }

    // AJOUT
    @Override
    protected void codeGenInst(DecacCompiler compiler, Registres regs) {
    	compiler.getCompteurEtiquette().addCompteur();
    	String FinIf = "finIf";
    	FinIf = FinIf + compiler.getCompteurEtiquette().getCompteur();
    	Label labFinIf = new Label(FinIf);
    	
    	if (!elseBranch.getList().isEmpty()) {
    		String chaineElse = "else";
    		chaineElse = chaineElse + compiler.getCompteurEtiquette().getCompteur();
    		Label labElse = new Label(chaineElse);
    		// Si la condition est fausse, on branche sur else
    		condition.codeCond(compiler, regs, false, labElse);
        	// Liste instructions if
        	thenBranch.codeGenListInst(compiler, regs);
        	compiler.addInstruction(new BRA(labFinIf));
        	compiler.addLabel(labElse);
        	// Liste instuctions else
        	elseBranch.codeGenListInst(compiler, regs);
    		
    	} else {
    		// Si la condition est fausse, on branche sur labFinIf
    		condition.codeCond(compiler, regs, false, labFinIf);
    		thenBranch.codeGenListInst(compiler, regs);
    	}
    	
    	compiler.addLabel(labFinIf);
    }

    @Override
    public void decompile(IndentPrintStream s) {
    	s.print("if (");
    	condition.decompile(s);
    	s.print(") {");
    	s.println();
    	s.indent();
    	this.thenBranch.decompile(s);
    	s.unindent();
    	s.println("}");
    	s.println("else {");
    	s.indent();
    	this.elseBranch.decompile(s);
    	s.unindent();
    	s.print("}");
    	}

    @Override
    protected
    void iterChildren(TreeFunction f) {
        condition.iter(f);
        thenBranch.iter(f);
        elseBranch.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        thenBranch.prettyPrint(s, prefix, false);
        elseBranch.prettyPrint(s, prefix, true);
    }

	public void setElseBranch(ListInst tree) {
		this.elseBranch = tree;
		
	}

	public ListInst getElseBranch() {
		return this.elseBranch;
		
	}
	
	//Extension
	@Override
    protected void codeGenInstARM(DecacCompiler compiler, Registres regs) {
    	compiler.getCompteurEtiquette().addCompteur();
    	String FinIf = "finIf";
    	FinIf = FinIf + compiler.getCompteurEtiquette().getCompteur();
    	Label labFinIf = new Label(FinIf);
    	
    	if (!elseBranch.getList().isEmpty()) {
    		String chaine_else = "else";
    		chaine_else = chaine_else + compiler.getCompteurEtiquette().getCompteur();
    		Label labElse = new Label(chaine_else);

    		// Si la condition est fausse, on branche sur else
    		condition.codeCondARM(compiler, regs, false, labElse);
        	// Liste instructions if
        	thenBranch.codeGenListInstARM(compiler, regs);
        	compiler.addInstruction(new B(labFinIf));
        	compiler.addLabel(labElse);
        	// Liste instuctions else
        	elseBranch.codeGenListInstARM(compiler, regs);
    		
    	} else {
    		// Si la condition est fausse, on branche sur labFinIf
    		condition.codeCondARM(compiler, regs, false, labFinIf);
    		thenBranch.codeGenListInstARM(compiler, regs);
    	}
    	
    	compiler.addLabel(labFinIf);
    }
	
	
	
	
}
