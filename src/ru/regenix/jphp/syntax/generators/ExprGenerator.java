package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.lexer.tokens.BreakToken;
import ru.regenix.jphp.lexer.tokens.OpenEchoTagToken;
import ru.regenix.jphp.lexer.tokens.SemicolonToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.EchoRawToken;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.ImportExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.IncludeExprToken;
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

    public ExprStmtToken getInBraces(BraceExprToken.Kind kind, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, kind))
            unexpectedToken(next, BraceExprToken.Kind.toOpen(kind));

        ExprStmtToken result = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, false, kind);

        if (!isClosedBrace(nextToken(iterator), kind))
            unexpectedToken(next, BraceExprToken.Kind.toClose(kind));

        return result;
    }

    protected void processIf(IfStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();

        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndifStmtToken.class
        );
        if (iterator.hasNext()){
            Token next = iterator.next();
            if (next instanceof ElseStmtToken){
                BodyStmtToken bodyElse = analyzer.generator(BodyGenerator.class).getToken(
                    nextToken(iterator), iterator, EndifStmtToken.class
                );
                result.setElseBody(bodyElse);
            } else if (next instanceof ElseIfStmtToken){
                BodyStmtToken bodyElse = analyzer.generator(BodyGenerator.class).getToken(
                        next, iterator, EndifStmtToken.class
                );
                result.setElseBody(bodyElse);
            } else if (next instanceof EndifStmtToken){
               // nop
            } else
                iterator.previous();
        }

        result.setCondition(condition);
        result.setBody(body);

        result.setLocal(analyzer.removeLocalScope());
    }

    protected void processWhile(WhileStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();

        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndwhileStmtToken.class
        );
        result.setCondition(condition);
        result.setBody(body);

        result.setLocal(analyzer.removeLocalScope());
    }

    protected void processReturn(ReturnStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (next instanceof SemicolonToken)
            result.setValue(null);
        else {
            ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(next, iterator);
            result.setValue(value);
        }
    }

    protected void processDo(DoStmtToken result, ListIterator<Token> iterator){
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator);
        result.setBody(body);

        Token next = nextToken(iterator);
        if (next instanceof WhileStmtToken){
            result.setCondition(getInBraces(BraceExprToken.Kind.SIMPLE, iterator));

            next = nextToken(iterator);
            if (next instanceof SemicolonToken)
                return;
        }
        unexpectedToken(next);
    }

    protected void processImport(ImportExprToken result, ListIterator<Token> iterator){
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator);
        result.setValue(value);
        if (analyzer.getFunction() != null){
            analyzer.getFunction().setDynamicLocal(true);
            analyzer.getFunction().setCallsExist(true);
        }
    }

    protected List<Token> processSimpleExpr(Token current, ListIterator<Token> iterator){
        ExprStmtToken token = analyzer.generator(SimpleExprGenerator.class).getToken(current, iterator);
        return token.getTokens();
    }

    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Class<? extends EndStmtToken> endToken) {
        List<Token> tokens = new ArrayList<Token>();
        do {
            if (current instanceof EndStmtToken){
                if (current instanceof ElseIfStmtToken){
                    IfStmtToken ifStmt = new IfStmtToken(current.getMeta());
                    processIf(ifStmt, iterator);
                    tokens.add(ifStmt);
                }

                if (endToken == null)
                    unexpectedToken(current);

                if (isTokenClass(current, endToken))
                    break;

                unexpectedToken(current);
            } else if (current instanceof EchoRawToken){
                tokens.add(current);
                break;
            } else if (current instanceof OpenEchoTagToken){
                OpenEchoTagToken result = (OpenEchoTagToken)current;
                result.setValue(
                        analyzer.generator(SimpleExprGenerator.class).getToken(nextToken(iterator), iterator)
                );
                tokens.add(current);
                break;
            } else if (current instanceof ImportExprToken){
                processImport((IncludeExprToken) current, iterator);
                tokens.add(current);
            } else if (current instanceof IfStmtToken){
                processIf((IfStmtToken)current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof ReturnStmtToken){
                processReturn((ReturnStmtToken)current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof WhileStmtToken){
                processWhile((WhileStmtToken)current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof DoStmtToken){
                processDo((DoStmtToken)current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof BreakToken){
                tokens.add(current);
                break;
            } else if (current instanceof SemicolonToken){
                tokens.add(current);
                break;
            } else if (current instanceof ExprToken || current instanceof FunctionStmtToken){
                if (isClosedBrace(current, BraceExprToken.Kind.BLOCK)){
                    if (endToken != null)
                        unexpectedToken(current);

                    break;
                }

                List<Token> tmp = processSimpleExpr(current, iterator);
                tokens.addAll(tmp);
                break;
            } else {
                if (!tokens.isEmpty())
                    unexpectedToken(current);

                break;
            }

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
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator) {
        return getToken(current, iterator, null);
    }
}
