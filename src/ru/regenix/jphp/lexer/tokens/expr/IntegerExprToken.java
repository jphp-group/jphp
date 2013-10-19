package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class IntegerExprToken extends ExprToken {

    private long value;

    public IntegerExprToken(TokenMeta meta) {
        super(meta);
        this.value = Long.parseLong(meta.getWord());
    }

    public long getValue() {
        return value;
    }
}
