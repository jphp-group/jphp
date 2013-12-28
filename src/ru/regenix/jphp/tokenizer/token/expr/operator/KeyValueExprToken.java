package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.KeyValueMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class KeyValueExprToken extends OperatorExprToken {
    public KeyValueExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DOUBLE_ARROW);
    }

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public String getCode() {
        return "newKeyValue";
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return new KeyValueMemory(o1, o2);
    }
}
