package Parser.Operators;

import java.util.ArrayList;

public abstract class UnaryOperation extends ArityOperation {

    public UnaryOperation(IExpression expression, String function)  {
        super(function, 1, new ArrayList<>());
        arguments.add(expression);
    }

    public void setExpr(IExpression value) {
        arguments.set(0, value);
    }

    public IExpression getExpr() {
        return arguments.get(0);
    }

    @Override
    public abstract String toString();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UnaryOperation) {
            UnaryOperation u = (UnaryOperation) obj;
            return this.getClass().equals(u.getClass()) && getExpr().equals(u.getExpr());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 2039 + getExpr().hashCode() * 127;
    }

}

