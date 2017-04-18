import Parser.*;
import Parser.Operators.*;
import Parser.Operators.Math.*;
import Parser.Operators.Logic.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    ArrayList<AxiomScheme> axiomSchemes = new ArrayList<>();
    ArrayList<IExpression> axioms = new ArrayList<>();
    ArrayList<IExpression> assumptions = new ArrayList<>();
    ArrayList<IExpression> proof = new ArrayList<>();
    ArrayList<IExpression> newProof = new ArrayList<>();
    ArrayList<IExpression> selfCons = new ArrayList<>();

    AxiomScheme inductionAxiom;
    AxiomScheme existenceAxiom;
    AxiomScheme universalAxiom;

    public Main() {
        Parser p = new Parser();

        inductionAxiom = new AxiomScheme("F(0)&@x(F(x)->F(x'))->F(x)");
        existenceAxiom = new AxiomScheme("F(o)->?xF(x)");
        universalAxiom = new AxiomScheme("@xF(x)->F(o)");

        axiomSchemes = new ArrayList<>();
        axiomSchemes.add(new AxiomScheme("A->B->A"));
        axiomSchemes.add(new AxiomScheme("(A->B)->(A->B->C)->(A->C)"));
        axiomSchemes.add(new AxiomScheme("A->B->A&B"));
        axiomSchemes.add(new AxiomScheme("A&B->A"));
        axiomSchemes.add(new AxiomScheme("A&B->B"));
        axiomSchemes.add(new AxiomScheme("A->A|B"));
        axiomSchemes.add(new AxiomScheme("B->A|B"));
        axiomSchemes.add(new AxiomScheme("(A->B)->(C->B)->(A|C->B)"));
        axiomSchemes.add(new AxiomScheme("(A->B)->(A->!B)->!A"));
        axiomSchemes.add(new AxiomScheme("!!A->A"));

        axioms = new ArrayList<>();
        axioms.add(p.parse("a=b->a'=b'"));
        axioms.add(p.parse("a=b->a=c->b=c"));
        axioms.add(p.parse("a'=b'->a=b"));
        axioms.add(p.parse("!a'=0"));
        axioms.add(p.parse("a+b'=(a+b)'"));
        axioms.add(p.parse("a+0=a"));
        axioms.add(p.parse("a*0=a"));
        axioms.add(p.parse("a*b'=a*b+a"));

        selfCons = new ArrayList<>();
        selfCons.add(p.parse("(A->A->A)"));
        selfCons.add(p.parse("(A->A->A)->(A->(A->A)->A)->(A->A)"));
        selfCons.add(p.parse("(A->(A->A)->A)->(A->A)"));
        selfCons.add(p.parse("(A->(A->A)->A)"));
        selfCons.add(p.parse("A->A"));
    }

    public static void main(String[] args) {
        if (args.length < 2) System.out.println("Wrong input format: required Main <inputFile> <outputFile>");
        Main s = new Main();
        s.Run(args[0], args[1]);
    }

    private Iterable<String> CommaSplit(String s) {
        ArrayList<String> rv = new ArrayList<>();
        int balans = 0;
        int prev = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                balans++;
            }
            if (s.charAt(i) == ')') {
                balans--;
            }
            if (balans == 0 && s.charAt(i) == ',') {
                rv.add(s.substring(prev, i - prev));
                prev = i + 1;
            }
        }
        if (s.length() != 0) {
            rv.add(s.substring(prev, s.length() - prev));
        }
        return rv;
    }

    private IExpression SubstituteVariableToExpr(IExpression baseExpression, Variable x, IExpression y) throws Exception {
        if (y == null) return baseExpression.clone();
        IExpression rv = baseExpression.clone();
        HashMap<Variable, Integer> closing = new HashMap<>();
        rv = _substituteVariableToExpr(rv, (Variable) x.clone(), y, closing);
        return rv;
    }

    boolean cantSet (IExpression expr, Variable x, HashMap<Variable, Integer> closing)
    {
        boolean has = false;
        if (expr instanceof ArityOperation) {
            ArityOperation yao = (ArityOperation) expr;
            for (IExpression arg : yao.arguments) {
                has |= cantSet(arg, x, closing);
            }
        } else if (expr instanceof Variable){
            Variable _v = (Variable) expr;
            return closing.containsKey(_v) && closing.get(_v) > 0 && _v.value != x.value;
        }
        return has;
    }

    private IExpression _substituteVariableToExpr(IExpression baseExpression, Variable x, IExpression y, HashMap<Variable, Integer> closing) throws Exception{
        if (y.equals(x)) return baseExpression;

        if (baseExpression instanceof ArityOperation) {
            ArityOperation ao = (ArityOperation) baseExpression;
            for (int i = 0; i < ao.arity; i++) {
                if (ao.arguments.get(i) instanceof Variable)
                {
                    Variable v = (Variable) ao.arguments.get(i);
                    if (v.equals(x) && (!closing.containsKey(v) || closing.get(v) == 0)) {
                        if (cantSet(y, x, closing)) {
                            throw new Exception();
                        }
                        ao.arguments.set(i, y);
                    }
                } else if (!(ao.arguments.get(i) instanceof Variable)) {
                    ao.arguments.set(i, _substituteVariableToExpr(ao.arguments.get(i), x, y, closing));
                }
            }
        } else if (baseExpression instanceof Quantifier) {
            Quantifier q = (Quantifier) baseExpression;
            if (!closing.containsKey(q.variable)) {
                closing.put(q.variable, 0);
            }
            closing.replace(q.variable, closing.get(q.variable) + 1);
            q.expression = _substituteVariableToExpr(q.expression, x, y, closing);
            closing.replace(q.variable, closing.get(q.variable) - 1);
        }

        return baseExpression;
    }

    private IExpression SubstituteExprsToExpr(IExpression baseExpression, HashMap<String, IExpression> map) {
        IExpression rv = baseExpression.clone();
        rv = _substituteExprsToExpr(rv, map);
        return rv;
    }

    private IExpression _substituteExprsToExpr(IExpression baseExpression, HashMap<String, IExpression> map) {

        if (baseExpression instanceof Predication) {
           return map.get(((Predication) baseExpression).name);
        } else if (baseExpression instanceof Variable) {
            return map.get(((Variable) baseExpression).value);
        } else if (baseExpression instanceof ArityOperation) {
            ArityOperation ao = (ArityOperation) baseExpression;
            for (int i = 0; i < ao.arity; i++) {
                IExpression arg = ao.arguments.get(i);
                ao.arguments.set(i, _substituteExprsToExpr(arg, map));
            }
        } else if (baseExpression instanceof Quantifier) {
            Quantifier q = (Quantifier) baseExpression;
            q.variable = (Variable) map.get(q.variable.value);
            q.expression = _substituteExprsToExpr(q.expression, map);
        }
        return baseExpression;
    }

    private ArrayList<IExpression> BaseDeduct(IExpression expr, IExpression alpha) {
        HashMap<String, IExpression> hashMap = new HashMap<>();
        hashMap.put("A", expr);
        hashMap.put("B", alpha);
        IExpression t = SubstituteExprsToExpr(axiomSchemes.get(0).expression, hashMap);

        ArrayList<IExpression> ans = new ArrayList<>();
        ans.add(t);
        ans.add(expr);
        ans.add(new Implication(alpha, expr));
        return ans;
    }

    private IExpression GetMismatch(IExpression baseExpr, IExpression substExpr, Variable var) throws SkipException {

        if (baseExpr instanceof Zero) {
            if (substExpr instanceof Zero) return null;
        } else if (baseExpr instanceof ArityOperation) {
            ArityOperation ao = (ArityOperation) baseExpr;
            if (substExpr instanceof ArityOperation) {
                ArityOperation s = (ArityOperation) substExpr;
                if (s.arity == ao.arity && s.name.equals(ao.name)) {
                    for (int i = 0; i < s.arity; i++) {
                        IExpression ans = GetMismatch(ao.arguments.get(i), s.arguments.get(i), var);
                        if (ans != null) return ans;
                    }
                    return null;
                }
            }
        } else if (baseExpr instanceof Quantifier) {
            Quantifier q = (Quantifier) baseExpr;
            if (substExpr instanceof Quantifier) {
                Quantifier sq = (Quantifier) substExpr;
                if (q.function.equals(sq.function)) {
                    return GetMismatch(q.expression, sq.expression, var);
                }
            }
        } else if (baseExpr instanceof Variable) {
            Variable v = (Variable) baseExpr;
            if (v.equals(var)) return substExpr;
            return null;
        }
        throw new SkipException();
    }

    public void Run(String inFile, String outFile) {
        String header = "";
        IExpression toProof = null;

        HashMap<IExpression, Integer> cache = new HashMap<>();
        Parser parser = new Parser();
        IExpression alpha = null;
        String reason = "";
        boolean isProoveCorrect = true;
        try (BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), "UTF-8"))) {
            header = sr.readLine();
            String line = header;
            String[] temp = new String[2];

            for (int i = 0; i < line.length() - 1; i++) {
                if (line.charAt(i) == '|' && line.charAt(i+1) == '-') {
                    temp[0] = line.substring(0, i);
                    temp[1] = line.substring(i+2);
                }
            }

            toProof = parser.parse(temp[1]);
            for (String s : CommaSplit(temp[0])) {
                assumptions.add(alpha = parser.parse(s));
            }
            while ((line = sr.readLine()) != null) {
                proof.add(parser.parse(line));
            }
        } catch (IOException e) {}

        if (alpha != null) {
            assumptions.remove(alpha);
        }

        int i = 0;
        for (i = 0; i < proof.size(); i++) {
            IExpression expr = proof.get(i);
            boolean isProofed = false;


            for (IExpression axiom : axioms) {
                if (expr.equals(axiom)) {
                    isProofed = true;
                    if (alpha != null) {
                        newProof.addAll(BaseDeduct(expr, alpha));
                    }
                    break;
                }
            }


            if (!isProofed)
                for (IExpression assump : assumptions) {
                    if (expr.equals(assump)) {
                        isProofed = true;
                        if (alpha != null) {
                            newProof.addAll(BaseDeduct(expr, alpha));
                        }
                        break;
                    }
                }


            if (!isProofed)
                for (AxiomScheme axiom : axiomSchemes) {
                    if (axiom.isMatch(expr)) {
                        isProofed = true;
                        if (alpha != null) {
                            newProof.addAll(BaseDeduct(expr, alpha));
                        }
                        break;
                    }
                }


            if (!isProofed) {
                if (alpha != null && expr.equals(alpha)) {
                    isProofed = true;
                    for (IExpression e : selfCons) {
                        HashMap<String, IExpression> hashMap = new HashMap<>();
                        hashMap.put("A", expr);
                        newProof.add(SubstituteExprsToExpr(e, hashMap));
                    }
                }
            }


            if (!isProofed) {
                if (inductionAxiom.isMatch(expr)) {
                    try {
                        IExpression f = ((Implication) expr).getRight();
                        Conjunction conj = (Conjunction) ((Implication) expr).getLeft();
                        Quantifier quant = (Quantifier) conj.getRight();
                        Variable x = quant.variable;
                        if (SubstituteVariableToExpr(f, x, new Zero()).equals(((conj).arguments.get(0))) &&
                                SubstituteVariableToExpr(f, x, new Increment(x)).equals(((ArityOperation) quant.expression).arguments.get(1))) {
                            isProofed = true;
                            if (alpha != null) {
                                newProof.addAll(BaseDeduct(expr, alpha));
                            }
                        }
                    } catch (Exception e) {
                        isProoveCorrect = false;
                        break;
                    }
                }
            }


            if (!isProofed) {
                if (universalAxiom.isMatch(expr)) {
                    Quantifier q = (Quantifier) ((ArityOperation) (expr)).arguments.get(0);

                    Variable x = q.variable;
                    IExpression f = q.expression;
                    //@y(((x)'+y=(x+y)')->((x)'+(y)'=(x+(y)')'))->(x'+0=(x+0)')&@y(((x)'+y=(x+y)')->((x)'+(y)'=(x+(y)')'))
                    IExpression term = null;
                    try {
                        term = GetMismatch(f, ((ArityOperation) expr).arguments.get(1), x);
                        if (term != null) term = term.clone();
                        try {
                            if (SubstituteVariableToExpr(f, x, term).equals(((ArityOperation) expr).arguments.get(1))) {
                                isProofed = true;
                                if (alpha != null) {
                                    newProof.addAll(BaseDeduct(expr, alpha));
                                }
                            }

                        } catch (Exception e) {
                            reason = "Терм "+term.toString()+" не свободен для подстановки в формулу "+f.toString()+" вместо "+x.toString();
                            isProoveCorrect = false;
                            break;
                        }
                    } catch (SkipException e) {

                    }

                }
            }


            if (!isProofed) {
                if (existenceAxiom.isMatch(expr)) {
                    Quantifier q = (Quantifier) ((ArityOperation) (expr)).arguments.get(1);

                    Variable x = q.variable;
                    IExpression f = q.expression;

                    IExpression term = null;
                    try {
                        term = GetMismatch(f, ((ArityOperation) (expr)).arguments.get(0), x);
                        if (term != null) term = term.clone();
                        try {
                            if (SubstituteVariableToExpr(f, x, term).equals(((ArityOperation) expr).arguments.get(0))) {
                                isProofed = true;
                                if (alpha != null) {
                                    newProof.addAll((BaseDeduct(expr, alpha)));
                                }
                            }

                        } catch (Exception e) {
                            reason = "Терм "+term.toString()+" не свободен для подстановки в формулу "+f.toString()+" вместо "+x.toString();
                            isProoveCorrect = false;
                            break;
                        }
                    } catch (SkipException skip) {

                    }
                }
            }


            if (!isProofed) {
                for (int j = i - 1; j >= 0; j--) {
                    if (proof.get(j) instanceof Implication)
                    {
                        Implication impl = (Implication) proof.get(j);
                        if (impl.getRight().equals(expr) && cache.containsKey(impl.getLeft())) {
                            isProofed = true;
                            if (alpha != null) {
                                HashMap<String, IExpression> hashMap = new HashMap<>();
                                hashMap.put("A", alpha);
                                hashMap.put("B", impl.arguments.get(0));
                                hashMap.put("C", expr);
                                IExpression t = SubstituteExprsToExpr(axiomSchemes.get(1).expression, hashMap);

                                newProof.add(t);
                                newProof.add(((ArityOperation) t).arguments.get(1));
                                newProof.add(((ArityOperation) ((ArityOperation) t).arguments.get(1)).arguments.get(1));
                            }
                            break;
                        }
                    }
                }
            }


            if (!isProofed) {
                if (expr instanceof Implication)
                {
                    Implication impl = (Implication) expr;
                    if (impl.getRight() instanceof Universal) {
                        Universal uv = (Universal) impl.getRight();
                        Implication ne = new Implication(impl.getLeft(), uv.expression);
                        Variable x = uv.variable;

                        if (cache.containsKey(ne)) {
                            try {
                                boolean isFree = !SubstituteVariableToExpr(impl.getLeft(), x, new Zero()).equals(impl.getLeft());
                                if (!isFree) {
                                    isProofed = true;
                                    if (alpha != null) {
                                        try {
                                            if (SubstituteVariableToExpr(alpha, x, new Zero()).equals(alpha)) {
                                                try (BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream("Universal.proof"), "UTF-8"))) {
                                                    String s;
                                                    while ((s = sr.readLine()) != null) {
                                                        HashMap<String, IExpression> hashMap = new HashMap<>();
                                                        hashMap.put("A", alpha);
                                                        hashMap.put("B", impl.getLeft());
                                                        hashMap.put("C", uv.expression);
                                                        hashMap.put("x", x);
                                                        newProof.add(SubstituteExprsToExpr(parser.parse(s), hashMap));
                                                    }
                                                } catch (IOException e) {
                                                }
                                            } else {
                                                isProoveCorrect = false;
                                                reason = "Переменная " + x.toString() + " входит свободно в формулу " + alpha.toString();
                                                break;
                                            }
                                        } catch (Exception e) {
                                            isProoveCorrect = false;
                                            reason = "Переменная " + x.toString() + " входит свободно в формулу " + alpha.toString();
                                            break;
                                        }
                                    }
                                } else {
                                    isProoveCorrect = false;
                                    reason = "Переменная " + x.toString() + " входит свободно в формулу " + impl.getLeft().toString();
                                    break;
                                }
                            } catch (Exception e) {
                                isProoveCorrect = false;
                                reason = "Переменная " + x.toString() + " входит свободно в формулу " + impl.getLeft().toString();
                                break;
                            }
                        }
                    }
                }
            }


            if (!isProofed) {
                if (expr instanceof Implication) {
                    Implication impl = (Implication) expr;
                    if (impl.getLeft() instanceof Existence) {
                        Existence ex = (Existence) impl.getLeft();
                        Implication ne = new Implication(ex.expression, impl.getRight());
                        Variable x = ex.variable;

                        if (cache.containsKey(ne)) {
                            try {
                                boolean isFree = !SubstituteVariableToExpr(impl.getRight(), x, new Zero()).equals(impl.getRight());
                                if (!isFree) {
                                    isProofed = true;
                                    if (alpha != null) {
                                        try {
                                            if (SubstituteVariableToExpr(alpha, x, new Zero()).equals(alpha)) {
                                                try (BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream("Existence.proof"), "UTF-8"))) {
                                                    String s;
                                                    while ((s = sr.readLine()) != null) {
                                                        HashMap<String, IExpression> hashMap = new HashMap<>();
                                                        hashMap.put("A", alpha);
                                                        hashMap.put("B", ex.expression);
                                                        hashMap.put("C", impl.getRight());
                                                        hashMap.put("x", x);
                                                        newProof.add(SubstituteExprsToExpr(parser.parse(s), hashMap));
                                                    }
                                                } catch (IOException e) {
                                                }
                                            } else {
                                                isProoveCorrect = false;
                                                reason = "Переменная " + x.toString() + " входит свободно в формулу " + alpha.toString();
                                                break;
                                            }
                                        } catch (Exception e) {
                                            isProoveCorrect = false;
                                            reason = "Переменная " + x.toString() + " входит свободно в формулу " + alpha.toString();
                                            break;
                                        }
                                    }
                                } else {
                                    isProoveCorrect = false;
                                    reason = "Переменная " + x.toString() + " входит свободно в формулу " + impl.getLeft().toString();
                                    break;
                                }
                            } catch (Exception e) {
                                isProoveCorrect = false;
                                reason = "Переменная " + x.toString() + " входит свободно в формулу " + impl.getLeft().toString();
                                break;
                            }
                        }
                    }
                }
            }


            if (!isProofed || !isProoveCorrect) {
                if (isProoveCorrect) {
                    reason = "Не доказано";
                }
                isProoveCorrect = false;
                break;
            }
            cache.put(expr, 1);
        }

        try (BufferedWriter sw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile),  "UTF-8")))
        {
            if (isProoveCorrect) {
                if (alpha != null) {
                    proof = newProof;
                    for (int j = 0; j < assumptions.size() - 1; j++) {
                        sw.write(assumptions.get(j) + (j != assumptions.size() - 2 ? "," : ""));
                    }
                    sw.write("|-");
                    sw.write((new Implication(alpha, toProof)).toString());
                    sw.newLine();
                } else {
                    sw.write(header);
                    sw.newLine();
                }
                for (IExpression p : proof) {
                    sw.write(p.toString());
                    sw.newLine();
                }
            } else {
                sw.write("Вывод неверен начиная с формулы номер "+(i + 1));
                if (!reason.equals("Не доказано")) sw.write(": "+reason);
                sw.newLine();
            }
        } catch (IOException e) {}
    }
}