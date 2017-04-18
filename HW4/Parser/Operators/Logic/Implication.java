package Parser.Operators.Logic;

import Parser.Operators.*;

public final class Implication extends BinaryOperation implements ILogic {
    public Implication(IExpression left, IExpression right) {
        super(left, right, "->");
    }

    @Override
    public IExpression clone() {
        return new Implication(getLeft().clone(), getRight().clone());
    }
}
