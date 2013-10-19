package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class BooleanExprToken extends ExprToken {

    private boolean value;

    protected BooleanExprToken(TokenMeta meta) {
        super(meta);
        String word = meta.getWord().toLowerCase();
        if ("true".equals(word))
            this.value = true;
        else if ("false".equals(word))
            this.value = false;
        else
            throw new IllegalArgumentException("Word must be TRUE of FALSE");
    }

    public boolean getValue() {
        return value;
    }
}
