package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

public class SelfExprToken extends ValueExprToken {
    public SelfExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
    }
}
