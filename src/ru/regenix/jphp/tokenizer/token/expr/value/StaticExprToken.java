package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

public class StaticExprToken extends ValueExprToken {
    public StaticExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STATIC);
    }
}
