package ru.regenix.jphp.syntax.generators.manually;


import ru.regenix.jphp.lexer.tokens.SemicolonToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.*;
import ru.regenix.jphp.lexer.tokens.expr.ArrayGetExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.ArrayPushExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.AssignExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.AssignRefExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
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
        return result;
    }

    protected Token processSimpleToken(Token current, Token previous){
        if (previous instanceof AssignExprToken && current instanceof AmpersandToken){
            return new AssignRefExprToken(TokenMeta.of(previous, current));
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
        do {
            if (isOpenedBrace(current, BraceExprToken.Kind.SIMPLE)){
                if (previous instanceof NameToken || previous instanceof VariableExprToken){
                    tokens.set(tokens.size() - 1, current = processCall(previous, current, iterator));
                }
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
                Token token = processSimpleToken(current, previous);
                if (token != null)
                    current = token;

                tokens.add(current);
            } else
                unexpectedToken(current);

            previous = current;
            if (iterator.hasNext())
                current = nextToken(iterator);
            else
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
