package org.develnext.jphp.core.tokenizer.token.expr.operator.cast;

import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

public class StringCastExprToken extends CastExprToken {
    public StringCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return String.class;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return new StringMemory(o1.toString());
    }

    @Override
    public String getCode() {
        return "toString";
    }
}
