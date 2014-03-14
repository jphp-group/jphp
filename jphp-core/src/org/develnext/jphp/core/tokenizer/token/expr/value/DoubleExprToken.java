package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

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

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public Object toNumeric() {
        return value;
    }
}
