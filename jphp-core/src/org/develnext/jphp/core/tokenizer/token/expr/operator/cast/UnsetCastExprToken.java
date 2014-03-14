package org.develnext.jphp.core.tokenizer.token.expr.operator.cast;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class UnsetCastExprToken extends CastExprToken {
    public UnsetCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_UNSET_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return Memory.class;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return Memory.NULL;
    }

    @Override
    public String getCode() {
        return "toUnset";
    }
}
