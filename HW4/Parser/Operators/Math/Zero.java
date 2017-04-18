package Parser.Operators.Math;

import Parser.Operators.*;

public final class Zero implements IMath {
    public final String value = "0";

    public Zero() {}

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Zero;
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
        return new Zero();
    }
}
