package org.develnext.jphp.core.syntax.generators;

import org.develnext.jphp.core.compiler.common.ASMExpression;
import org.develnext.jphp.core.tokenizer.token.expr.*;
import php.runtime.common.Messages;
import org.develnext.jphp.core.common.Separator;
import php.runtime.exceptions.FatalException;
import org.develnext.jphp.core.syntax.SyntaxAnalyzer;
import org.develnext.jphp.core.syntax.generators.manually.BodyGenerator;
import org.develnext.jphp.core.syntax.generators.manually.SimpleExprGenerator;
import org.develnext.jphp.core.tokenizer.token.*;
import org.develnext.jphp.core.tokenizer.token.expr.operator.*;
import org.develnext.jphp.core.tokenizer.token.expr.value.*;
import org.develnext.jphp.core.tokenizer.token.stmt.*;
import php.runtime.exceptions.support.ErrorType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import static org.develnext.jphp.core.tokenizer.token.expr.BraceExprToken.Kind.ARRAY;

public class ExprGenerator extends Generator<ExprStmtToken> {

    public ExprGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    public ExprStmtToken getInBraces(BraceExprToken.Kind kind, ListIterator<Token> iterator) {
        return getInBraces(kind, iterator, false);
    }

    public ExprStmtToken getInBraces(BraceExprToken.Kind kind, ListIterator<Token> iterator, boolean alreadyOpened) {
        Token next;

        if (!alreadyOpened) {
            next = nextToken(iterator);
            if (!isOpenedBrace(next, kind))
                unexpectedToken(next, BraceExprToken.Kind.toOpen(kind));
        }

        ExprStmtToken result = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, false, kind);

        if (!isClosedBrace(next = nextToken(iterator), kind))
            unexpectedToken(next, BraceExprToken.Kind.toClose(kind));

        return result;
    }

    protected void processCase(CaseStmtToken result, ListIterator<Token> iterator){
        analyzer.addScope();

        if (!(result instanceof DefaultStmtToken)){
            Token next = nextToken(iterator);
            ExprStmtToken conditional = analyzer.generator(SimpleExprGenerator.class).getToken(
                        next, iterator, Separator.COLON, null
            );
            if (conditional == null)
                unexpectedToken(next);
            result.setConditional(conditional);
        } else {
            Token next = nextToken(iterator);
            if (!(isBreak(next) || next instanceof ColonToken))
                unexpectedToken(next);
        }

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
            nextToken(iterator), iterator, true,
            EndswitchStmtToken.class, CaseStmtToken.class, DefaultStmtToken.class, BraceExprToken.class
        );
        result.setBody(body);

        result.setLocals(analyzer.removeScope().getVariables());
    }

    protected void processSwitch(SwitchStmtToken result, ListIterator<Token> iterator){
        analyzer.addScope(false).setLevelForGoto(true);

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
        result.setLocal(analyzer.removeScope().getVariables());
    }

    protected void processIf(IfStmtToken result, ListIterator<Token> iterator){
        analyzer.addScope(false);

        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        if (condition == null)
            unexpectedToken(iterator);

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndifStmtToken.class, ElseIfStmtToken.class
        );
        if (iterator.hasNext()){
            Token next = nextToken(iterator);
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

        result.setLocal(analyzer.removeScope().getVariables());
    }

    protected void processForeach(ForeachStmtToken result, ListIterator<Token> iterator){
        analyzer.addScope(false).setLevelForGoto(true);

        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, "(");

        ExprStmtToken expr = analyzer.generator(SimpleExprGenerator.class).getToken(nextToken(iterator), iterator, Separator.AS, null);

        if (expr == null) {
            unexpectedToken(iterator.previous());
        }

        result.setIterator(expr);

        next = nextToken(iterator);
        boolean reference = false;
        if (next instanceof AmpersandRefToken){
            reference = true;
            next = nextToken(iterator);
        }

        if (next instanceof ListExprToken || isOpenedBrace(next, ARRAY)) {
            ListExprToken listExpr = analyzer.generator(SimpleExprGenerator.class).processSingleList(next, iterator);
            result.setValue(new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), listExpr));
        } else if (next instanceof VariableExprToken && nextTokenAndPrev(iterator) instanceof KeyValueExprToken){
            iterator.next();
            result.setKey((VariableExprToken)next);
            result.setKeyReference(reference);

            next = nextToken(iterator);
            reference = false;
            if (next instanceof AmpersandRefToken){
                reference = true;
                next = nextToken(iterator);
            }

            if (next instanceof ListExprToken) {
                ListExprToken listExpr = analyzer.generator(SimpleExprGenerator.class)
                        .processSingleList(next, iterator);

                result.setValue(new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), listExpr));
            } else {
                ExprStmtToken eValue = analyzer.generator(SimpleExprGenerator.class)
                        .getToken(next, iterator, null, BraceExprToken.Kind.SIMPLE);

                if (eValue == null) {
                    unexpectedToken(next);
                    return;
                }

                Token single = eValue.getLast();

                if (!(single instanceof VariableExprToken
                        || single instanceof ArrayGetExprToken
                        || single instanceof DynamicAccessExprToken
                        || (single instanceof StaticAccessExprToken && ((StaticAccessExprToken) single).isGetStaticField()))) {
                    unexpectedToken(single);
                }

                result.setValue(eValue);
                result.setValueReference(reference);
            }

        } else {
            //next = iterator.previous();
            ExprStmtToken eValue = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(next, iterator, null, BraceExprToken.Kind.SIMPLE);

            if (eValue == null) {
                unexpectedToken(next);
                return;
            }

            Token single = eValue.getLast();

            if (!(single instanceof VariableExprToken
                    || single instanceof ArrayGetExprToken
                    || single instanceof DynamicAccessExprToken
                    || (single instanceof StaticAccessExprToken && ((StaticAccessExprToken) single).isGetStaticField())))
                unexpectedToken(single);

            result.setValue(eValue);
            result.setValueReference(reference);
        }
            //unexpectedToken(next, "$var");

        if (result.getValue().isSingle() && result.getValue().getSingle() instanceof ListExprToken) {
            ListExprToken listExprToken = (ListExprToken)result.getValue().getSingle();
            if (listExprToken.getVariables().isEmpty()) {
                analyzer.getEnvironment().error(
                        listExprToken.toTraceInfo(analyzer.getContext()), ErrorType.E_ERROR,
                        Messages.ERR_CANNOT_USE_EMPTY_LIST
                );
            }
        }

        next = nextToken(iterator);
        if (!isClosedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next, ")");

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndforeachStmtToken.class
        );
        if (body != null && body.isAlternativeSyntax())
            iterator.next();

        result.setBody(body);

        Token last = result.getValue().getLast();
        if (analyzer.getFunction() != null) {
            if (result.getKey() != null){
                analyzer.getFunction().variable(result.getKey())
                        .setReference(true)
                        .setUnstable(true);
            }

            if (last instanceof VariableExprToken){
                VariableExprToken variable = (VariableExprToken)last;
                analyzer.getFunction().variable(variable)
                        .setReference(true)
                        .setUnstable(true);
            }
        }

        if (last instanceof ArrayGetExprToken){
            result.getValue().getTokens().set(
                    result.getValue().getTokens().size() - 1, new ArrayGetRefExprToken((ArrayGetExprToken)last)
            );
            result.getValue().updateAsmExpr(analyzer.getEnvironment(), analyzer.getContext());
        }

        if (result.getKey() != null)
            analyzer.getScope().addVariable(result.getKey());

        result.setLocal(analyzer.removeScope().getVariables());
    }

    protected void processFor(ForStmtToken result, ListIterator<Token> iterator){
        analyzer.addScope();

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

        result.setInitLocal(analyzer.removeScope().getVariables());
        analyzer.addScope().setLevelForGoto(true);

        // CONDITIONS
        List<ExprStmtToken> conditions = new ArrayList<ExprStmtToken>();

        do {
            ExprStmtToken conditionExpr = analyzer.generator(SimpleExprGenerator.class)
                .getToken(nextToken(iterator), iterator, Separator.COMMA_OR_SEMICOLON, null);
            if (conditionExpr == null)
                break;

            conditions.add(conditionExpr);

            if (iterator.previous() instanceof SemicolonToken) {
                iterator.next();
                break;
            }
            iterator.next();
        } while (true);

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

        result.setIterationLocal(new HashSet<VariableExprToken>(analyzer.getScope().getVariables()));

        nextAndExpected(iterator, BraceExprToken.class);
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
            nextToken(iterator), iterator, EndforStmtToken.class
        );
        if (body != null && body.isAlternativeSyntax())
            iterator.next();

        result.setInitExpr(inits);
        result.setConditionExpr(conditions);
        result.setIterationExpr(iterations);
        result.setBody(body);
        result.setLocal(analyzer.removeScope().getVariables());
    }

    protected void processWhile(WhileStmtToken result, ListIterator<Token> iterator){
        analyzer.addScope().setLevelForGoto(true);

        ExprStmtToken condition = getInBraces(BraceExprToken.Kind.SIMPLE, iterator);
        if (condition == null)
            unexpectedToken(iterator);

        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(
                nextToken(iterator), iterator, EndwhileStmtToken.class
        );
        if (body != null && body.isAlternativeSyntax())
            iterator.next();

        result.setCondition(condition);
        result.setBody(body);

        result.setLocal(analyzer.removeScope().getVariables());
    }

    protected void processReturn(ReturnStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);

        if (next instanceof SemicolonToken) {
            result.setValue(null);
            result.setEmpty(true);
        } else {
            result.setEmpty(false);
            ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(next, iterator);
            result.setValue(value);
        }
    }

    protected void processDo(DoStmtToken result, ListIterator<Token> iterator){
        analyzer.addScope().setLevelForGoto(true);
        BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(nextToken(iterator), iterator);
        result.setBody(body);

        Token next = nextToken(iterator);
        if (next instanceof WhileStmtToken){
            result.setCondition(getInBraces(BraceExprToken.Kind.SIMPLE, iterator));
            if (result.getCondition() == null)
                unexpectedToken(iterator);

            next = nextToken(iterator);
            result.setLocal(analyzer.removeScope().getVariables());
            if (next instanceof SemicolonToken)
                return;
        }
        unexpectedToken(next);
    }

    protected void processGlobal(GlobalStmtToken result, ListIterator<Token> iterator){
        List<ValueExprToken> variables = new ArrayList<ValueExprToken>();
        Token next = nextToken(iterator);
        Token prev = null;
        do {
            if (next instanceof VariableExprToken) {
                VariableExprToken variable = (VariableExprToken)next;
                analyzer.getScope().addVariable(variable);
                if (analyzer.getFunction() != null) {
                    analyzer.getFunction().variable(variable)
                            .setReference(true)
                            .setUnstable(true);
                }

                variables.add(variable);
            } else if (next instanceof DollarExprToken) {
                GetVarExprToken var = analyzer.generator(SimpleExprGenerator.class)
                        .processVarVar(next, nextTokenAndPrev(iterator), iterator);

                variables.add(var);
                next = var;
            } else if (next instanceof CommaToken){
                if (!(prev instanceof VariableValueExprToken))
                    unexpectedToken(next);
            } else if (next instanceof SemicolonToken){
                if (!(prev instanceof VariableValueExprToken))
                    unexpectedToken(next);
                break;
            } else
                unexpectedToken(next);

            prev = next;
            next = nextToken(iterator);
        } while (true);
        result.setVariables(variables);
    }

    protected List<StaticStmtToken> processStatic(StaticExprToken token, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (!(next instanceof VariableExprToken)){
            iterator.previous();
            return null;
        }

        List<StaticStmtToken> list = new ArrayList<StaticStmtToken>();
        while (next instanceof VariableExprToken){
            VariableExprToken variable = (VariableExprToken)next;

            analyzer.getScope().addVariable(variable);
            if (analyzer.getFunction() != null){
                analyzer.getFunction().variable(variable)
                        .setReference(true)
                        .setUnstable(true);
                analyzer.getFunction().getStaticLocal().add(variable);
            }

            StaticStmtToken result = new StaticStmtToken(variable.getMeta());
            result.setVariable((VariableExprToken)next);
            next = nextToken(iterator);
            if (next instanceof AssignExprToken){
                ExprStmtToken initValue = analyzer.generator(SimpleExprGenerator.class).getNextExpression(
                        nextToken(iterator), iterator, Separator.COMMA_OR_SEMICOLON, null
                );
                if (nextTokenAndPrev(iterator) instanceof CommaToken)
                    iterator.next();

                result.setInitValue(initValue);
                list.add(result);
            } else if (isBreak(next)){
                result.setInitValue(null);
                list.add(result);
                break;
            } else if (next instanceof CommaToken) {
                result.setInitValue(null);
                list.add(result);
            } else
                unexpectedToken(next);

            next = nextToken(iterator);
        }

        //unexpectedToken(next); TODO: check it!
        return list;
    }

    protected void processGoto(GotoStmtToken result, ListIterator<Token> iterator) {
        NameToken token = nextAndExpected(iterator, NameToken.class);
        if (token.getClass() != NameToken.class)
            unexpectedToken(token);

        nextAndExpected(iterator, SemicolonToken.class);

        result.setLevel(analyzer.getScope().getGotoLevel());
        result.setLabel(token);
    }

    protected void processJump(JumpStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        long level = 1;
        if (next instanceof IntegerExprToken){
            level = ((IntegerExprToken) next).getValue();
            if (level < 1) {
                throw new FatalException(
                        Messages.ERR_OPERATOR_ACCEPTS_ONLY_POSITIVE.fetch(result.getWord()),
                        result.toTraceInfo(analyzer.getContext())
                );
            }

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

            Token prev = iterator.previous();
            if (isBreak(prev)){
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
            } else if (current instanceof ThrowStmtToken){
                tokens.add(analyzer.generator(ThrowGenerator.class).getToken(current, iterator));
                break;
            } else if (current instanceof TryStmtToken){
                tokens.add(analyzer.generator(TryCatchGenerator.class).getToken(current, iterator));
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
            } else if (current instanceof GotoStmtToken){
                processGoto((GotoStmtToken)current, iterator);
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
                    closure.setOwnerClass(analyzer.getClazz());
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
                    List<StaticStmtToken> result = processStatic((StaticExprToken) current, iterator);
                    if (result != null){
                        tokens.addAll(result);
                        break;
                    }
                }

                // check label
                if (current.getClass() == NameToken.class
                        && iterator.hasNext() && nextTokenAndPrev(iterator) instanceof ColonToken) {

                    LabelStmtToken labelStmtToken = new LabelStmtToken(current.getMeta());
                    tokens.add(labelStmtToken);
                    iterator.next();

                    analyzer.getScope().addLabel(labelStmtToken);
                    break;
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
            } else if (current instanceof CommentToken) {
                tokens.add(current);
                break;
                // nop
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

        ExprStmtToken stmtToken = new ExprStmtToken(analyzer.getEnvironment(), analyzer.getContext(), tokens);
        return stmtToken;
    }

    @Override
    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator) {
        return getToken(current, iterator, (Class[])null);
    }
}
