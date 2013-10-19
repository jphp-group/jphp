package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

public class StaticExprToken extends ValueExprToken {
    public StaticExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STATIC);
    }
}
