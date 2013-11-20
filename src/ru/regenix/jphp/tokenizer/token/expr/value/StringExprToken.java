package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

public class StringExprToken extends ValueExprToken {

    public enum Quote { SINGLE, DOUBLE, SHELL }

    protected final Quote quote;
    private String value;

    public StringExprToken(TokenMeta meta, Quote quote) {
        super(meta, TokenType.T_CONSTANT_ENCAPSED_STRING);
        this.quote = quote;
        this.value = meta.getWord();
    }

    public Quote getQuote() {
        return quote;
    }

    public String getValue() {
        return value;
    }

    @Override
    public Object toNumeric(){
        int len = value.length();
        boolean real = false;
        int i = 0;
        for(; i < len; i++){
            char ch = value.charAt(i);
            if (!('9' >= ch && ch >= '0')){
                if (ch == '.'){
                    if (real)
                        break;
                    real = true;
                    continue;
                }
                if (i == 0)
                    return 0;
                else
                    break;
            }
        }
        if (real) {
            if (len == i)
                return Double.parseDouble(value);
            else
                return Double.parseDouble(value.substring(0, i));
        } else {
            if (len == i)
                return Long.parseLong(value);
            else
                return Long.parseLong(value.substring(0, i));
        }
    }


    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isConstant() {
        return true;
    }
}
