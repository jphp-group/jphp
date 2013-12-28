package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class GreaterExprToken extends OperatorExprToken {
    public GreaterExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_IS_GREATER);
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public String getCode() {
        return "greater";
    }

    @Override
    public Class<?> getResultClass() {
        return Boolean.TYPE;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.greater(o2) ? Memory.TRUE : Memory.FALSE;
    }
}
