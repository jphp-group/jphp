package org.develnext.jphp.core.tokenizer.token.expr.operator.cast;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import php.runtime.Memory;

public class BinaryCastExprToken extends CastExprToken {
    public BinaryCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BINARY_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return Memory.class;
    }

    @Override
    public String getCode() {
        return "toBinary";
    }
}
