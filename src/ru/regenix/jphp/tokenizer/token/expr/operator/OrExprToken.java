package ru.regenix.jphp.tokenizer.token.expr.operator;

import php.runtime.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

/**
 * |
 */
public class OrExprToken extends OperatorExprToken {
    public OrExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public int getPriority() {
        return 110;
    }

    @Override
    public String getCode() {
        return "bitOr";
    }

    @Override
    public boolean isSide() {
        return false;
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.bitOr(o2);
    }
}
