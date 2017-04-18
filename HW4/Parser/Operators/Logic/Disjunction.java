package Parser.Operators.Logic;

import Parser.Operators.*;

public final class Disjunction extends BinaryOperation implements ILogic {
    public Disjunction(IExpression left, IExpression right) {
        super(left, right, "|");
    }

    @Override
    public IExpression clone() {
        return new Disjunction(getLeft().clone(), getRight().clone());
    }
}
