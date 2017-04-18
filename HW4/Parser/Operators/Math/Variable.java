package Parser.Operators.Math;

import Parser.Operators.*;

public final class Variable implements IMath {
    public String value;

    public Variable(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Variable) {
            Variable var = (Variable) obj;
            return var.value.equals(value);
        }
        if (obj instanceof String) {
            return value.equals(obj);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }

    public IExpression clone() {
        return new Variable(value);
    }
}