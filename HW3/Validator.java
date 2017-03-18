import java.util.ArrayList;

/**
 * Created by Bogdan on 18.03.2017.
 */
public class Validator {
    public static Valuation validate(Expression exp) {
        ArrayList<String> vars = new ArrayList<>(exp.getVariables());
        int amount = 1 << vars.size();

        for (int vals = 0; vals < amount; vals++) {
            Valuation value = new Valuation(vars, vals);
            boolean isTrue = exp.evaluate(value);
            if (!isTrue) return value;
        }

        return null;
    }
}
