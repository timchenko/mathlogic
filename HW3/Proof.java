import java.util.ArrayList;

/**
 * Created by Bogdan on 18.02.2017.
 */
public class Proof {
    public ArrayList<Expression> assumptions;
    public Expression alpha;
    public Expression beta;
    public ArrayList<Expression> inference;

    private static ArrayList<Axiom> axioms;

    static {
        axioms = new ArrayList<>();
        axioms.add(new Axiom("a->b->a"));
        axioms.add(new Axiom("(a->b)->(a->b->c)->(a->c)"));
        axioms.add(new Axiom("a->b->a&b"));
        axioms.add(new Axiom("a&b->a"));
        axioms.add(new Axiom("a&b->b"));
        axioms.add(new Axiom("a->a|b"));
        axioms.add(new Axiom("b->a|b"));
        axioms.add(new Axiom("(a->c)->(b->c)->(a|b->c)"));
        axioms.add(new Axiom("(a->b)->(a->!b)->!a"));
        axioms.add(new Axiom("!!a->a"));
    }

    public Proof(String head) {
        assumptions = new ArrayList<>();
        inference = new ArrayList<>();
        int left = 0;
        for (int i = 0; i < head.length(); ++i) {
            if (head.charAt(i) == ',') {
                assumptions.add(ExpressionParser.parse(head.substring(left, i)));
                left = i+1;
            }
            if (i >= 1 && head.substring(i-1, i+1).equals("|-")) {
                alpha = ExpressionParser.parse(head.substring(left, i-1));
                beta = ExpressionParser.parse(head.substring(i+1, head.length()));
                break;
            }
        }
    }

    private Proof(ArrayList<Expression> assum, Expression statement) {
        assumptions = assum;
        beta = statement;
        inference = new ArrayList<>();
    }

    public Proof() {
        inference = new ArrayList<>();
    }

    public void add(String str) {
        inference.add(ExpressionParser.parse(str));
    }

    public void add(Expression exp) {
        inference.add(exp);
    }

    public void add(Proof proof) {
        inference.addAll(proof.inference);
    }

    public void add(ArrayList<Expression> expressionList) {
        this.inference.addAll(expressionList);
    }

    public String getHead() {
        String ans = "";
        for (int i = 0; i < assumptions.size() - 1; i++) {
            ans = ans + assumptions.get(i).toString() + ", ";
        }
        if (assumptions.size() > 0)
            ans = ans + assumptions.get(assumptions.size() - 1).toString();
        ans = ans + "|-" + beta.toString();
        return ans;
    }

    public Proof setHead(ArrayList<Expression> assum, Expression exp) {
        assumptions = assum;
        beta = exp;
        return this;
    }

    public String getExp(int index) {
        return inference.get(index).toString();
    }

    public int size() {
        return inference.size();
    }

    public Proof deduction() {
        if (alpha == null) {
            alpha = assumptions.remove(0);
        }
        Proof ans = new Proof(new ArrayList<>(assumptions), new Implication(alpha, beta));
        for (int i = 0; i < inference.size(); i++) {
            boolean next = false;

            Expression currentExp = inference.get(i);
            for (int j = 0; j < assumptions.size(); j++) {
                if (currentExp.equals(assumptions.get(j))) {
                    ans.add(new Implication(currentExp, new Implication(alpha, currentExp)));
                    ans.add(currentExp);
                    ans.add(new Implication(alpha, currentExp));
                    next = true;
                    break;
                }
            }

            if (next) continue;

            for (int j = 0; j < axioms.size(); j++) {
                if (axioms.get(j).isAxiom(currentExp)) {
                    ans.add(new Implication(currentExp, new Implication(alpha, currentExp)));
                    ans.add(currentExp);
                    ans.add(new Implication(alpha, currentExp));
                    next = true;
                    break;
                }
            }

            if (next) continue;

            if (currentExp.equals(alpha)) {
                ans.add(new Implication(alpha, new Implication(alpha, alpha)));
                ans.add(new Implication(alpha, new Implication(new Implication(alpha, alpha), alpha)));
                ans.add(new Implication(
                        new Implication(alpha, new Implication(alpha, alpha)),
                        new Implication(
                                new Implication(alpha, new Implication(new Implication(alpha, alpha), alpha)),
                                new Implication(alpha, alpha))));
                ans.add(new Implication(
                        new Implication(alpha, new Implication(new Implication(alpha, alpha), alpha)),
                        new Implication(alpha, alpha)));
                ans.add(new Implication(alpha, alpha));
                continue;
            }

            for (int j = i - 1; j >= 0; j--) {
                if (inference.get(j).getClass() == Implication.class &&
                        ((Implication)inference.get(j)).right.equals(currentExp)) {
                    Expression left = ((Implication)inference.get(j)).left;

                    for (int k = i - 1; k >=0; k--) {
                        if (inference.get(k).equals(left)) {
                            ans.add(new Implication(
                                    new Implication(alpha, inference.get(k)),
                                    new Implication(
                                            new Implication(alpha, inference.get(j)),
                                            new Implication(alpha, inference.get(i)))));
                            ans.add(new Implication(
                                    new Implication(alpha, inference.get(j)),
                                    new Implication(alpha, inference.get(i))));
                            ans.add(new Implication(alpha, inference.get(i)));
                            next = true;
                            break;
                        }
                    }

                    if (next) break;

                }
            }
        }

        return ans;
    }
}
