package org.develnext.jphp.core.tokenizer.token.expr.operator.cast;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class BooleanCastExprToken extends CastExprToken {
    public BooleanCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOL_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return Boolean.TYPE;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.toBoolean() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public String getCode() {
        return "toBoolean";
    }
}
