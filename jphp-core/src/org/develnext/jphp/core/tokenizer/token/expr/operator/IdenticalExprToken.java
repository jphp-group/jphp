package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class IdenticalExprToken extends OperatorExprToken {
    public IdenticalExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_IDENTICAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }

    @Override
    public String getCode() {
        return "identical";
    }

    @Override
    public Class<?> getResultClass() {
        return Boolean.TYPE;
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.identical(o2) ? Memory.TRUE : Memory.FALSE;
    }
}
