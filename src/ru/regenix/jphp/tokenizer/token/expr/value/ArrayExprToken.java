package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenMeta;

public class ArrayExprToken extends CallExprToken {
    public ArrayExprToken(TokenMeta meta) {
        super(meta);
        this.setName(new NameToken(meta));
    }
}
