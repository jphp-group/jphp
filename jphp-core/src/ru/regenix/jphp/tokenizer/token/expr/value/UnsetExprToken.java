package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class UnsetExprToken extends CallExprToken {
    public UnsetExprToken(TokenMeta meta) {
        super(meta, TokenType.T_UNSET);
    }
}
