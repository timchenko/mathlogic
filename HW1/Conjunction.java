/**
 * Created by Bogdan on 17.02.2017.
 */
public class Conjunction extends BinaryOperation {
    public Conjunction(Expression l, Expression r) {
        super(l, r);
    }

    public String print() {
        return "(" + left.print() + "&" + right.print() + ")";
    }
}
