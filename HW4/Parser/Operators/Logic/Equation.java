package Parser.Operators.Logic;

import Parser.Operators.*;

public final class Equation extends BinaryOperation implements ILogic {
    public Equation(IExpression left, IExpression right) {
        super(left, right, "=");
    }

    @Override
    public IExpression clone() {
        return new Equation(getLeft().clone(), getRight().clone());
    }
}
