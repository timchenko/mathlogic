import java.util.HashSet;

/**
 * Created by Bogdan on 18.02.2017.
 */
public class Variable implements Expression {
    private String name;

    public Variable(String str) {
        name = str;
    }

    public String toString() {
        return name;
    }

    public boolean equals(Expression e) {
        if (e == this) return true;

        if (e == null) return false;

        if (getClass() != e.getClass()) return false;
        else {
            Variable var = (Variable) e;
            return name.equals(var.name);
        }
    }

    public HashSet<String> getVariables() {
        HashSet<String> ans = new HashSet<>();
        ans.add(name);
        return ans;
    }

    public boolean evaluate(Valuation valuation) {
        return valuation.values.get(name);
    }

    public String getOperation() {
        return "";
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == Variable.class) {
            return equals((Expression)o);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
