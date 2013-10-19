package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

abstract public class ValueExprToken extends ExprToken {
    public ValueExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
