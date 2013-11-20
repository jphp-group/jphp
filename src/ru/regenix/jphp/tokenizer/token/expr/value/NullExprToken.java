package ru.regenix.jphp.tokenizer.token.expr.value;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.ValueExprToken;

public class NullExprToken extends ValueExprToken {

    public NullExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
    }

    @Override
    public boolean isConstant() {
        return true;
    }

    @Override
    public Object toNumeric() {
        return 0L;
    }

    @Override
    public String toString() {
        return "";
    }
}
