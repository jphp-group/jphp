package ru.regenix.jphp.syntax.generators;

import ru.regenix.jphp.lexer.tokens.expr.AmpersandToken;
import ru.regenix.jphp.lexer.tokens.expr.CommaToken;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.BraceExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.AssignExprToken;
import ru.regenix.jphp.lexer.tokens.expr.value.VariableExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ArgumentStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.BodyStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.FunctionStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.BodyGenerator;
import ru.regenix.jphp.syntax.generators.manually.ConstExprGenerator;

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

        if (next instanceof AmpersandToken){
            isReference = true;
            next = nextToken(iterator);
        }

        if (next instanceof VariableExprToken){
            variable = (VariableExprToken)next;
        } else
            unexpectedToken(next);

        next = nextToken(iterator);
        if (next instanceof AssignExprToken){
            value = (ExprStmtToken) analyzer.generateToken(next, iterator, ConstExprGenerator.class);
        } else {
            if (next instanceof CommaToken || isClosedBrace(next, BraceExprToken.Kind.SIMPLE)){
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
        BodyStmtToken body = (BodyStmtToken) analyzer.generateToken(next, iterator, BodyGenerator.class);
        result.setBody(body);
    }

    @Override
    public FunctionStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof FunctionStmtToken){
            FunctionStmtToken result = (FunctionStmtToken)current;
            checkUnexpectedEnd(iterator);

            Token next = iterator.next();
            if (next instanceof NameToken){
                BraceExprToken brace = nextAndExpected(iterator, BraceExprToken.class);
                if (!brace.isSimpleOpened())
                    unexpectedToken(brace, "(");

                processArguments(result, iterator);
                processBody(result, iterator);

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
