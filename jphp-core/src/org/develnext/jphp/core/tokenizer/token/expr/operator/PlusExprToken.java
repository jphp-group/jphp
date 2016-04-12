package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;

public class PlusExprToken extends OperatorExprToken {
    public PlusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_PLUS);
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public String getCode() {
        return "plus";
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return o1.plus(o2);
    }
}
