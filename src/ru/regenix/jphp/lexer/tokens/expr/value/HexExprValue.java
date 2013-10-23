package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

import java.math.BigInteger;

public class HexExprValue extends ValueExprToken {

    private long value;

    public HexExprValue(TokenMeta meta) {
        super(meta, TokenType.T_LNUMBER);
        String word = meta.getWord();
        if (word.startsWith("-"))
            value = - new BigInteger(meta.getWord().substring(3), 16).longValue();
        else
            value = new BigInteger(meta.getWord().substring(2), 16).longValue();
    }

    public long getValue() {
        return value;
    }
}
