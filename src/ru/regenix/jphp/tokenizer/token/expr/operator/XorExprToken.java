package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

/**
 * ^
 */
public class XorExprToken extends OperatorExprToken {
    public XorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public String getCode() {
        return "bitXor";
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.bitXor(o2);
    }
}
