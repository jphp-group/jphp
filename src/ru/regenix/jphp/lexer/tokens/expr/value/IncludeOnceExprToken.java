package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class IncludeOnceExprToken extends ImportExprToken {
    public IncludeOnceExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INCLUDE_ONCE);
    }
}
