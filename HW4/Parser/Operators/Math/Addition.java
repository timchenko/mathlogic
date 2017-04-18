package Parser.Operators.Math;

import Parser.Operators.*;

public final class Addition extends BinaryOperation implements IMath {
    public Addition(IExpression left, IExpression right) {
        super(left, right, "+");
    }

    @Override
    public IExpression clone() {
        return new Addition(getLeft().clone(), getRight().clone());
    }
}
