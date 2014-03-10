package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

import java.math.BigInteger;

public class IntegerExprToken extends ValueExprToken {
    private long value;
    private BigInteger bigValue;

    public IntegerExprToken(TokenMeta meta) {
        super(meta, TokenType.T_LNUMBER);
        try {
            this.value = Long.parseLong(meta.getWord());
        } catch (NumberFormatException e){
            this.bigValue = new BigInteger(meta.getWord());
        }
    }

    public long getValue() {
        return value;
    }

    public BigInteger getBigValue() {
        return bigValue == null ? BigInteger.valueOf(value) : bigValue;
    }

    public boolean isBigValue(){
        return bigValue != null;
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
