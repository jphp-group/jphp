package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class SmallerExprToken extends OperatorExprToken {
    public SmallerExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_SMALLER);
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public String getCode() {
        return "smaller";
    }

    @Override
    public Class<?> getResultClass() {
        return Boolean.TYPE;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.smaller(o2) ? Memory.TRUE : Memory.FALSE;
    }
}
