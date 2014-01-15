package ru.regenix.jphp.tokenizer.token.expr.operator;

import php.runtime.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

/**
 * or symbol
 */
public class BooleanOr2ExprToken extends LogicOperatorExprToken {
    public BooleanOr2ExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_OR);
    }

    @Override
    public int getPriority() {
        return 190;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.toBoolean() || o2.toBoolean() ? Memory.TRUE : Memory.FALSE;
    }
}
