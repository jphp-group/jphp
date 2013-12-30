package ru.regenix.jphp.tokenizer.token.expr.operator.cast;

import ru.regenix.jphp.runtime.memory.DoubleMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class DoubleCastExprToken extends CastExprToken {
    public DoubleCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DOUBLE_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return Double.TYPE;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return new DoubleMemory(o1.toDouble());
    }

    @Override
    public String getCode() {
        return "toDouble";
    }
}
