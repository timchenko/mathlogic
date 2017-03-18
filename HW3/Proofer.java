import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Bogdan on 18.03.2017.
 */
public class Proofer {
    private static Expression exp;

    public static Proof getProof(Expression e) {
        exp = e;
        ArrayList<String> vars = new ArrayList<>(exp.getVariables());
        int size = vars.size();
        int amount = 1 << size;
        ArrayList<Proof> proofs = new ArrayList<>(amount);

        for (int vals = 0; vals < amount; vals++) {
            Valuation valuation = new Valuation(vars, vals);
            proofs.add(getBaseProof(valuation, exp).setHead(valuation.toAssum(), e));
        }

        for (int step = 0; step < size; step++) {
            ArrayList<Proof> newProofs = new ArrayList<>(amount / 2);
            for (int index = 0; index + 1 < amount; index += 2) {
                newProofs.add(mergeProofs(proofs.get(index), proofs.get(index + 1), vars.get(step)));
            }

            proofs = newProofs;
            amount /= 2;
        }

        return proofs.get(0);
    }

    private static Proof getBaseProof(Valuation val, Expression exp) { //TODO: gavnocode
        Proof ans = new Proof();

        if (exp.getOperation().equals("&")) {

            Conjunction conj = (Conjunction) exp;
            ans.add(getBaseProof(val, conj.left));
            ans.add(getBaseProof(val, conj.right));
            boolean lValue = conj.left.evaluate(val);
            boolean rValue = conj.right.evaluate(val);
            if (lValue && rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.T_T_AND, conj.left, conj.right));
            } else if (lValue && !rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.T_F_AND, conj.left, conj.right));
            } else if (!lValue && rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.F_T_AND, conj.left, conj.right));
            } else {
                ans.add(PartialProofs.getPartProof(PartialProofs.F_F_AND, conj.left, conj.right));
            }

        } else if (exp.getOperation().equals("|")) {

            Disjunction disj = (Disjunction) exp;
            ans.add(getBaseProof(val, disj.left));
            ans.add(getBaseProof(val, disj.right));
            boolean lValue = disj.left.evaluate(val);
            boolean rValue = disj.right.evaluate(val);
            if (lValue && rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.T_T_OR, disj.left, disj.right));
            } else if (lValue && !rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.T_F_OR, disj.left, disj.right));
            } else if (!lValue && rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.F_T_OR, disj.left, disj.right));
            } else {
                ans.add(PartialProofs.getPartProof(PartialProofs.F_F_OR, disj.left, disj.right));
            }

        } else if (exp.getOperation().equals("->")) {

            Implication impl = (Implication) exp;
            ans.add(getBaseProof(val, impl.left));
            ans.add(getBaseProof(val, impl.right));
            boolean lValue = impl.left.evaluate(val);
            boolean rValue = impl.right.evaluate(val);
            if (lValue && rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.T_T_IMPL, impl.left, impl.right));
            } else if (lValue && !rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.T_F_IMPL, impl.left, impl.right));
            } else if (!lValue && rValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.F_T_IMPL, impl.left, impl.right));
            } else {
                ans.add(PartialProofs.getPartProof(PartialProofs.F_F_IMPL, impl.left, impl.right));
            }

        } else if (exp.getOperation().equals("!")) {

            Negation neg = (Negation) exp;
            ans.add(getBaseProof(val, neg.exp));
            boolean eValue = neg.exp.evaluate(val);
            if (eValue) {
                ans.add(PartialProofs.getPartProof(PartialProofs.T_NOT, neg.exp, neg.exp));
            } else {
                ans.add(PartialProofs.getPartProof(PartialProofs.F_NOT, neg.exp, neg.exp));
            }

        } else {    //Variable

            Variable var = (Variable) exp;
            boolean varValue = var.evaluate(val);
            ans.add(varValue ? var : new Negation(var));

        }
        return ans;
    }

    private static Proof mergeProofs(Proof a, Proof b, String variable) {
        Proof deductA = a.deduction();
        Proof deductB = b.deduction();

        Proof ans = new Proof();
        ans.setHead(deductA.assumptions, a.beta);
        ans.add(deductA);
        ans.add(deductB);
        ans.add(PartialProofs.getPartProof(PartialProofs.endOfMerge, new Variable(variable), exp));
        return ans;
    }
}
