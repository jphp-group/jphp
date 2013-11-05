package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;

public class ArrayExprToken extends CallExprToken {
    public ArrayExprToken(TokenMeta meta) {
        super(meta);
        this.setName(new NameToken(meta));
    }
}
