package ru.regenix.jphp.tokenizer.token.expr.operator.cast;

import php.runtime.memory.StringMemory;
import php.runtime.Memory;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

public class StringCastExprToken extends CastExprToken {
    public StringCastExprToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING_CAST);
    }

    @Override
    public Class<?> getResultClass() {
        return String.class;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return new StringMemory(o1.toString());
    }

    @Override
    public String getCode() {
        return "toString";
    }
}
