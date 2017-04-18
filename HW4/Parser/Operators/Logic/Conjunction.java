package Parser.Operators.Logic;

import Parser.Operators.*;

public final class Conjunction extends BinaryOperation implements ILogic {
    public Conjunction(IExpression left, IExpression right) {
        super(left, right, "&");
    }

    @Override
    public IExpression clone() {
        return new Conjunction(getLeft().clone(), getRight().clone());
    }
}
