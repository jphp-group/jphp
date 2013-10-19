package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class DoubleExprToken extends ExprToken {

    private double value;

    public DoubleExprToken(TokenMeta meta) {
        super(meta);
        this.value = Double.parseDouble(meta.getWord());
    }

    public double getValue() {
        return value;
    }
}
