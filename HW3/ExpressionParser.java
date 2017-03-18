/**
 * Created by Bogdan on 17.02.2017.
 */
public class ExpressionParser {
    private static String unparse;
    private static int left;
    private static int len;

    public static Expression parse(String str) {
        StringBuilder newStr = new StringBuilder("");
        for (int i = 0; i < str.length(); ++i) {
            if (Character.isWhitespace(str.charAt(i))) {
                continue;
            } else {
                newStr.append(str.charAt(i));
            }
        }
        unparse = newStr.toString();
        left = 0;
        len = unparse.length();
        return Impl();
    }

    private static Expression Impl() {
        Expression acc = Disj();
        Expression res;
        if (len - left > 2 && unparse.substring(left, left + 2).equals("->")) {
            left += 2;
            res = Impl();
            acc = new Implication(acc, res);
        }
        return acc;
    }

    private static Expression Disj() {
        Expression acc = Conj();
        Expression res;
        while (len - left > 1 && unparse.charAt(left) == '|') {
            left++;
            res = Conj();
            acc = new Disjunction(acc, res);
        }
        return acc;
    }

    private static Expression Conj() {
        Expression acc = Neg();
        Expression res;
        while (len - left > 1 && unparse.charAt(left) == '&') {
            left++;
            res = Neg();
            acc = new Conjunction(acc, res);
        }
        return acc;
    }

    private static Expression Neg() {
        Expression acc;
        if (len - left > 1 && unparse.charAt(left) == '!') {
            left++;
            acc = Neg();
            acc = new Negation(acc);
        } else acc = Bracket();
        return acc;
    }

    private static Expression Bracket() {
        if (len - left > 0) {
            char firstCh = unparse.charAt(left);
            if (firstCh == '(') {
                left++;
                Expression res = Impl();
                left++;
                return res;
            }
        }
        return Var();
    }

    private static Expression Var() {
        int length = 1;
        while (left + length < len &&
               (Character.isAlphabetic(unparse.charAt(left + length)) ||
                Character.isDigit(unparse.charAt(left + length)))) {
            length++;
        }
        if (left + length > len) return null;
        Expression res = new Variable(unparse.substring(left, left + length));
        left += length;
        return res;
    }
}