package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

public class IntegerExprToken extends ValueExprToken {

    private long value;

    public IntegerExprToken(TokenMeta meta) {
        super(meta, TokenType.T_LNUMBER);
        this.value = Long.parseLong(meta.getWord());
    }

    public long getValue() {
        return value;
    }
}
