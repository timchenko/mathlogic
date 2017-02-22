/**
 * Created by Bogdan on 17.02.2017.
 */
public class Disjunction extends BinaryOperation {
    public Disjunction(Expression l, Expression r) {
        super(l, r);
    }

    public String print() {
        return "(" + left.print() + "|" + right.print() + ")";
    }
}
