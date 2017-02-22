/**
 * Created by Bogdan on 17.02.2017.
 */
public class Negation extends UnaryOperation {
    public Negation(Expression e) {
        super(e);
    }

    public String print() {
        return "!" + "(" + exp.print() + ")";
    }
}
