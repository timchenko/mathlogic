package Parser.Operators.Logic;

import Parser.Operators.*;
import Parser.Operators.Math.Variable;

public final class Universal extends Quantifier {
    public Universal(Variable var, IExpression expr) {
        super(var, expr, "@");
    }

    @Override
    public IExpression clone() {
        return new Universal(variable, expression.clone());
    }
}

