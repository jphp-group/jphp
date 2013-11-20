package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class RequireExprToken extends ImportExprToken {
    public RequireExprToken(TokenMeta meta) {
        super(meta, TokenType.T_REQUIRE);
    }
}
