package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class IncludeOnceExprToken extends ImportExprToken {
    public IncludeOnceExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INCLUDE_ONCE);
    }
}
