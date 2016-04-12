package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class MulExprToken extends OperatorExprToken {
    public MulExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MUL);
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getCode() {
        return "mul";
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.mul(o2);
    }
}
