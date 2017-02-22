/**
 * Created by Bogdan on 18.02.2017.
 */
public class Variable implements Expression {
    private String name;

    public Variable(String str) {
        name = str;
    }

    public String print() {
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

    //// TODO: 20.02.2017
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
