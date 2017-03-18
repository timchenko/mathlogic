import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Created by Bogdan on 18.03.2017.
 */
public class Valuation {
    public HashMap<String, Boolean> values;

    public Valuation(ArrayList<String> vars, int vals) {
        values = new HashMap<>(vars.size());
        for (int index = 0; index < vars.size(); index++) {
            values.put(vars.get(index), (vals >> index) % 2 == 1);
        }
    }

    public ArrayList<Expression> toAssum() {
        return new ArrayList<>(values.keySet().stream().map(var -> values.get(var) ? new Variable(var) : new Negation(new Variable(var))).collect(Collectors.toList()));
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder(values.size() * 5);
        for (String var: values.keySet()) {
            buffer.append(var);
            buffer.append("=");
            buffer.append(values.get(var) ? "И" : "Л");
            buffer.append(", ");
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        return buffer.toString();
    }
}
