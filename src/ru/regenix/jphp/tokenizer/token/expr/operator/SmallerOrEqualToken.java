package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class SmallerOrEqualToken extends OperatorExprToken {
    public SmallerOrEqualToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_SMALLER_OR_EQUAL);
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public String getCode() {
        return "smallerEq";
    }

    @Override
    public Class<?> getResultClass() {
        return Boolean.TYPE;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.smallerEq(o2) ? Memory.TRUE : Memory.FALSE;
    }
}
