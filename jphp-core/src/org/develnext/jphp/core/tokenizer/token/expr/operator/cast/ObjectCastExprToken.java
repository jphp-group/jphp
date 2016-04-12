package org.develnext.jphp.core.tokenizer.token.expr.operator.cast;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class ObjectCastExprToken extends CastExprToken {
    public ObjectCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_OBJECT_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return Memory.class;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return null;
    }

    @Override
    public String getCode() {
        return "toObject";
    }

    @Override
    public boolean isEnvironmentNeeded() {
        return true;
    }
}
