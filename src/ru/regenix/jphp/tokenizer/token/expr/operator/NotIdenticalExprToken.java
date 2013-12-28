package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class NotIdenticalExprToken extends OperatorExprToken {
    public NotIdenticalExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_NOT_IDENTICAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }

    @Override
    public String getCode() {
        return "notIdentical";
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
        return o1.notIdentical(o2) ? Memory.TRUE : Memory.FALSE;
    }
}
