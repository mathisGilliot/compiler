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
 *
 * @author gl27
 * @date 01/01/2017
 */
public class While extends AbstractInst {
	
	private static final Logger LOG = Logger.getLogger(While.class);

    private AbstractExpr condition;
    private ListInst body;

    public AbstractExpr getCondition() {
        return condition;
    }

    public ListInst getBody() {
        return body;
    }

    /**
     * 
     * @param condition
     * @param body
     */
    public While(AbstractExpr condition, ListInst body) {
        Validate.notNull(condition);
        Validate.notNull(body);
        this.condition = condition;
        this.body = body;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass, Type returnType)
            throws ContextualError {
    	LOG.debug("verify inst: start");
    	this.getCondition().verifyCondition(compiler, localEnv, currentClass);
    	this.getBody().verifyListInst(compiler, localEnv, currentClass, returnType);
    	LOG.debug("verify inst: end");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("while (");
        getCondition().decompile(s);
        s.println(") {");
        s.indent();
        getBody().decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        condition.iter(f);
        body.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        condition.prettyPrint(s, prefix, false);
        body.prettyPrint(s, prefix, true);
    }
    
    @Override
    protected void codeGenInst(DecacCompiler compiler, Registres regs) {
    	//    	⟨Code(while (C) { I } )⟩ ≡ BRA E_Cond.n
    	//    	E_Debut.n :
    	//			⟨Code(I )⟩
    	//    	E_Cond.n :
    	//    		⟨Code(C, vrai, E_Debut.n)⟩
    	compiler.getCompteurEtiquette().addCompteur();
    	String cond = "E_Cond" + compiler.getCompteurEtiquette().getCompteur();
    	String debLoop = "debLoop" + compiler.getCompteurEtiquette().getCompteur();
    	Label labDebLoop = new Label(debLoop);
    	Label labCond = new Label(cond);
    	compiler.addInstruction(new BRA(labCond));
    	compiler.addLabel(labDebLoop);
    	// Liste instructions while
    	body.codeGenListInst(compiler, regs);
    	compiler.addLabel(labCond);
    	condition.codeCond(compiler, regs, true, labDebLoop);
    }

    //Extension
	@Override
	protected void codeGenInstARM(DecacCompiler compiler, Registres regs) {

		//    	⟨Code(while (C) { I } )⟩ ≡ BRA E_Cond.n
    	//    	E_Debut.n :
    	//			⟨Code(I )⟩
    	//    	E_Cond.n :
    	//    		⟨Code(C, vrai, E_Debut.n)⟩
		compiler.getCompteurEtiquette().addCompteur();
    	String cond = "E_Cond" + compiler.getCompteurEtiquette().getCompteur();
    	String debLoop = "debLoop" + compiler.getCompteurEtiquette().getCompteur();
    	Label labDebLoop = new Label(debLoop);
    	Label labCond = new Label(cond);
    	compiler.addInstruction(new B(labCond));
    	compiler.addLabel(labDebLoop);
    	// Liste instructions while
    	body.codeGenListInstARM(compiler, regs);
    	compiler.addLabel(labCond);
    	condition.codeCondARM(compiler, regs, true, labDebLoop);

		
	}

}
