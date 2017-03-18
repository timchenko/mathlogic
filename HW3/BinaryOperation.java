import java.util.HashSet;

/**
 * Created by Bogdan on 17.02.2017.
 */
abstract class BinaryOperation implements Expression {
    public Expression left;
    public Expression right;

    public BinaryOperation(Expression l, Expression r) {
        left = l;
        right = r;
    }

    public boolean equals(Expression e) {
        if(e == this) return true;

        if(e == null) return false;

        if(getClass() != e.getClass()) return false;
        else {
            BinaryOperation binOp = (BinaryOperation)e;
            return (left.equals(binOp.left) && right.equals(binOp.right));
        }
    }

    public HashSet<String> getVariables() {
        HashSet<String> vars = left.getVariables();
        vars.addAll(right.getVariables());
        return vars;
    }
}
