package Parser.Operators;

import java.util.ArrayList;

public abstract class ArityOperation implements IExpression {
    public String name;
    public int arity;
    public ArrayList<IExpression> arguments;

    public ArityOperation(String name, int arity, ArrayList<IExpression> arguments) {
        this.name = name;
        this.arity = arity;
        this.arguments = arguments;
    }

    public abstract IExpression clone();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("(");
        for (IExpression a : arguments) {
            sb.append(a.toString()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;

        if(obj == null) return false;

        if (obj instanceof ArityOperation) {
            ArityOperation ao = (ArityOperation) obj;
            if (name.equals(ao.name) && ao.arity == this.arity) {
                for (int i = 0; i < arity; i++) {
                    if (!ao.arguments.get(i).equals(arguments.get(i)))
                    {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = name.hashCode();
        for (int i = 0; i < arity; i++)
        {
            hash += arguments.get(i).hashCode() * (int)Math.pow(19, i + 1);
            hash %= 1_000_000_007;
        }
        return hash;
    }
}

