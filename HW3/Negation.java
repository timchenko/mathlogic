/**
 * Created by Bogdan on 17.02.2017.
 */
public class Negation extends UnaryOperation {
    public Negation(Expression e) {
        super(e);
    }

    public String toString() {
        return "!" + "(" + exp.toString() + ")";
    }

    public boolean evaluate(Valuation valuation) {
        return !exp.evaluate(valuation);
    }

    public String getOperation() {
        return "!";
    }
}
