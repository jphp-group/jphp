package ru.regenix.jphp.syntax.generators;


import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.ParseException;
import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;
import ru.regenix.jphp.lexer.tokens.SemicolonExprToken;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;
import ru.regenix.jphp.lexer.tokens.expr.operator.AssignExprToken;
import ru.regenix.jphp.lexer.tokens.stmt.ConstStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class ConstGenerator extends Generator<ConstStmtToken> {

    @SuppressWarnings("unchecked")
    public final static Class<? extends Token>[] valueTokens = new Class[]{
            ValueExprToken.class, OperatorExprToken.class
    };

    public ConstGenerator(SyntaxAnalyzer analyzer) {
        super(analyzer);
    }

    @SuppressWarnings("unchecked")
    protected void processBody(ConstStmtToken result, ListIterator<Token> iterator){
        checkUnexpectedEnd(iterator);

        Token current = iterator.next();
        if (!(current instanceof AssignExprToken))
            throw new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(current.getType(), TokenType.T_J_EQUAL),
                current.toTraceInfo(analyzer.getFile())
            );

        List<Token> exprTokens = new ArrayList<Token>();

        while (iterator.hasNext()){
            current = iterator.next();
            if (isTokenClass(current, valueTokens)){
                exprTokens.add(current);
                continue;
            }

            if (current instanceof SemicolonExprToken){
                if (exprTokens.isEmpty())
                    throw new ParseException(
                        Messages.ERR_PARSE_UNEXPECTED_X.fetch(current.getWord()),
                        current.toTraceInfo(analyzer.getFile())
                    );

                break;
            }

            throw new ParseException(
                Messages.ERR_PARSE_UNEXPECTED_X.fetch(current.getType()),
                current.toTraceInfo(analyzer.getFile())
            );
        }

        result.setValue(new ExprStmtToken(exprTokens));
    }

    @Override
    public ConstStmtToken getToken(Token current, ListIterator<Token> iterator) {
        checkUnexpectedEnd(iterator);

        if (current instanceof ConstStmtToken){
            ConstStmtToken result = (ConstStmtToken)current;
            Token next = iterator.next();
            if (next instanceof NameToken){
                result.setName((NameToken)next);
                processBody(result, iterator);
                return result;
            } else
                throw new ParseException(
                        Messages.ERR_PARSE_UNEXPECTED_X_EXPECTED_Y.fetch(next.getType(), TokenType.T_STRING),
                        next.toTraceInfo(analyzer.getFile())
                );
        }
        return null;
    }
}
