package ru.regenix.jphp.tokenizer.token.expr.operator;

import php.runtime.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

/**
 * >>
 */
public class ShiftRightExprToken extends OperatorExprToken {
    public ShiftRightExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SR);
    }

    @Override
    public int getPriority() {
        return 70;
    }

    @Override
    public String getCode() {
        return "bitShr";
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.bitShr(o2);
    }
}
