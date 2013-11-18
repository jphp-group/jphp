package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

public class RequireOnceExprToken extends ImportExprToken {
    public RequireOnceExprToken(TokenMeta meta) {
        super(meta, TokenType.T_REQUIRE_ONCE);
    }
}
