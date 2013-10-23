package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.NameToken;
import ru.regenix.jphp.lexer.tokens.expr.value.CallExprToken;

public class ArrayExprToken extends CallExprToken {
    public ArrayExprToken(TokenMeta meta) {
        super(meta);
        this.setName(new NameToken(meta));
    }
}
