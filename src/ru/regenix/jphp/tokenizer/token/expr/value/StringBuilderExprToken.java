package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.Token;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

import java.util.List;

public class StringBuilderExprToken extends ValueExprToken {
    protected List<Token> expression;

    public StringBuilderExprToken(TokenMeta meta, List<Token> expression) {
        super(meta, TokenType.T_CONSTANT_ENCAPSED_STRING);
        this.expression = expression;
    }

    public List<Token> getExpression() {
        return expression;
    }
}
