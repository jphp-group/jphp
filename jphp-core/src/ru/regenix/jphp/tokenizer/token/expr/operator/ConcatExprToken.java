package ru.regenix.jphp.tokenizer.token.expr.operator;

import php.runtime.memory.StringMemory;
import php.runtime.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

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
