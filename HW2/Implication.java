/**
 * Created by Bogdan on 17.02.2017.
 */
public class Implication extends BinaryOperation {
    public Implication(Expression l, Expression r) {
        super(l, r);
    }

    public String print() {
        return "(" + left.print() + "->" + right.print() + ")";
    }
}
