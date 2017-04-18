package Parser.Operators.Logic;

import Parser.Operators.*;
import Parser.Operators.Math.Variable;

public final class Existence extends Quantifier {
    public Existence(Variable var, IExpression expr) {
        super(var, expr, "?");
    }

    @Override
    public IExpression clone() {
        return new Existence(variable, expression.clone());
    }
}

