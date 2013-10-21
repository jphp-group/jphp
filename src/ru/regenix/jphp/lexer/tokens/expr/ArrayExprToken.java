package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class ArrayExprToken extends CallExprToken {
    public ArrayExprToken(TokenMeta meta) {
        super(meta);
        this.setName(new NameToken(meta));
    }
}
