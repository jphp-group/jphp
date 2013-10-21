package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.lexer.tokens.SemicolonExprToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.*;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ExprGenerator extends Generator<ExprStmtToken> {

    public ExprGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    protected ExprStmtToken getInBraces(BraceExprToken.Kind kind, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, kind))
            unexpectedToken(next, BraceExprToken.Kind.toOpen(kind));

        ExprStmtToken result = (ExprStmtToken)analyzer.generateToken(
                nextToken(iterator), iterator, ExprGenerator.class
        );

        if (!isClosedBrace(next, kind))
            unexpectedToken(next, BraceExprToken.Kind.toClose(kind));

        return result;
    }

    protected void processIf(IfStmtToken result, ListIterator<Token> iterator){
        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        BodyStmtToken body = (BodyStmtToken) analyzer.generateToken(
                nextToken(iterator), iterator, BodyGenerator.class
        );
        result.setCondition(condition);
        result.setBody(body);
    }

    protected void processWhile(WhileStmtToken result, ListIterator<Token> iterator){
        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        BodyStmtToken body = (BodyStmtToken) analyzer.generateToken(
                nextToken(iterator), iterator, BodyGenerator.class
        );
        result.setCondition(condition);
        result.setBody(body);
    }

    protected void processDo(DoStmtToken result, ListIterator<Token> iterator){
        BodyStmtToken body = (BodyStmtToken) analyzer.generateToken(
                nextToken(iterator), iterator, BodyGenerator.class
        );
        result.setBody(body);

        Token next = nextToken(iterator);
        if (next instanceof WhileStmtToken){
            result.setCondition(getInBraces(BraceExprToken.Kind.SIMPLE, iterator));

            next = nextToken(iterator);
            if (next instanceof SemicolonExprToken)
                return;
        }
        unexpectedToken(next);
    }

    protected List<Token> processSimpleExpr(Token current, ListIterator<Token> iterator){
        ExprStmtToken token = (ExprStmtToken) analyzer.generateToken(
                current, iterator, SimpleExprGenerator.class
        );
        return token.getTokens();
    }

    @Override
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator) {
        List<Token> tokens = new ArrayList<Token>();
        do {
            if (current instanceof IfStmtToken){
                processIf((IfStmtToken)current, iterator);
                tokens.add(current);
            } else if (current instanceof WhileStmtToken){
                processWhile((WhileStmtToken)current, iterator);
                tokens.add(current);
            } else if (current instanceof DoStmtToken){
                processDo((DoStmtToken)current, iterator);
                tokens.add(current);
            } else if (current instanceof ExprStmtToken || current instanceof FunctionStmtToken){
                List<Token> tmp = processSimpleExpr(current, iterator);
                tokens.addAll(tmp);
                break;
            } else {
               unexpectedToken(current);
            }

            current = nextToken(iterator);
        } while (iterator.hasNext());

        if (tokens.isEmpty())
            return null;

        return new ExprStmtToken(tokens);
    }
}
