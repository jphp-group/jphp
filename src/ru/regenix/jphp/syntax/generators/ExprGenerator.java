package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.common.Separator;
import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;
import ru.regenix.jphp.tokenizer.token.BreakToken;
import ru.regenix.jphp.tokenizer.token.OpenEchoTagToken;
import ru.regenix.jphp.tokenizer.token.SemicolonToken;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.expr.CommaToken;
import ru.regenix.jphp.tokenizer.token.expr.ExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.AmpersandRefToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.AssignExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.KeyValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.ClosureStmtToken;
import ru.regenix.jphp.tokenizer.token.expr.value.IntegerExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.StaticExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.*;

import java.util.ArrayList;
import java.util.HashSet;
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
            unexpectedToken(iterator);

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, false, false, EndswitchStmtToken.class
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

        if (body.isAlternativeSyntax())
            iterator.next();

        result.setCases(cases);
        result.setLocal(analyzer.removeLocalScope());
    }

    protected void processIf(IfStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();

        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        if (condition == null)
            unexpectedToken(iterator);

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndifStmtToken.class, ElseIfStmtToken.class
        );
        if (iterator.hasNext()){
            Token next = iterator.next();
            if (next instanceof ElseStmtToken){
                BodyStmtToken bodyElse = analyzer.generator(BodyGenerator.class).getToken(
                    nextToken(iterator), iterator, false, false, EndifStmtToken.class
                );
                if (!bodyElse.getInstructions().isEmpty())
                    result.setElseBody(bodyElse);

                if (bodyElse.isAlternativeSyntax())
                    iterator.next();

            } else if (next instanceof ElseIfStmtToken){
                next = new IfStmtToken(next.getMeta());
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

    protected void processForeach(ForeachStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();
        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, "(");

        ExprStmtToken expr = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.AS, null);
        if (expr == null)
            unexpectedToken(iterator.previous());

        result.setIterator(expr);

        next = nextToken(iterator);
        boolean reference = false;
        if (next instanceof AmpersandRefToken){
            reference = true;
            next = nextToken(iterator);
        }

        if (next instanceof VariableExprToken) {
            VariableExprToken value = (VariableExprToken)next;
            if (nextToken(iterator) instanceof KeyValueExprToken){
                result.setKey(value);
                result.setKeyReference(reference);

                next = nextToken(iterator);
                reference = false;
                if (next instanceof AmpersandRefToken){
                    reference = true;
                    next = nextToken(iterator);
                }

                if (next instanceof VariableExprToken){
                    result.setValue((VariableExprToken)next);
                    result.setValueReference(reference);
                } else
                    unexpectedToken(next, "$var");

            } else {
                result.setValue(value);
                result.setValueReference(reference);
                iterator.previous();
            }
        } else
            unexpectedToken(next, "$var");

        next = nextToken(iterator);
        if (!isClosedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, ")");

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndforeachStmtToken.class
        );
        result.setBody(body);

        if (analyzer.getFunction() != null) {
            if (result.getKey() != null){
                analyzer.getFunction().getRefLocal().add(result.getKey());
                analyzer.getFunction().getUnstableLocal().add(result.getKey());
            }

            analyzer.getFunction().getRefLocal().add(result.getValue());
            analyzer.getFunction().getUnstableLocal().add(result.getValue());
        }

        if (result.getKey() != null)
            analyzer.getLocalScope().add(result.getKey());
        analyzer.getLocalScope().add(result.getValue());

        result.setLocal(analyzer.removeLocalScope());
    }

    protected void processFor(ForStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();
        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, "(");

        ExprStmtToken init;
        List<ExprStmtToken> inits = new ArrayList<ExprStmtToken>();
        do {
            init = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.COMMA_OR_SEMICOLON, null);
            if (init == null)
                break;

            inits.add(init);

            if (iterator.previous() instanceof SemicolonToken) {
                iterator.next();
                break;
            }
            iterator.next();
        } while (true);

        result.setInitLocal(analyzer.removeLocalScope());
        analyzer.addLocalScope();

        ExprStmtToken condition = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.SEMICOLON, null);

        // ITERATIONS
        List<ExprStmtToken> iterations = new ArrayList<ExprStmtToken>();

        do {
            ExprStmtToken iteratorExpr = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.COMMA, BraceExprToken.Kind.SIMPLE);
            if (iteratorExpr == null)
                break;

            iterations.add(iteratorExpr);

            if (isClosedBrace(iterator.previous(), BraceExprToken.Kind.SIMPLE)) {
                iterator.next();
                break;
            }
            iterator.next();
        } while (true);

        result.setIterationLocal(new HashSet<VariableExprToken>(analyzer.getLocalScope()));

        nextAndExpected(iterator, BraceExprToken.class);
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
            nextToken(iterator), iterator, EndforStmtToken.class
        );

        result.setInitExpr(inits);
        result.setCondition(condition);
        result.setIterationExpr(iterations);
        result.setBody(body);
        result.setLocal(analyzer.removeLocalScope());
    }

    protected void processWhile(WhileStmtToken result, ListIterator<Token> iterator){
        analyzer.addLocalScope();

        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        if (condition == null)
            unexpectedToken(iterator);

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
            if (result.getCondition() == null)
                unexpectedToken(iterator);

            next = nextToken(iterator);
            result.setLocal(analyzer.removeLocalScope());
            if (next instanceof SemicolonToken)
                return;
        }
        unexpectedToken(next);
    }

    protected void processGlobal(GlobalStmtToken result, ListIterator<Token> iterator){
        List<VariableExprToken> variables = new ArrayList<VariableExprToken>();
        Token next = nextToken(iterator);
        Token prev = null;
        do {
            if (next instanceof VariableExprToken) {
                VariableExprToken variable = (VariableExprToken)next;
                analyzer.getLocalScope().add(variable);
                if (analyzer.getFunction() != null) {
                    analyzer.getFunction().getRefLocal().add(variable);
                    analyzer.getFunction().getUnstableLocal().add(variable);
                }

                variables.add(variable);
            } else if (next instanceof CommaToken){
                if (!(prev instanceof VariableExprToken))
                    unexpectedToken(next);
            } else if (next instanceof SemicolonToken){
                if (!(prev instanceof VariableExprToken))
                    unexpectedToken(next);
                break;
            } else
                unexpectedToken(next);

            prev = next;
            next = nextToken(iterator);
        } while (true);
        result.setVariables(variables);
    }

    protected StaticStmtToken processStatic(StaticExprToken token, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (next instanceof VariableExprToken){
            VariableExprToken variable = (VariableExprToken)next;
            analyzer.addLocalScope().add(variable);
            if (analyzer.getFunction() != null){
                analyzer.getFunction().getRefLocal().add(variable);
                analyzer.getFunction().getUnstableLocal().add(variable);
            }

            StaticStmtToken result = new StaticStmtToken(token.getMeta());
            result.setVariable((VariableExprToken)next);
            next = nextToken(iterator);
            if (next instanceof AssignExprToken){
                ExprStmtToken initValue = analyzer.generator(SimpleExprGenerator.class).getToken(
                        nextToken(iterator), iterator, null, null
                );
                result.setInitValue(initValue);
            } else if (next instanceof SemicolonToken){
                result.setInitValue(null);
            } else
                unexpectedToken(next);
            return result;
        } else {
            iterator.previous();

            //unexpectedToken(next); TODO: check it!
        }
        return null;
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
        if (value == null)
            unexpectedToken(iterator);

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
        ExprStmtToken token = analyzer.generator(SimpleExprGenerator.class)
                .getToken(current, iterator, Separator.SEMICOLON, null);
        return token.getTokens();
    }

    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Class<? extends Token>... endTokens) {
        List<Token> tokens = new ArrayList<Token>();
        do {
            if (current instanceof EndStmtToken || isTokenClass(current, endTokens)){
                boolean doBreak = true;
                if (!isTokenClass(current, endTokens))
                    unexpectedToken(current);

                if (current instanceof BraceExprToken){
                    if (((BraceExprToken) current).isOpened()){
                        doBreak = false;
                    } else if (!isClosedBrace(current, BraceExprToken.Kind.BLOCK))
                        unexpectedToken(current);
                }

                if (doBreak)
                    break;
            }

            if (current instanceof EchoRawToken){
                tokens.add(current);
                break;
            } else if (current instanceof OpenEchoTagToken){
                OpenEchoTagToken result = (OpenEchoTagToken)current;
                result.setValue(
                        analyzer.generator(SimpleExprGenerator.class).getToken(nextToken(iterator), iterator)
                );
                tokens.add(current);
                break;
            } else if (isOpenedBrace(current, BraceExprToken.Kind.BLOCK)){
                BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                        current, iterator, EndforStmtToken.class
                );
                tokens.add(body);
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
            } else if (current instanceof ForeachStmtToken){
                processForeach((ForeachStmtToken) current, iterator);
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
            } else if (current instanceof GlobalStmtToken){
                processGlobal((GlobalStmtToken) current, iterator);
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
            } else if (current instanceof FunctionStmtToken){
                FunctionStmtToken token = analyzer.generator(FunctionGenerator.class)
                        .getToken(current, iterator, true);
                token.setStatic(false);
                if (token.getName() == null){
                    ClosureStmtToken closure = new ClosureStmtToken(token.getMeta());
                    closure.setFunction(token);
                    tokens.add(closure);
                    analyzer.registerClosure(closure);

                    List<Token> tmp = processSimpleExpr(current, iterator);
                    tokens.addAll(tmp);
                } else {
                    tokens.add(token);
                    analyzer.registerFunction(token);
                }
                break;
            } else if (current instanceof ExprToken /*|| current instanceof FunctionStmtToken*/){
                if (current instanceof StaticExprToken){
                    Token result = processStatic((StaticExprToken) current, iterator);
                    if (result != null){
                        tokens.add(result);
                        break;
                    }
                }

                if (isClosedBrace(current, BraceExprToken.Kind.BLOCK)){
                    if (endTokens != null && !isTokenClass(current, endTokens))
                        unexpectedToken(current);

                    break;
                }

                List<Token> tmp = processSimpleExpr(current, iterator);
                tokens.addAll(tmp);
                break;
            } else if (current instanceof ClassStmtToken){
                unexpectedToken(current);
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
    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator) {
        return getToken(current, iterator, (Class[])null);
    }
}
