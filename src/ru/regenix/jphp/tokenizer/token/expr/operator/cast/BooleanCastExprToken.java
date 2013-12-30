package ru.regenix.jphp.tokenizer.token.expr.operator.cast;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class BooleanCastExprToken extends CastExprToken {
    public BooleanCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOL_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return Boolean.TYPE;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.toBoolean() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public String getCode() {
        return "toBoolean";
    }
}
