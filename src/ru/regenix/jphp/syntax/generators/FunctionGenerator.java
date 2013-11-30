package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;
import ru.regenix.jphp.tokenizer.token.SemicolonToken;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.operator.AmpersandRefToken;
import ru.regenix.jphp.tokenizer.token.expr.BraceExprToken;
import ru.regenix.jphp.tokenizer.token.expr.CommaToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.AssignExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.VariableExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ArgumentStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.BodyStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.FunctionStmtToken;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class FunctionGenerator extends Generator<FunctionStmtToken> {

    public FunctionGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @SuppressWarnings("unchecked")
    protected ArgumentStmtToken processArgument(ListIterator<Token> iterator){
        boolean isReference = false;
        VariableExprToken variable = null;
        ExprStmtToken value = null;

        Token next = nextToken(iterator);
        if (next instanceof CommaToken || isClosedBrace(next, BraceExprToken.Kind.SIMPLE))
            return null;

        if (next instanceof AmpersandRefToken){
            isReference = true;
            next = nextToken(iterator);
        }

        if (next instanceof VariableExprToken){
            variable = (VariableExprToken)next;
        } else
            unexpectedToken(next);

        next = nextToken(iterator);
        if (next instanceof AssignExprToken){
            value = analyzer.generator(SimpleExprGenerator.class).getToken(
                    nextToken(iterator), iterator, true, BraceExprToken.Kind.SIMPLE
            );
        } else {
            if (next instanceof CommaToken || isClosedBrace(next, BraceExprToken.Kind.SIMPLE)){
                if (next instanceof BraceExprToken)
                    iterator.previous();
            } else
                unexpectedToken(next);
        }

        ArgumentStmtToken argument = new ArgumentStmtToken(variable.getMeta());
        argument.setName(variable);
        argument.setReference(isReference);
        argument.setValue(value);

        return argument;
    }

    protected void processArguments(FunctionStmtToken result, ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);
        List<ArgumentStmtToken> arguments = new ArrayList<ArgumentStmtToken>();
        while (iterator.hasNext()){
            ArgumentStmtToken argument = processArgument(iterator);
            if (argument == null)
                break;

            arguments.add(argument);
        }
        result.setArguments(arguments);
    }

    protected void processBody(FunctionStmtToken result, ListIterator<Token> iterator){
        Token next = nextToken(iterator);
        if (isOpenedBrace(next, BraceExprToken.Kind.BLOCK)){
            BodyStmtToken body = analyzer.generator(BodyGenerator.class).getToken(next, iterator);
            result.setBody(body);
        } else if (next instanceof SemicolonToken) {
            result.setInterfacable(true);
            result.setBody(null);
        } else
            unexpectedToken(next);
    }

    @Override
    public FunctionStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof FunctionStmtToken){
            FunctionStmtToken result = (FunctionStmtToken)current;

            Token next = nextToken(iterator);
            if (next instanceof AmpersandRefToken){
                result.setReturnReference(true);
                next = nextToken(iterator);
            }

            if (next instanceof NameToken){
                if (analyzer.getFunction() != null)
                    unexpectedToken(current);

                analyzer.addLocalScope();
                analyzer.setFunction(result);
                BraceExprToken brace = nextAndExpected(iterator, BraceExprToken.class);
                if (!brace.isSimpleOpened())
                    unexpectedToken(brace, "(");

                result.setNamespace(analyzer.getNamespace());
                result.setName((NameToken)next);
                processArguments(result, iterator);
                processBody(result, iterator);

                result.setLocal(analyzer.removeLocalScope());

                analyzer.setFunction(null);
                return result;
            } else if (next instanceof BraceExprToken){
                // Closure
                if (((BraceExprToken) next).isSimpleOpened()){
                    iterator.previous();
                    return null;
                }
            }

            unexpectedToken(next);
        }

        return null;
    }
}
