package ru.regenix.jphp.syntax.generators.manually;


import ru.regenix.jphp.common.Separator;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.*;
import ru.regenix.jphp.tokenizer.token.expr.*;
import ru.regenix.jphp.tokenizer.token.expr.operator.*;
import ru.regenix.jphp.tokenizer.token.expr.value.*;
import ru.regenix.jphp.tokenizer.token.stmt.AsStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.ExprGenerator;
import ru.regenix.jphp.syntax.generators.Generator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class SimpleExprGenerator extends Generator<ExprStmtToken> {

    private boolean isRef = false;

    public SimpleExprGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @Override
    public boolean isSingleton() {
        return false;
    }

    protected CallExprToken processCall(Token previous, Token current, ListIterator<Token> iterator){
        ExprStmtToken param;

        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
        do {
            param = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE);

            if (param != null) {
                parameters.add(param);
                if (param.isSingle()){
                    if (param.getTokens().get(0) instanceof VariableExprToken) {
                        if (analyzer.getFunction() != null)
                            analyzer.getFunction().getPassedLocal().add((VariableExprToken)param.getTokens().get(0));
                    }
                }
            }

        } while (param != null);
        nextToken(iterator);

        CallExprToken result = new CallExprToken(TokenMeta.of(previous, current));
        if (previous instanceof ValueExprToken)
            result.setName(analyzer.getRealName((ValueExprToken)previous));
        else
            result.setName((ExprToken)previous);

        result.setParameters(parameters);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setCallsExist(true);
        }

        return result;
    }

    protected ImportExprToken processImport(Token current, Token next, ListIterator<Token> iterator,
                                              BraceExprToken.Kind closedBrace, int braceOpened){
        ImportExprToken result = (ImportExprToken)current;
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getToken(
                nextToken(iterator), iterator, Separator.SEMICOLON, closedBrace
        );
        if (closedBrace == null || braceOpened < 1)
            iterator.previous();
        result.setValue(value);
        return result;
    }

    protected DynamicAccessExprToken processDynamicAccess(Token current, Token next, ListIterator<Token> iterator,
            BraceExprToken.Kind closedBraceKind, int braceOpened){
        DynamicAccessExprToken result = (DynamicAccessExprToken)current;
        if (next instanceof NameToken){
            result.setField((ValueExprToken) next);
            iterator.next();
        } else if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){
            ExprStmtToken name = analyzer.generator(ExprGenerator.class).getInBraces(
                    BraceExprToken.Kind.BLOCK, iterator
            );
            result.setFieldExpr(name);
        }

        if (iterator.hasNext()){
            next = iterator.next();
            if (next instanceof AssignableOperatorToken){
                DynamicAccessAssignExprToken dResult = new DynamicAccessAssignExprToken(result);
                dResult.setAssignOperator(next);

                ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getToken(
                        nextToken(iterator), iterator, Separator.SEMICOLON, closedBraceKind
                );
                if (closedBraceKind == null || braceOpened < 1)
                    iterator.previous();
                dResult.setValue(value);
                return dResult;
            }
            iterator.previous();
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
            iterator.previous();
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

    protected Token processValueIfElse(ValueIfElseToken current, Token next, ListIterator<Token> iterator,
                                       BraceExprToken.Kind closedBrace, int braceOpened){
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getToken(
                nextToken(iterator), iterator, Separator.COLON, closedBrace
        );
        /*if (closedBrace == null || braceOpened < 1)
            iterator.previous();*/
        current.setValue(value);

        if (!((next = iterator.previous()) instanceof ColonToken))
            unexpectedToken(next, ":");

        iterator.next();
        ExprStmtToken alternative = analyzer.generator(SimpleExprGenerator.class).getToken(
                    nextToken(iterator), iterator, Separator.SEMICOLON, closedBrace
        );
        if (closedBrace == null || braceOpened < 1)
           iterator.previous();

        if (alternative == null)
            unexpectedToken(iterator.next());

        current.setAlternative(alternative);
        return current;
    }

    protected Token processNew(Token current, ListIterator<Token> iterator){
        NewExprToken result = (NewExprToken)current;
        Token next = nextToken(iterator);
        if (next instanceof NameToken){
            FulledNameToken nameToken = analyzer.getRealName((NameToken)next);
            result.setName(nameToken);
        } else if (next instanceof VariableExprToken){
            result.setName((VariableExprToken)next);
        } else
            unexpectedToken(next);

        next = nextToken(iterator);
        if (isOpenedBrace(next, BraceExprToken.Kind.SIMPLE)){
            ExprStmtToken param;
            List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
            do {
                param = analyzer.generator(SimpleExprGenerator.class)
                        .getToken(nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE);

                if (param != null)
                    parameters.add(param);
            } while (param != null);
            nextToken(iterator);
            result.setParameters(parameters);
        } else {
            result.setParameters(new ArrayList<ExprStmtToken>());
            iterator.previous();
        }

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setCallsExist(true);
        }

        return result;
    }

    protected Token processSimpleToken(Token current, Token previous, Token next, ListIterator<Token> iterator,
                                       BraceExprToken.Kind closedBraceKind, int braceOpened){
        if (current instanceof DynamicAccessExprToken){
            return processDynamicAccess(current, next, iterator, closedBraceKind, braceOpened);
        }

        if (current instanceof OperatorExprToken)
            isRef = false;

        if (current instanceof ImportExprToken)
            return processImport(current, next, iterator, closedBraceKind, braceOpened);

        if (current instanceof NewExprToken)
            return processNew(current, iterator);

        if (current instanceof DollarExprToken){
            return processVarVar(current, next, iterator);
        }

        if (current instanceof VariableExprToken){
            analyzer.getLocalScope().add((VariableExprToken) current);
            if (analyzer.getFunction() != null)
                analyzer.getFunction().setVarsExist(true);
        }

        if (current instanceof ValueIfElseToken){
            return processValueIfElse((ValueIfElseToken)current, next, iterator, closedBraceKind, braceOpened);
        }

        // &
        if (current instanceof AmpersandRefToken){
            isRef = true;
            if (next instanceof VariableExprToken)
                if (analyzer.getFunction() != null)
                    analyzer.getFunction().getRefLocal().add((VariableExprToken)next);

            if (previous instanceof AssignExprToken || previous == null) {
                iterator.previous();
                Token token = iterator.previous(); // =
                if (iterator.hasPrevious()) {
                    token = iterator.previous();
                    if (token instanceof VariableExprToken && analyzer.getFunction() != null){
                        analyzer.getFunction().getRefLocal().add((VariableExprToken)token);
                       // analyzer.getFunction().getUnstableLocal().add((VariableExprToken)token); TODO: check is needed?
                    }

                    iterator.next();
                }
                iterator.next();
                iterator.next();
                if (!(next instanceof ValueExprToken))
                    unexpectedToken(token);

            } else {
                if (next instanceof ValueExprToken)
                    return new AndExprToken(current.getMeta());
                else
                    unexpectedToken(current);
            }

            return current;
        }
        // &$var, &$obj->prop['x'], &class::$prop, &$arr['x'], &call()->x;
        if (previous instanceof AmpersandRefToken){
            if (current instanceof VariableExprToken)
                if (analyzer.getFunction() != null)
                    analyzer.getFunction().getRefLocal().add((VariableExprToken)current);
        }

        if ((current instanceof MinusExprToken || current instanceof PlusExprToken)
                && (next instanceof IntegerExprToken || next instanceof DoubleExprToken
                        || next instanceof HexExprValue)){

            if (!(previous instanceof ValueExprToken
                    || previous instanceof ArrayGetExprToken || previous instanceof DynamicAccessExprToken
                    || isOpenedBrace(previous, BraceExprToken.Kind.SIMPLE))){
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
            ExprStmtToken result = analyzer.generator(SimpleExprGenerator.class).getToken(
                            nextToken(iterator), iterator, Separator.SEMICOLON,
                            braceOpened > 0 ? BraceExprToken.Kind.SIMPLE : closedBraceKind
                    );

            if (closedBraceKind == null && braceOpened < 1)
                iterator.previous();

            logic.setRightValue(result);
            return logic;
        }

        if (next instanceof StaticAccessExprToken){
            if (current instanceof NameToken || current instanceof VariableExprToken
                    || current instanceof SelfExprToken){
                StaticAccessExprToken result = (StaticAccessExprToken)next;
                ValueExprToken clazz = (ValueExprToken)current;
                if (clazz instanceof NameToken){
                    clazz = analyzer.getRealName((NameToken)clazz);
                }

                result.setClazz(clazz);
                nextToken(iterator);

                next = nextToken(iterator);
                if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){
                    ExprStmtToken expr = getToken(nextToken(iterator), iterator, false, BraceExprToken.Kind.BLOCK);
                    result.setFieldExpr(expr);
                    nextAndExpected(iterator, BraceExprToken.class);
                } else if (next instanceof NameToken || next instanceof VariableExprToken){
                    result.setField((ValueExprToken)next);
                } else
                    unexpectedToken(next);

                return result;
            } else
                unexpectedToken(current);
        }

        return null;
    }

    protected Token processNewArray(Token current, ListIterator<Token> iterator){
        ArrayExprToken result = new ArrayExprToken(current.getMeta());
        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
        Token next = nextToken(iterator);
        if (!isOpenedBrace(next, BraceExprToken.Kind.SIMPLE))
            unexpectedToken(next);

        do {
            ExprStmtToken argument = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE);
            if (argument == null)
                break;

            parameters.add(argument);
        } while (true);
        nextToken(iterator); // skip )

        result.setParameters(parameters);
        return result;
    }

    protected Token processArrayToken(Token previous, Token current, ListIterator<Token> iterator){
        if (previous instanceof VariableExprToken) {
            if (analyzer.getFunction() != null){
                analyzer.getFunction().getArrayAccessLocal().add((VariableExprToken)previous);
            }
        }

        Token next = nextToken(iterator);
        if (isClosedBrace(next, BraceExprToken.Kind.ARRAY)){
            if (nextToken(iterator) instanceof AssignableOperatorToken) {
                iterator.previous();
                return new ArrayPushExprToken(TokenMeta.of(current, next));
            } else
                unexpectedToken(next);
        } else
            iterator.previous();

        ExprStmtToken param;
        List<ExprStmtToken> parameters = new ArrayList<ExprStmtToken>();
        do {
            param = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, false, BraceExprToken.Kind.ARRAY);

            if (param != null)
                parameters.add(param);

        } while (param != null);
        nextToken(iterator); // skip ]

        ArrayGetExprToken result;
        result = new ArrayGetExprToken(current.getMeta());
        result.setParameters(parameters);

        if (isRef){
            result = new ArrayGetRefExprToken(result);
        } else if (iterator.hasNext()){
            next = iterator.next();
            if (next instanceof AssignableOperatorToken){
                result = new ArrayGetRefExprToken(result);
            }
            iterator.previous();
        }

        return result;
    }

    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  boolean commaSeparator, BraceExprToken.Kind closedBraceKind) {
        return getToken(current, iterator, commaSeparator ? Separator.COMMA : Separator.SEMICOLON, closedBraceKind);
    }

    @SuppressWarnings("unchecked")
    public ExprStmtToken getToken(Token current, ListIterator<Token> iterator,
                                  Separator separator, BraceExprToken.Kind closedBraceKind) {
        isRef = false;

        List<Token> tokens = new ArrayList<Token>();
        Token previous = null;
        Token next = iterator.hasNext() ? iterator.next() : null;
        if (next != null)
            iterator.previous();

        int braceOpened = 0;
        do {
            if (isOpenedBrace(current, BraceExprToken.Kind.SIMPLE)){
                boolean isFunc = false;
                if (previous instanceof NameToken || previous instanceof VariableExprToken)
                    isFunc = true;
                else if (previous instanceof StaticAccessExprToken){
                    isFunc = true; // !((StaticAccessExprToken)previous).isGetStaticField(); TODO check it!
                } else if (previous instanceof DynamicAccessExprToken){
                    isFunc = true;
                }

                if (isFunc){
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
                        ArrayGetExprToken.class, DynamicAccessExprToken.class)){
                    // array
                    tokens.add(current = processArrayToken(previous, current, iterator));
                }
            } else if (current instanceof CommaToken){
                if (separator == Separator.COMMA || separator == Separator.COMMA_OR_SEMICOLON){
                    break;
                } else {
                    unexpectedToken(current);
                }
            } else if (current instanceof AsStmtToken){
                if (separator == Separator.AS)
                    break;
                unexpectedToken(current);
            } else if (isClosedBrace(current, closedBraceKind)){
                iterator.previous();
                break;
            } else if (current instanceof BreakToken){
                break;
            } else if (current instanceof ColonToken){
                if (separator == Separator.COLON || separator == Separator.COMMA_OR_SEMICOLON)
                    break;
                unexpectedToken(current);
            } else if (current instanceof SemicolonToken){ // TODO refactor!
                if (separator == Separator.SEMICOLON || separator == Separator.COMMA_OR_SEMICOLON)
                    break;

                if (separator == Separator.COMMA || closedBraceKind != null || tokens.isEmpty())
                    unexpectedToken(current);
                break;
            } else if (current instanceof BraceExprToken){
                unexpectedToken(current);
            } else if (current instanceof ArrayExprToken){
                tokens.add(processNewArray(current, iterator));
            } else if (current instanceof ExprToken) {
                Token token = processSimpleToken(current, previous, next, iterator, closedBraceKind, braceOpened);
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

            if (current == null)
                nextToken(iterator);

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
