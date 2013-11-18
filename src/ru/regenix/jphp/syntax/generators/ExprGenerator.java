package ru.regenix.jphp.syntax.generators;

import com.google.common.collect.Sets;
import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.common.Separator;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.tokenizer.token.*;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.IntegerExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.EchoRawToken;
import ru.regenix.jphp.tokenizer.token.expr.ExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.*;
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

    protected void processCase(CaseStmtToken result, ListIterator<Token> iterator){
        if (!(result instanceof DefaultStmtToken)){
            Token next = nextToken(iterator);
            ExprStmtToken conditional = analyzer.generator(SimpleExprGenerator.class).getToken(
                    next, iterator, Separator.COLON, null
            );
            if (conditional == null)
                unexpectedToken(next);
            result.setConditional(conditional);
        }

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
            nextToken(iterator), iterator, true,
            EndswitchStmtToken.class, CaseStmtToken.class, DefaultStmtToken.class, BraceExprToken.class
        );
        result.setBody(body);
    }

    protected void processSwitch(SwitchStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();
        result.setValue(getInBraces(BraceExprToken.Kind.SIMPLE, iterator));
        if (result.getValue() == null)
            unexpectedToken(iterator.previous());

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndswitchStmtToken.class
        );
        List<CaseStmtToken> cases = new ArrayList<CaseStmtToken>();
        for(ExprStmtToken instruction : body.getInstructions()){
            if (instruction.isSingle()){
                Token token = instruction.getSingle();
                if (token instanceof CaseStmtToken){
                    cases.add((CaseStmtToken)token);
                    if (token instanceof DefaultStmtToken){
                        if (result.getDefaultCase() != null)
                            unexpectedToken(token);
                        result.setDefaultCase((DefaultStmtToken)token);
                    }
                } else
                    unexpectedToken(token);
            } else
                unexpectedToken(instruction.getSingle());
        }
        result.setCases(cases);
        result.setLocal(analyzer.removeLocalScope());
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

    protected void processFor(ForStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();
        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next);

        ExprStmtToken init = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.SEMICOLON, null);

        result.setInitLocal(analyzer.removeLocalScope());
        analyzer.addLocalScope();

        ExprStmtToken condition = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.SEMICOLON, null);

        ExprStmtToken iteratorExpr = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, false, BraceExprToken.Kind.SIMPLE);

        result.setIterationLocal(Sets.newHashSet(analyzer.getLocalScope()));

        nextAndExpected(iterator, BraceExprToken.class);
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
            nextToken(iterator), iterator, EndforStmtToken.class
        );

        result.setInitExpr(init);
        result.setCondition(condition);
        result.setIterationExpr(iteratorExpr);
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
        analyzer.addLocalScope();
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator);
        result.setBody(body);

        Token next = nextToken(iterator);
        if (next instanceof WhileStmtToken){
            result.setCondition(getInBraces(BraceExprToken.Kind.SIMPLE, iterator));

            next = nextToken(iterator);
            result.setLocal(analyzer.removeLocalScope());
            if (next instanceof SemicolonToken)
                return;
        }
        unexpectedToken(next);
    }

    protected void processJump(JumpStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        long level = 1;
        if (next instanceof IntegerExprToken){
            level = ((IntegerExprToken) next).getValue();
            if (level < 1)
                throw new FatalException(
                        Messages.ERR_FATAL_OPERATOR_ACCEPTS_ONLY_POSITIVE.fetch(result.getWord()),
                        result.toTraceInfo(analyzer.getContext())
                );

            next = nextToken(iterator);
        }

        result.setLevel((int)level);
        if (!(next instanceof SemicolonToken))
            unexpectedToken(next);
    }

    protected void processImport(RequireStmtToken result, ListIterator<Token> iterator){
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.SEMICOLON, null);
        result.setValue(value);
        if (analyzer.getFunction() != null){
            analyzer.getFunction().setDynamicLocal(true);
            analyzer.getFunction().setCallsExist(true);
        }
    }

    public void processEcho(EchoStmtToken result, ListIterator<Token> iterator){
        List<ExprStmtToken> arguments = new ArrayList<ExprStmtToken>();
        ExprStmtToken argument;
        do {
            argument = analyzer.generator(SimpleExprGenerator.class).getToken(
                    nextToken(iterator), iterator, Separator.COMMA_OR_SEMICOLON, null
            );

            if (argument != null)
                arguments.add(argument);

            if (iterator.previous() instanceof SemicolonToken){
                iterator.next();
                break;
            }
            iterator.next();
        } while (argument != null);
        result.setArguments(arguments);
    }

    protected List<Token> processSimpleExpr(Token current, ListIterator<Token> iterator){
        ExprStmtToken token = analyzer.generator(SimpleExprGenerator.class).getToken(current, iterator);
        return token.getTokens();
    }

    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Class<? extends Token>... endTokens) {
        List<Token> tokens = new ArrayList<Token>();
        do {
            if (current instanceof EndStmtToken || isTokenClass(current, endTokens)){
                if (current instanceof ElseIfStmtToken){
                    IfStmtToken ifStmt = new IfStmtToken(current.getMeta());
                    processIf(ifStmt, iterator);
                    tokens.add(ifStmt);
                }

                if (!isTokenClass(current, endTokens))
                    unexpectedToken(current);

                if (current instanceof BraceExprToken && !isClosedBrace(current, BraceExprToken.Kind.BLOCK)){
                    unexpectedToken(current);
                }

                break;
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
            } else if (current instanceof RequireStmtToken){
                processImport((RequireStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof EchoStmtToken){
                processEcho((EchoStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof IfStmtToken){
                processIf((IfStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof ReturnStmtToken){
                processReturn((ReturnStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof ForStmtToken){
                processFor((ForStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof WhileStmtToken){
                processWhile((WhileStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof DoStmtToken){
                processDo((DoStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof CaseStmtToken){
                processCase((CaseStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof SwitchStmtToken){
                processSwitch((SwitchStmtToken) current, iterator);
                tokens.add(current);
                break;
            } else if (current instanceof JumpStmtToken){
                processJump((JumpStmtToken)current, iterator);
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
                    if (endTokens != null && !isTokenClass(current, endTokens))
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

           /* if (iterator.hasNext())
                current = nextToken(iterator);
            else
                current = null; */
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
