package Parser;

import Parser.Operators.*;
import Parser.Operators.Math.*;
import Parser.Operators.Logic.*;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Parser {
    public Parser() {
    }

    private boolean skipIf(Predicate<Character> skipper, ParserContext context) {
        boolean rv = false;
        if (context.currentPosition < context.text.length() && skipper.test(context.text.charAt(context.currentPosition))) {
            rv = true;
            context.currentPosition++;
        }
        return rv;
    }

    private boolean skipWhile(Predicate<Character> skipper, ParserContext context) {
        boolean rv = false;
        while (context.currentPosition < context.text.length() && skipper.test(context.text.charAt(context.currentPosition))) {
            rv = true;
            context.currentPosition++;
        }
        return rv;
    }

    private boolean skipWhiteSpace(ParserContext context) {
        return skipWhile((c) -> Character.isWhitespace(c), context);
    }

    private boolean skipCharacter(char toSkip, ParserContext context) {
        return skipIf((c) -> c == toSkip, context);
    }

    private boolean skipASymbol(ParserContext context) {
        return skipIf((c) -> true, context);
    }

    public IExpression parse(String value) {
        ParserContext ctx = new ParserContext(value, 0);
        try {
            return getExpression(ctx);
        } catch (Exception e) {
            return null;
        }
    }


    private IExpression getExpression(ParserContext ctx) throws Exception {
        skipWhiteSpace(ctx);
        IExpression a = getDisjunction(ctx);
        if (ctx.currentPosition != ctx.text.length() && skipCharacter('-', ctx)) {
            skipASymbol(ctx);
            a = new Implication(a, getExpression(ctx));
        }
        skipWhiteSpace(ctx);
        return a;
    }

    private IExpression getDisjunction(ParserContext ctx) throws Exception {
        skipWhiteSpace(ctx);
        IExpression a = getConjunction(ctx);
        while (ctx.currentPosition != ctx.text.length() && skipCharacter('|', ctx)) {
            a = new Disjunction(a, getConjunction(ctx));
        }
        skipWhiteSpace(ctx);
        return a;
    }

    private IExpression getConjunction(ParserContext ctx) throws Exception {
        skipWhiteSpace(ctx);
        IExpression a = getUnary(ctx);
        while (ctx.currentPosition != ctx.text.length() && skipCharacter('&', ctx)) {
            a = new Conjunction(a, getUnary(ctx));
        }
        skipWhiteSpace(ctx);
        return a;
    }

    private IExpression getUnary(ParserContext ctx) throws Exception {
        skipWhiteSpace(ctx);

        if (skipCharacter('!', ctx)) {
            skipWhiteSpace(ctx);
            return new Negation(getUnary(ctx));
        }

        if (skipCharacter('(', ctx)) {
            ParserContext saveCtx = ctx.clone();
            try {
                IExpression expr = getExpression(ctx);
                if (expr instanceof IMath)
                {
                    ctx = saveCtx;
                    ctx.currentPosition--;
                    return getPredicate(ctx);
                }
                else
                {
                    skipASymbol(ctx);
                    return expr;
                }
            } catch (Exception e) {}
            ctx.currentPosition = saveCtx.currentPosition - 1;
            return getPredicate(ctx);
        }

        char prevChar = ctx.text.charAt(ctx.currentPosition);
        if (skipCharacter('@', ctx) || skipCharacter('?', ctx)) {
            String value = "";
            while (ctx.currentPosition != ctx.text.length() && (Character.isLowerCase(ctx.text.charAt(ctx.currentPosition))
                    || Character.isDigit(ctx.text.charAt(ctx.currentPosition))))

            {
                value += ctx.text.charAt(ctx.currentPosition);
                skipASymbol(ctx);

            }
            Variable constant = new Variable(value);
            if (prevChar == '@') {
                skipWhiteSpace(ctx);
                return new Universal(constant, getUnary(ctx));
            } else {
                skipWhiteSpace(ctx);
                return new Existence(constant, getUnary(ctx));
            }
        }

        skipWhiteSpace(ctx);
        return getPredicate(ctx);
    }


    private IExpression getPredicate(ParserContext ctx) throws Exception {
        skipWhiteSpace(ctx);
        char curChar = ctx.text.charAt(ctx.currentPosition);
        if (Character.isUpperCase(curChar)) {
            String value = "";
            while (ctx.currentPosition != ctx.text.length() && Character.isLetterOrDigit(ctx.text.charAt(ctx.currentPosition))) {
                value += ctx.text.charAt(ctx.currentPosition);
                skipASymbol(ctx);

            }
            skipWhiteSpace(ctx);
            ArrayList<IExpression> args = new ArrayList<>();
            if (skipCharacter('(', ctx)) {
                args.add(getTerm(ctx));
                skipWhiteSpace(ctx);
                while (skipCharacter(',', ctx)) {
                    args.add(getTerm(ctx));
                    skipWhiteSpace(ctx);
                }
                skipASymbol(ctx);
            }
            skipWhiteSpace(ctx);
            return new Predication(value, args.size(), args);
        } else {
            IExpression first = getTerm(ctx);
            skipWhiteSpace(ctx);
            if (!skipCharacter('=', ctx)) {
                throw new Exception();
            }
            return new Equation(first, getTerm(ctx));
        }
    }

    private IExpression getTerm(ParserContext ctx) throws Exception  {
        skipWhiteSpace(ctx);
        IExpression t = getAdd(ctx);
        skipWhiteSpace(ctx);
        while (skipCharacter('+', ctx)) {
            t = new Addition(t, getAdd(ctx));
            skipWhiteSpace(ctx);
        }
        return t;
    }

    private IExpression getAdd(ParserContext ctx) throws Exception  {
        skipWhiteSpace(ctx);
        IExpression t = getMul(ctx);
        skipWhiteSpace(ctx);
        while (skipCharacter('*', ctx)) {
            t = new Multiplication(t, getMul(ctx));
            skipWhiteSpace(ctx);
        }
        return t;
    }

    private IExpression getMul(ParserContext ctx) throws Exception {
        IExpression rv;
        skipWhiteSpace(ctx);
        char curChar = ctx.text.charAt(ctx.currentPosition);
        if (Character.isLetter(curChar) && Character.isLowerCase(curChar)) {
            String value = "";
            while (ctx.currentPosition != ctx.text.length() && Character.isLetterOrDigit(ctx.text.charAt(ctx.currentPosition))) {
                value += ctx.text.charAt(ctx.currentPosition);
                skipASymbol(ctx);
            }
            skipWhiteSpace(ctx);
            ArrayList<IExpression> args = new ArrayList<IExpression>();
            if (skipCharacter('(', ctx)) {
                args.add(getTerm(ctx));
                skipWhiteSpace(ctx);
                while (skipCharacter(',', ctx)) {
                    args.add(getTerm(ctx));
                    skipWhiteSpace(ctx);
                }
                skipASymbol(ctx);
                rv = new Function(value, args.size(), args);
            } else {
                rv = new Variable(value);
            }
        } else if (skipCharacter('(', ctx)) {
            rv = getTerm(ctx);
            skipWhiteSpace(ctx);
            skipASymbol(ctx);
        } else if (skipCharacter('0', ctx)) {
            rv = new Zero();
        } else {
            throw new Exception();
        }
        skipWhiteSpace(ctx);


        while (skipCharacter('\'', ctx)) {
            rv = new Increment(rv);
        }
        skipWhiteSpace(ctx);
        return rv;
    }
}
