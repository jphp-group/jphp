package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class IncludeExprToken extends ImportExprToken {
    public IncludeExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INCLUDE);
    }

    @Override
    public String getCode() {
        return "include";
    }
}
