package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class BooleanXorExprToken extends OperatorExprToken {
    public BooleanXorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BOOLEAN_XOR);
    }

    @Override
    public int getPriority() {
        return 180;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.xor(o2) ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public String getCode() {
        return "xor";
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Class<?> getResultClass() {
        return Boolean.TYPE;
    }

    @Override
    public boolean isNamedToken() {
        return true;
    }
}
