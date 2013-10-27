package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

public class BooleanExprToken extends ValueExprToken {

    private boolean value;

    public BooleanExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
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

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Object toNumeric() {
        return value ? 1L : 0L;
    }

    @Override
    public String toString() {
        return value ? "-1" : "";
    }
}
