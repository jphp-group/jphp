package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class PowExprToken extends OperatorExprToken {
    public PowExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_POW);
    }

    @Override
    public int getPriority() {
        return 40;
    }

    @Override
    public String getCode() {
        return "pow";
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.pow(o2);
    }
}
