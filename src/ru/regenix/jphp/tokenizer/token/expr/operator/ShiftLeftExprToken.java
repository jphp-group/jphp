package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

/**
 * <<
 */
public class ShiftLeftExprToken extends OperatorExprToken {
    public ShiftLeftExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SL);
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public String getCode() {
        return "bitShl";
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.bitShl(o2);
    }
}
