package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class BooleanNotExprToken extends OperatorExprToken {
    public BooleanNotExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BOOLEAN_NOT);
    }

    @Override
    public int getPriority() {
        return 30;
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public String getCode() {
        return "not";
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
    public Memory calc(Memory o1, Memory o2) {
        return o1.not() ? Memory.TRUE : Memory.FALSE;
    }
}
