import Parser.*;
import Parser.Operators.*;
import Parser.Operators.Logic.*;

import java.util.HashMap;

public class AxiomScheme {
    public final IExpression expression;

    public AxiomScheme(String expr) {
        expression = new Parser().parse(expr);
    }

    public AxiomScheme(IExpression expr) {
        this.expression = expr;
    }

    public boolean isMatch(IExpression expr) {
        return isMatch(expression, expr, new HashMap<>());
    }

    private boolean isMatch(IExpression axiom, IExpression expr, HashMap<Predication, IExpression> map) {
        if (expr instanceof IMath) {
            return false;
        }

        if (axiom instanceof Predication) {
            Predication p = (Predication) axiom;
            if (map.containsKey(p)) {
                return map.get(p).equals(expr);
            } else {
                map.put(p, expr);
                return true;
            }

        } else if (axiom instanceof ArityOperation) {
            ArityOperation ao = (ArityOperation) axiom;
            if (expr instanceof ArityOperation) {
                ArityOperation e = (ArityOperation) expr;
                if (e.arity == ao.arity && e.name.equals(ao.name)) {
                    for (int i = 0; i < e.arity; i++) {
                        if (!isMatch(ao.arguments.get(i), e.arguments.get(i), map)) {
                            return false;
                        }
                    }
                } else return false;
            } else return false;
            return true;

        } else if (axiom instanceof Quantifier) {
            Quantifier q = (Quantifier) axiom;
            if (expr instanceof Quantifier) {
                Quantifier eq = (Quantifier) expr;
                return q.function.equals(eq.function) && isMatch(q.expression, eq.expression, map);
            } else return false;

        } else return false;
    }
}
