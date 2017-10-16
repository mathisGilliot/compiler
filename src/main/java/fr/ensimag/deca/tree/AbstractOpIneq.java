package fr.ensimag.deca.tree;


/**
 *
 * @author gl27
 * @date 01/01/2017
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

}
