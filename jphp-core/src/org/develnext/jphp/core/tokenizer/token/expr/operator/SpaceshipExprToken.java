package org.develnext.jphp.core.tokenizer.token.expr.operator;

import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.LongMemory;

public class SpaceshipExprToken extends OperatorExprToken {
    public SpaceshipExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SPACESHIP);
    }

    @Override
    public int getPriority() {
        return 80;
    }

    @Override
    public String getCode() {
        return "spaceshipCompare";
    }

    @Override
    public Class<?> getResultClass() {
        return Long.TYPE;
    }

    @Override
    public Memory calc(Environment env, TraceInfo trace, Memory o1, Memory o2) {
        return LongMemory.valueOf(o1.spaceshipCompare(o2));
    }
}
