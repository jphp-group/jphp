package ru.regenix.jphp.syntax.generators.manually;


import ru.regenix.jphp.common.Separator;
import ru.regenix.jphp.tokenizer.token.*;
import ru.regenix.jphp.tokenizer.token.expr.*;
import ru.regenix.jphp.tokenizer.token.expr.operator.*;
import ru.regenix.jphp.tokenizer.token.expr.value.*;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
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
        result.setName((ValueExprToken)previous);
        result.setParameters(parameters);

        if (analyzer.getFunction() != null){
            analyzer.getFunction().setCallsExist(true);
        }

        return result;
    }

    protected ImportExprToken processImport(Token current, Token next, ListIterator<Token> iterator,
                                              BraceExprToken.Kind closedBrace){
        ImportExprToken result = (ImportExprToken)current;
        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getToken(
                nextToken(iterator), iterator, Separator.SEMICOLON, closedBrace
        );
        if (closedBrace == null)
            iterator.previous();
        result.setValue(value);
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

    protected Token processSimpleToken(Token current, Token previous, Token next, ListIterator<Token> iterator,
                                       BraceExprToken.Kind closedBraceKind){
        if (current instanceof ImportExprToken)
            return processImport(current, next, iterator, closedBraceKind);

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

            if (!(previous instanceof ValueExprToken || previous instanceof ArrayGetExprToken
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
            ExprStmtToken result = analyzer.generator(SimpleExprGenerator.class)
                    .getToken(nextToken(iterator), iterator, Separator.SEMICOLON, closedBraceKind);
            if (closedBraceKind == null)
                iterator.previous();

            logic.setRightValue(result);
            return logic;
        }

        if (next instanceof StaticAccessExprToken){
            if (current instanceof NameToken || current instanceof VariableExprToken
                    || current instanceof SelfExprToken){
                StaticAccessExprToken result = (StaticAccessExprToken)next;
                result.setClazz((ValueExprToken)current);
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
        if (previous instanceof VariableExprToken)
            if (analyzer.getFunction() != null){
                analyzer.getFunction().getArrayAccessLocal().add((VariableExprToken)previous);
            }

        Token next = nextToken(iterator);
        if (isClosedBrace(next, BraceExprToken.Kind.ARRAY)){
            return new ArrayPushExprToken(TokenMeta.of(current, next));
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

        if (iterator.hasNext()){
            next = iterator.next();
            if (next instanceof AssignOperatorExprToken || next instanceof AssignExprToken){
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
                        ArrayGetExprToken.class)){
                    // array
                    tokens.add(current = processArrayToken(previous, current, iterator));
                }
            } else if (current instanceof CommaToken){
                if (separator == Separator.COMMA || separator == Separator.COMMA_OR_SEMICOLON){
                    break;
                } else {
                    unexpectedToken(current);
                }
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
                Token token = processSimpleToken(current, previous, next, iterator, closedBraceKind);
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
