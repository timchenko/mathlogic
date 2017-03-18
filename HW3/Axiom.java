import java.util.HashMap;

/**
 * Created by Bogdan on 19.02.2017.
 */
public class Axiom {
    private Expression axiom;

    public Axiom(Expression exp) {
        axiom = exp;
    }

    public Axiom(String str) {
        this(ExpressionParser.parse(str));
    }

    public boolean isAxiom(Expression exp) {
        HashMap<Variable, Expression> replacements = new HashMap<>();
        return dfs(axiom, exp, replacements);
    }

    private static boolean dfs(Expression ax, Expression exp, HashMap<Variable, Expression> replacements) {

        if (ax.getClass() == Variable.class) {
            if (replacements.containsKey(ax)) {
                return replacements.get(ax).equals(exp);
            } else {
                replacements.put((Variable) ax, exp);
                return true;
            }
        }

        else if (ax instanceof BinaryOperation &&
                   ax.getClass() == exp.getClass()) {
            BinaryOperation axB = (BinaryOperation) ax;
            BinaryOperation expB = (BinaryOperation) exp;
            return dfs(axB.left, expB.left, replacements) && dfs(axB.right, expB.right, replacements);
        } else if (ax instanceof UnaryOperation &&
                   ax.getClass() == exp.getClass()) {
            UnaryOperation axB = (UnaryOperation) ax;
            UnaryOperation expB = (UnaryOperation) exp;
            return dfs(axB.exp, expB.exp, replacements);
        } else return false;
    }
}
