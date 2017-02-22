import java.util.ArrayList;

/**
 * Created by Bogdan on 18.02.2017.
 */
public class Proof {
    private ArrayList<Expression> assumptions;
    private Expression statement;
    private ArrayList<Expression> inference;
    private String head;
    private ArrayList<String> body;

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
        body = new ArrayList<>();
        this.head = head;
        int left = 0;
        for (int i = 0; i < head.length(); ++i) {
            if (head.charAt(i) == ',') {
                assumptions.add(ExpressionParser.parse(head.substring(left, i)));
                left = i+1;
            }
            if (i >= 1 && head.substring(i-1, i+1).equals("|-")) {
                Expression toAdd = ExpressionParser.parse(head.substring(left, i-1));
                if (toAdd != null) assumptions.add(toAdd);
                statement = ExpressionParser.parse(head.substring(i+1, head.length()));
                break;
            }
        }
    }

    public void addExpression(String str) {
        body.add(str);
        inference.add(ExpressionParser.parse(str));
    }

    public String getHead() {
        return head;
    }

    public String getExp(int index) {
        assert (index >= 1);
        return body.get(index - 1);
    }

    ArrayList<Annotation> annotate() {
        ArrayList<Annotation> ans = new ArrayList<>(body.size());
        for (int i = 0; i < body.size(); i++) {

            Expression currentExp = inference.get(i);
            for (int j = 0; j < assumptions.size(); j++) {
                if (currentExp.equals(assumptions.get(j))) {
                    ans.add(new Annotation(Annotation.ASSUM, j+1));
                    break;
                }
            }

            if (ans.size() > i) continue;

            for (int j = i - 1; j >= 0; j--) {
                if (inference.get(j).getClass() == Implication.class &&
                        ((Implication)inference.get(j)).right.equals(currentExp)) {
                    Expression left = ((Implication)inference.get(j)).left;

                    for (int k = i - 1; k >=0; k--) {
                        if (inference.get(k).equals(left)) {
                            ans.add(new Annotation(Annotation.MODUS, k+1, j+1));
                            break;
                        }
                    }

                    if (ans.size() > i) break;

                }
            }

            if (ans.size() > i) continue;

            for (int j = 0; j < axioms.size(); j++) {
                if (axioms.get(j).isAxiom(currentExp)) {
                    ans.add(new Annotation(Annotation.AXIOM, j+1));
                    break;
                }
            }

            if (ans.size() == i) ans.add(new Annotation(Annotation.WRONG));
        }

        return ans;
    }
}
