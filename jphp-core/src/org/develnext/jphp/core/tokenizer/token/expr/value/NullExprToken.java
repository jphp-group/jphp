package org.develnext.jphp.core.tokenizer.token.expr.value;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.ValueExprToken;

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
