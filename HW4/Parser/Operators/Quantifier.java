package Parser.Operators;

import Parser.Operators.Math.Variable;

public class Quantifier implements ILogic {
    public Variable variable;
    public IExpression expression;
    public String function;

    public Quantifier(Variable c, IExpression expr, String function) {
        this.function = function;
        this.variable = c;
        this.expression = expr;
    }

    public IExpression clone() {
        return new Quantifier((Variable)variable.clone(), expression.clone(), function);
    }
    
    @Override
    public String toString() {
        return function + variable.toString() + expression.toString();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Quantifier) {
            Quantifier q = (Quantifier) obj;
            return q.function.equals(function) && q.variable.equals(variable) && q.expression.equals(expression);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return function.hashCode() * 2039 + expression.hashCode() * 181 + variable.hashCode() * 71;
    }
}
