package Parser.Operators.Math;

import Parser.Operators.*;
import java.util.ArrayList;

public final class Function extends ArityOperation implements IMath {
    public Function(String name, int arity, ArrayList<IExpression> args) {
        super(name, arity, args);
    }

    @Override
    public String toString() {
        if (arity == 0) {
            return name;
        }
        return super.toString();
    }

    @Override
    public IExpression clone() {
        ArrayList<IExpression> copyArg = new ArrayList<>(arity);
        for (IExpression a : arguments) {
            copyArg.add(a.clone());
        }
        return new Function(name, arity, copyArg);
    }
}
