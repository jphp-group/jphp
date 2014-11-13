package org.develnext.jphp.core.tokenizer.token.expr.value;

import php.runtime.common.GrammarUtils;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

import java.math.BigInteger;

public class IntegerExprToken extends ValueExprToken {

    private long value;
    private BigInteger bigValue;

    public IntegerExprToken(TokenMeta meta) {
        super(meta, TokenType.T_LNUMBER);
        String word = meta.getWord();
        boolean isNegative = false;
        int radix, offset;
        if (word.charAt(0) == '-') {
            word = word.substring(1);
            isNegative = true;
        }
        if (GrammarUtils.isOctalInteger(word)) {
            radix = 8;
            offset = 1;
        } else if (GrammarUtils.isBinaryInteger(word)) {
            radix = 2;
            offset = 2;
        } else if (GrammarUtils.isHexInteger(word)) {
            radix = 16;
            offset = 2;
        } else {
            try {
                value = Long.parseLong(word);
                if (isNegative) {
                    value = -value;
                }
            } catch (NumberFormatException e){
                bigValue = new BigInteger(word);
                if (isNegative) {
                    bigValue = bigValue.negate();
                }
            }
            return;
        }

        value = new BigInteger(word.substring(offset), radix).longValue();
        if (isNegative) {
            value = -value;
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
