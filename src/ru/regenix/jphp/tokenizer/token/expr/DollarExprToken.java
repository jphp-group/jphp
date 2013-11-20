package ru.regenix.jphp.tokenizer.token.expr;


import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class DollarExprToken extends ExprToken {
    public DollarExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
