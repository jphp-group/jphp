package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

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
