package ru.regenix.jphp.syntax.generators;


import ru.regenix.jphp.syntax.SyntaxAnalyzer;
import ru.regenix.jphp.syntax.generators.manually.SimpleExprGenerator;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;
import ru.regenix.jphp.tokenizer.token.expr.operator.AssignExprToken;
import ru.regenix.jphp.tokenizer.token.expr.value.FulledNameToken;
import ru.regenix.jphp.tokenizer.token.expr.value.NameToken;
import ru.regenix.jphp.tokenizer.token.stmt.ConstStmtToken;
import ru.regenix.jphp.tokenizer.token.stmt.ExprStmtToken;

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
        Token current = nextToken(iterator);
        if (!(current instanceof AssignExprToken))
            unexpectedToken(current, "=");

        ExprStmtToken value = analyzer.generator(SimpleExprGenerator.class).getToken(nextToken(iterator), iterator);
        if (value == null)
            unexpectedToken(nextToken(iterator));

        result.setValue(value);
    }

    @Override
    public ConstStmtToken getToken(Token current, ListIterator<Token> iterator) {
        if (current instanceof ConstStmtToken){
            ConstStmtToken result = (ConstStmtToken)current;
            Token next = iterator.next();
            if (next instanceof NameToken){
                if (next instanceof FulledNameToken)
                    unexpectedToken(next, TokenType.T_STRING);

                if (analyzer.getClazz() == null)
                    result.setNamespace(analyzer.getNamespace());

                result.setName((NameToken)next);
                processBody(result, iterator);
                return result;
            } else
                unexpectedToken(next, TokenType.T_STRING);
        }
        return null;
    }
}
