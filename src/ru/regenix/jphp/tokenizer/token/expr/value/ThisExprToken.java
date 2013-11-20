package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

public class ThisExprToken extends ValueExprToken {
    public ThisExprToken(TokenMeta meta) {
        super(meta, TokenType.T_VARIABLE);
    }
}
