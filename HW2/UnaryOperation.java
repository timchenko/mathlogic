/**
 * Created by Bogdan on 17.02.2017.
 */
abstract class UnaryOperation implements Expression {
    Expression exp;

    public UnaryOperation(Expression e) {
        exp = e;
    }

    public boolean equals(Expression e) {
        if(e == this) return true;

        if(e == null) return false;

        if(getClass() != e.getClass()) return false;
        else {
            UnaryOperation unOp = (UnaryOperation) e;
            return exp.equals(unOp.exp);
        }
    }

}
