/**
 * Created by Bogdan on 17.02.2017.
 */
public class Implication extends BinaryOperation {
    public Implication(Expression l, Expression r) {
        super(l, r);
    }

    public String toString() {
        return "(" + left.toString() + "->" + right.toString() + ")";
    }

    public boolean evaluate(Valuation valuation) {
        return !left.evaluate(valuation) || right.evaluate(valuation);
    }

    public String getOperation() {
        return "->";
    }
}
