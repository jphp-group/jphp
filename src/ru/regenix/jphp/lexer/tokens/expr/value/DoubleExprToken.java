package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

public class DoubleExprToken extends ValueExprToken {

    private double value;

    public DoubleExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DNUMBER);
        this.value = Double.parseDouble(meta.getWord());
    }

    public double getValue() {
        return value;
    }

    public boolean isZero(){
        return meta.getWord().matches("^0\\.[0]+$");
    }

    public boolean isOne(){
        return meta.getWord().matches("^1\\.[0]+$");
    }

    public boolean isFloat(){
        return value < Float.MAX_VALUE && value > Float.MIN_VALUE;
    }
}
