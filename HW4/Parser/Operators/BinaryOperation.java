package Parser.Operators;

import java.util.ArrayList;

public abstract class BinaryOperation extends ArityOperation {
    
    public BinaryOperation(IExpression left, IExpression right, String function) {
        super(function, 2, new ArrayList<>());
        arguments.add(left);
        arguments.add(right);
    }

    public void setLeft(IExpression value) {
        arguments.set(0, value);
    }

    public void setRight(IExpression value) {
        arguments.set(1, value);
    }

    public IExpression getLeft() {
        return arguments.get(0);
    }

    public IExpression getRight() {
        return arguments.get(1);
    }

    @Override
    public String toString() {
        return "(" + getLeft().toString() + name + getRight().toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BinaryOperation) {
            BinaryOperation b = (BinaryOperation) obj;
            return this.getClass().equals(b.getClass()) && getLeft().equals(b.getLeft()) && getRight().equals(b.getRight());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return name.hashCode() * 2039 + getRight().hashCode() * 181 + getLeft().hashCode() * 71;
    }

}

