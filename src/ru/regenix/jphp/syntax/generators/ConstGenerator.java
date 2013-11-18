package ru.regenix.jphp.syntax.generators;


import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.AssignExprToken;
import ru.regenix.jphp.tokenizer.token.stmt.ConstStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.ConstExprGenerator;

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
            unexpectedToken(current, TokenType.T_J_EQUAL);

        ExprStmtToken value = analyzer.generator(ConstExprGenerator.class).getToken(nextToken(iterator), iterator);
        result.setValue(value);
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
                unexpectedToken(next, TokenType.T_STRING);
        }
        return null;
    }
}
