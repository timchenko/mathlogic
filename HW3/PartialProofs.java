import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Bogdan on 18.03.2017.
 */
public class PartialProofs {
    public static ArrayList<Expression> replace(ArrayList<Expression> base, HashMap<Variable, Expression> replacements) {
        return new ArrayList<>(base.stream().map(exp -> replace(exp, replacements)).collect(Collectors.toList()));
        //TODO: check lambda
    }

    public static Expression replace(Expression base, HashMap<Variable, Expression> replacements) {
        if (base.getClass() == Variable.class) {
            if (replacements.containsKey((Variable)base)) {
                return replacements.get((Variable)base);
            } else return base;
        } else if (base.getClass() == Conjunction.class) {
            return new Conjunction(replace(((Conjunction)base).left, replacements), replace(((Conjunction)base).right, replacements));
        } else if (base.getClass() == Disjunction.class) {
            return new Disjunction(replace(((Disjunction)base).left, replacements), replace(((Disjunction)base).right, replacements));
        } else if (base.getClass() == Implication.class) {
            return new Implication(replace(((Implication)base).left, replacements), replace(((Implication)base).right, replacements));
        } else if (base.getClass() == Negation.class) {
            return new Negation(replace(((Negation)base).exp, replacements));
        } else {
            return null;
        }
    }

    public static ArrayList<Expression> getPartProof(String[] part, Expression a, Expression b) {
        HashMap<Variable, Expression> repl = new HashMap<>(2);
        repl.put(new Variable("A"), a);
        repl.put(new Variable("B"), b);
        return new ArrayList<>(Arrays.stream(part).map(str -> replace(ExpressionParser.parse(str), repl)).collect(Collectors.toList()));
        //TODO: check lambda
    }

    public static final String[] endOfMerge = {
            "A->A|!A",
            "(((A->(A|!A))->((A->!(A|!A))->!A))->(!(A|!A)->((A->(A|!A))->((A->!(A|!A))->!A))))",
            "((A->(A|!A))->((A->!(A|!A))->!A))",
            "(!(A|!A)->((A->(A|!A))->((A->!(A|!A))->!A)))",
            "((A->(A|!A))->(!(A|!A)->(A->(A|!A))))",
            "(A->(A|!A))",
            "(!(A|!A)->(A->(A|!A)))",
            "((!(A|!A)->(A->(A|!A)))->((!(A|!A)->((A->(A|!A))->((A->!(A|!A))->!A)))->(!(A|!A)->((A->!(A|!A))->!A))))",
            "((!(A|!A)->((A->(A|!A))->((A->!(A|!A))->!A)))->(!(A|!A)->((A->!(A|!A))->!A)))",
            "(!(A|!A)->((A->!(A|!A))->!A))",
            "((!(A|!A)->(A->!(A|!A)))->(!(A|!A)->(!(A|!A)->(A->!(A|!A)))))",
            "(!(A|!A)->(A->!(A|!A)))",
            "(!(A|!A)->(!(A|!A)->(A->!(A|!A))))",
            "(!(A|!A)->(!(A|!A)->!(A|!A)))",
            "((!(A|!A)->(!(A|!A)->!(A|!A)))->((!(A|!A)->((!(A|!A)->!(A|!A))->!(A|!A)))->(!(A|!A)->!(A|!A))))",
            "((!(A|!A)->((!(A|!A)->!(A|!A))->!(A|!A)))->(!(A|!A)->!(A|!A)))",
            "(!(A|!A)->((!(A|!A)->!(A|!A))->!(A|!A)))",
            "(!(A|!A)->!(A|!A))",
            "((!(A|!A)->!(A|!A))->((!(A|!A)->(!(A|!A)->(A->!(A|!A))))->(!(A|!A)->(A->!(A|!A)))))",
            "((!(A|!A)->(!(A|!A)->(A->!(A|!A))))->(!(A|!A)->(A->!(A|!A))))",
            "(!(A|!A)->(A->!(A|!A)))",
            "((!(A|!A)->(A->!(A|!A)))->((!(A|!A)->((A->!(A|!A))->!A))->(!(A|!A)->!A)))",
            "((!(A|!A)->((A->!(A|!A))->!A))->(!(A|!A)->!A))",
            "(!(A|!A)->!A)",
            "!A->A|!A",
            "(((!A->(A|!A))->((!A->!(A|!A))->!!A))->(!(A|!A)->((!A->(A|!A))->((!A->!(A|!A))->!!A))))",
            "((!A->(A|!A))->((!A->!(A|!A))->!!A))",
            "(!(A|!A)->((!A->(A|!A))->((!A->!(A|!A))->!!A)))",
            "((!A->(A|!A))->(!(A|!A)->(!A->(A|!A))))",
            "(!A->(A|!A))",
            "(!(A|!A)->(!A->(A|!A)))",
            "((!(A|!A)->(!A->(A|!A)))->((!(A|!A)->((!A->(A|!A))->((!A->!(A|!A))->!!A)))->(!(A|!A)->((!A->!(A|!A))->!!A))))",
            "((!(A|!A)->((!A->(A|!A))->((!A->!(A|!A))->!!A)))->(!(A|!A)->((!A->!(A|!A))->!!A)))",
            "(!(A|!A)->((!A->!(A|!A))->!!A))",
            "((!(A|!A)->(!A->!(A|!A)))->(!(A|!A)->(!(A|!A)->(!A->!(A|!A)))))",
            "(!(A|!A)->(!A->!(A|!A)))",
            "(!(A|!A)->(!(A|!A)->(!A->!(A|!A))))",
            "(!(A|!A)->(!(A|!A)->!(A|!A)))",
            "((!(A|!A)->(!(A|!A)->!(A|!A)))->((!(A|!A)->((!(A|!A)->!(A|!A))->!(A|!A)))->(!(A|!A)->!(A|!A))))",
            "((!(A|!A)->((!(A|!A)->!(A|!A))->!(A|!A)))->(!(A|!A)->!(A|!A)))",
            "(!(A|!A)->((!(A|!A)->!(A|!A))->!(A|!A)))",
            "(!(A|!A)->!(A|!A))",
            "((!(A|!A)->!(A|!A))->((!(A|!A)->(!(A|!A)->(!A->!(A|!A))))->(!(A|!A)->(!A->!(A|!A)))))",
            "((!(A|!A)->(!(A|!A)->(!A->!(A|!A))))->(!(A|!A)->(!A->!(A|!A))))",
            "(!(A|!A)->(!A->!(A|!A)))",
            "((!(A|!A)->(!A->!(A|!A)))->((!(A|!A)->((!A->!(A|!A))->!!A))->(!(A|!A)->!!A)))",
            "((!(A|!A)->((!A->!(A|!A))->!!A))->(!(A|!A)->!!A))",
            "(!(A|!A)->!!A)",
            "(!(A|!A)->!A)->(!(A|!A)->!!A)->!!(A|!A)",
            "(!(A|!A)->!!A)->!!(A|!A)",
            "!!(A|!A)",
            "!!(A|!A)->(A|!A)",
            "A|!A",
            "(A->B)->(!A->B)->(A|!A->B)",
            "(!A->B)->(A|!A->B)",
            "A|!A->B",
            "B"
    };

    public static final String[] T_T_AND = {
            "A->B->A&B",
            "A",
            "B->A&B",
            "B",
            "A&B"
    };

    public static final String[] T_F_AND = {
            "((A&B)->B)->((A&B)->!B)->!(A&B)",
            "(A&B)->B ",
            "(A&B->!B)->!(A&B)",
            "!B->A&B->!B",
            "!B",
            "A&B->!B",
            "!(A&B)"
    };

    public static final String[] F_T_AND = {
            "((A&B)->A)->((A&B)->!A)->!(A&B)",
            "(A&B)->A ",
            "(A&B->!A)->!(A&B)",
            "!A->A&B->!A",
            "!A",
            "A&B->!A",
            "!(A&B)"
    };

    public static final String[] F_F_AND = {
            "((A&B)->B)->((A&B)->!B)->!(A&B)",
            "(A&B)->B ",
            "(A&B->!B)->!(A&B)",
            "!B->A&B->!B",
            "!B",
            "A&B->!B",
            "!(A&B)"
    };

    public static final String[] T_T_OR = {
            "A->A|B",
            "A",
            "A|B"
    };

    public static final String[] T_F_OR = {
            "A->A|B",
            "A",
            "A|B"
    };

    public static final String[] F_T_OR = {
            "B->A|B",
            "B",
            "A|B"
    };

    public static final String[] F_F_OR = {
            "(A|B->(A))->(A|B->!(A))->!(A|B)",
            "(A->A)->(B->A)->(A|B->A)",
            "A->A->A",
            "(A->A->A)->(A->(A->A)->A)->(A->A)",
            "(A->(A->A)->A)->(A->A)",
            "A->(A->A)->A",
            "A->A",
            "(B->A)->(A|B->A)",
            "(B->!A->B)->B->(B->!A->B)",
            "(B->!A->B)",
            "B->(B->!A->B)",
            "B->B->B",
            "(B->B->B)->(B->(B->B)->B)->(B->B)",
            "(B->(B->B)->B)->(B->B)",
            "B->(B->B)->B",
            "B->B",
            "(B->B)->(B->B->(!A->B))->(B->(!A->B))",
            "(B->B->!A->B)->(B->!A->B)",
            "B->!A->B",
            "(!B->!A->!B)->B->(!B->!A->!B)",
            "!B->!A->!B",
            "B->!B->!A->!B",
            "!B->B->!B",
            "!B",
            "B->!B",
            "(B->!B)->(B->!B->!A->!B)->(B->!A->!B)",
            "(B->!B->!A->!B)->(B->!A->!B)",
            "B->!A->!B",
            "((!A->B)->(!A->!B)->!!A)->B->((!A->B)->(!A->!B)->!!A)",
            "(!A->B)->(!A->!B)->!!A",
            "B->((!A->B)->(!A->!B)->!!A)",
            "(B->(!A->B))->(B->(!A->B)->((!A->!B)->!!A))->(B->((!A->!B)->!!A))",
            "((B->((!A->B)->((!A->!B)->!!A)))->(B->((!A->!B)->!!A)))",
            "B->((!A->!B)->!!A)",
            "(B->(!A->!B))->(B->(!A->!B)->!!A)->(B->!!A)",
            "((B->((!A->!B)->!!A))->(B->!!A))",
            "B->!!A",
            "(!!A->A)->B->(!!A->A)",
            "!!A->A",
            "B->!!A->A",
            "(B->!!A)->(B->!!A->A)->(B->A)",
            "((B->!!A->A)->(B->A))",
            "B->A",
            "A|B->A",
            "(A|B->!A)->!(A|B)",
            "!A->A|B->!A",
            "A|B->!A",
            "!(A|B)"
    };

    public static final String[] T_T_IMPL = {
            "B->A->B",
            "B",
            "A->B"
    };

    public static final String[] T_F_IMPL = {
            "((A->B)->B)->((A->B)->!B)->!(A->B)",
            "((A->B)->A)->((A->B)->(A->B))->((A->B)->B)",
            "A->(A->B)->A",
            "A",
            "(A->B)->A",
            "((A->B)->(A->B))->((A->B)->B)",
            "((A->B)->(A->B)->(A->B))",
            "((A->B)->(A->B)->(A->B))->((A->B)->((A->B)->(A->B))->(A->B))->((A->B)->(A->B))",
            "((A->B)->((A->B)->(A->B))->(A->B))->((A->B)->(A->B))",
            "((A->B)->((A->B)->(A->B))->(A->B))",
            "(A->B)->(A->B)",
            "(A->B)->B",
            "!B->(A->B)->!B",
            "!B",
            "(A->B)->!B",
            "((A->B)->!B)->!(A->B)",
            "!(A->B)"
    };

    public static final String[] F_T_IMPL = {
            "B->A->B",
            "B",
            "A->B"
    };

    public static final String[] F_F_IMPL = {
            "(A->!B->A)->A->(A->!B->A)",
            "(A->!B->A)",
            "A->(A->!B->A)",
            "A->A->A",
            "(A->A->A)->(A->(A->A)->A)->(A->A)",
            "(A->(A->A)->A)->(A->A)",
            "A->(A->A)->A",
            "A->A",
            "(A->A)->(A->A->(!B->A))->(A->(!B->A))",
            "(A->A->!B->A)->(A->!B->A)",
            "A->!B->A",
            "(!A->!B->!A)->A->(!A->!B->!A)",
            "!A->!B->!A",
            "A->!A->!B->!A",
            "!A->A->!A",
            "!A",
            "A->!A",
            "(A->!A)->(A->!A->!B->!A)->(A->!B->!A)",
            "(A->!A->!B->!A)->(A->!B->!A)",
            "A->!B->!A",
            "((!B->A)->(!B->!A)->!!B)->A->((!B->A)->(!B->!A)->!!B)",
            "(!B->A)->(!B->!A)->!!B",
            "A->((!B->A)->(!B->!A)->!!B)",
            "(A->(!B->A))->(A->(!B->A)->((!B->!A)->!!B))->(A->((!B->!A)->!!B))",
            "((A->((!B->A)->((!B->!A)->!!B)))->(A->((!B->!A)->!!B)))",
            "A->((!B->!A)->!!B)",
            "(A->(!B->!A))->(A->(!B->!A)->!!B)->(A->!!B)",
            "((A->((!B->!A)->!!B))->(A->!!B))",
            "A->!!B",
            "(!!B->B)->A->(!!B->B)",
            "!!B->B",
            "A->!!B->B",
            "(A->!!B)->(A->!!B->B)->(A->B)",
            "((A->!!B->B)->(A->B))",
            "A->B"
    };

    public static final String[] T_NOT = {
            "A",
            "!A->(!A->!A)->!A",
            "!A->!A->!A",
            "(!A->!A->!A)->(!A->(!A->!A)->!A)->(!A->!A)",
            "(!A->(!A->!A)->!A)->(!A->!A)",
            "!A->!A",
            "A->!A->A",
            "!A->A",
            "(!A->A)->(!A->!A)->!!A",
            "(!A->!A)->!!A",
            "!!A"
    };

    public static final String[] F_NOT = {
            "!A"
    };

}
