package Parser.Operators.Math;

import Parser.Operators.*;

public final class Increment extends UnaryOperation implements IMath {
    public Increment(IExpression expression) {
        super(expression, "'");
    }

    @Override
    public IExpression clone() {
        return new Increment(getExpr().clone());
    }

    @Override
    public String toString() {
        return getExpr().toString() + "'";
    }
}
