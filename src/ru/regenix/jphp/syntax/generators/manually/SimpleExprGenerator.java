package ru.regenix.jphp.syntax.generators.manually;


import ru.regenix.jphp.lexer.tokens.SemicolonToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.*;
import ru.regenix.jphp.lexer.tokens.expr.value.ArrayGetExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.*;
import ru.regenix.jphp.lexer.tokens.expr.value.*;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.ExprGenerator;
import ru.regenix.jphp.syntax.generators.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SimpleExprGenerator extends Generator<ExprStmtToken> {

    public SimpleExprGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected CallExprToken processCall(Token previous, Token current, ListIterator<Token> iterator){
        ExprStmtToken param;

        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
        do {
            param = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE);

            if (param != null)
                parameters.add(param);

        } while (param != null);
        nextToken(iterator);

        CallExprToken result = new CallExprToken(TokenMeta.of(previous, current));
        result.setName(previous);
        result.setParameters(parameters);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setCallsExist(true);
        }

        return result;
    }

    protected GetVarExprToken processVarVar(Token current, Token next, ListIterator<Token> iterator){
        ExprStmtToken name = null;
        if (next instanceof VariableExprToken){ // $$var
            name = new ExprStmtToken(next);
            nextToken(iterator);
        } else if (next instanceof DollarExprToken){ // $$$var
            current = nextToken(iterator);
            next    = nextToken(iterator);
            name    = new ExprStmtToken(processVarVar(current, next, iterator));
        } else if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){ // ${var}
            name = analyzer.generator(ExprGenerator.class).getInBraces(
                    BraceExprToken.Kind.BLOCK, iterator
            );
        }

        if (name == null)
            unexpectedToken(next);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setDynamicLocal(true);
            analyzer.getFunction().setVarsExist(true);
        }

        GetVarExprToken result = new GetVarExprToken(TokenMeta.of(current, name));
        result.setName(name);
        return result;
    }

    protected Token processSimpleToken(Token current, Token previous, Token next, ListIterator<Token> iterator){
        if (current instanceof DollarExprToken){
            return processVarVar(current, next, iterator);
        }

        if (current instanceof VariableExprToken){
            analyzer.getLocalScope().add((VariableExprToken) current);
            if (analyzer.getFunction() != null)
                analyzer.getFunction().setVarsExist(true);
        }

        if (current instanceof AssignExprToken && next instanceof AmpersandToken){
            iterator.next();
            return new AssignRefExprToken(TokenMeta.of(current, next));
        }

        if ((current instanceof MinusExprToken || current instanceof PlusExprToken)
                && (next instanceof IntegerExprToken || next instanceof DoubleExprToken
                        || next instanceof HexExprValue)){

            if (!(previous instanceof ValueExprToken ||
                    isOpenedBrace(previous, BraceExprToken.Kind.SIMPLE))){
                iterator.next();
                // if it minus
                if (current instanceof MinusExprToken){
                    if (next instanceof IntegerExprToken){
                        return new IntegerExprToken(TokenMeta.of(current, next));
                    } else if (next instanceof DoubleExprToken){
                        return new DoubleExprToken(TokenMeta.of(current, next));
                    } else {
                        return new HexExprValue(TokenMeta.of(current, next));
                    }
                }

                // if it plus nothing
                return next;
            }
        }

        if (current instanceof MinusExprToken){
            if (!(previous instanceof ValueExprToken)){
                return new UnarMinusExprToken(current.getMeta());
            }
        }

        if (current instanceof LogicOperatorExprToken){
            if (next == null)
                unexpectedToken(current);

            LogicOperatorExprToken logic = (LogicOperatorExprToken)current;
            ExprStmtToken result = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator);
            logic.setRightValue(result);
            return logic;
        }

        return null;
    }

    protected Token processArrayToken(Token previous, Token current, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (isClosedBrace(next, BraceExprToken.Kind.ARRAY)){
            return new ArrayPushExprToken(TokenMeta.of(current, next));
        } else
            iterator.previous();

        ExprStmtToken param;
        ArrayGetExprToken result = (ArrayGetExprToken)current;

        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
        do {
            param = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, false, BraceExprToken.Kind.ARRAY);

            if (param != null)
                parameters.add(param);

        } while (param != null);

        result.setParameters(parameters);
        return result;
    }

    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  boolean commaSeparator, BraceExprToken.Kind closedBraceKind) {
        List<Token> tokens = new ArrayList<Token>();
        Token previous = null;
        Token next = iterator.hasNext() ? iterator.next() : null;
        if (next != null)
            iterator.previous();

        int braceOpened = 0;
        do {
            if (isOpenedBrace(current, BraceExprToken.Kind.SIMPLE)){
                if (previous instanceof NameToken || previous instanceof VariableExprToken){
                    tokens.set(tokens.size() - 1, current = processCall(previous, current, iterator));
                } else {
                    braceOpened += 1;
                    tokens.add(current);
                }
            } else if (braceOpened > 0 && isClosedBrace(current, BraceExprToken.Kind.SIMPLE)){
                braceOpened -= 1;
                tokens.add(current);
            } else if (isOpenedBrace(current, BraceExprToken.Kind.ARRAY)){
                if (isTokenClass(previous,
                        NameToken.class,
                        VariableExprToken.class,
                        CallExprToken.class,
                        ArrayGetExprToken.class)){
                    // array
                    tokens.set(tokens.size() - 1, current = processArrayToken(previous, current, iterator));
                }
            } else if (current instanceof CommaToken){
                if (commaSeparator){
                    break;
                } else {
                    unexpectedToken(current);
                }
            } else if (isClosedBrace(current, closedBraceKind)){
                iterator.previous();
                break;
            } else if (current instanceof SemicolonToken){
                if (commaSeparator || closedBraceKind != null || tokens.isEmpty())
                    unexpectedToken(current);
                break;
            } else if (current instanceof BraceExprToken){
                unexpectedToken(current);
            } else if (current instanceof ExprToken) {
                Token token = processSimpleToken(current, previous, next, iterator);
                if (token != null)
                    current = token;

                tokens.add(current);
            } else
                unexpectedToken(current);

            previous = current;
            if (iterator.hasNext()){
                current = nextToken(iterator);
                next = iterator.hasNext() ? iterator.next() : null;
                if (next != null)
                    iterator.previous();
            } else
                current = null;
        } while (current != null);

        if (tokens.isEmpty())
            return null;

        return new ExprStmtToken(tokens);
    }

    @Override
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator){
        return getToken(current, iterator, false, null);
    }

    public boolean isAutomatic() {
        return false;
    }
}
