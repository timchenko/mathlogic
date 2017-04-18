package Parser.Operators.Math;

import Parser.Operators.*;

public final class Multiplication extends BinaryOperation implements IMath {
    public Multiplication(IExpression left, IExpression right) {
        super(left, right, "*");
    }

    @Override
    public IExpression clone() {
        return new Multiplication(getLeft().clone(), getRight().clone());
    }
}
