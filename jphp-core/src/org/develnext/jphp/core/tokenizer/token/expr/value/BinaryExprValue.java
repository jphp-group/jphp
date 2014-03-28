package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

import java.math.BigInteger;

/**
 * User: popsul
 * Date: 28.03.14
 * Time: 21:14
 */
public class BinaryExprValue extends ValueExprToken {

    private long value;

    public BinaryExprValue(TokenMeta meta) {
        super(meta, TokenType.T_LNUMBER);

        String word = meta.getWord();
        if (word.startsWith("-")) {
            value = -new BigInteger(meta.getWord().substring(3), 2).longValue();
        } else {
            value = new BigInteger(meta.getWord().substring(2), 2).longValue();
        }
    }

    public long getValue() {
        return value;
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
