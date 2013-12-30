package ru.regenix.jphp.tokenizer.token.expr.operator.cast;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class UnsetCastExprToken extends CastExprToken {
    public UnsetCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_UNSET_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return Memory.class;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return Memory.NULL;
    }

    @Override
    public String getCode() {
        return "toUnset";
    }
}
