package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class IncludeExprToken extends ImportExprToken {
    public IncludeExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INCLUDE);
    }
}
