package org.develnext.jphp.core.tokenizer.token.expr.operator;

import php.runtime.memory.StringMemory;
import php.runtime.Memory;
import org.develnext.jphp.core.tokenizer.TokenType;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.token.expr.OperatorExprToken;

public class ConcatExprToken extends OperatorExprToken {
    public ConcatExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CONCAT);
    }

    @Override
    public int getPriority() {
        return 50;
    }

    @Override
    public String getCode() {
        return "concat";
    }

    @Override
    public Class<?> getResultClass() {
        return String.class;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return StringMemory.valueOf(o1.concat(o2));
    }
}
