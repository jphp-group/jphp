package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

public class IntegerExprToken extends ValueExprToken {
    private long value;

    public IntegerExprToken(TokenMeta meta) {
        super(meta, TokenType.T_LNUMBER);
        this.value = Long.parseLong(meta.getWord());
    }

    public long getValue() {
        return value;
    }

    public boolean isByte(){
        return value >= 0 && value <= 255;
    }

    public boolean isShort(){
        return value >= 0 && value <= Short.MAX_VALUE;
    }

    public boolean isInteger(){
        return value >= 0 && value <= Integer.MAX_VALUE;
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
