package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class BooleanNotEqualExprToken extends OperatorExprToken {
    public BooleanNotEqualExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_NOT_EQUAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }

    @Override
    public String getCode() {
        return "notEqual";
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
        return o1.notEqual(o2) ? Memory.TRUE : Memory.FALSE;
    }
}
