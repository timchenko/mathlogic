package Parser.Operators.Logic;

import Parser.Operators.*;

public final class Negation extends UnaryOperation implements ILogic {
    public Negation(IExpression expr) {
        super(expr, "!");
    }

    @Override
    public IExpression clone() {
        return new Negation(getExpr().clone());
    }

    @Override
    public String toString() {
        return "(!" + getExpr().toString() + ")";
    }
}
