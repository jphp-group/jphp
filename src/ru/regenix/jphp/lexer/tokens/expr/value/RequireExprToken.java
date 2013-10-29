package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class RequireExprToken extends ImportExprToken {
    public RequireExprToken(TokenMeta meta) {
        super(meta, TokenType.T_REQUIRE);
    }
}
