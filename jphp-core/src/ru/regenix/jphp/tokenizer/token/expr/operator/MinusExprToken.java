package ru.regenix.jphp.tokenizer.token.expr.operator;

import php.runtime.Memory;
import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class MinusExprToken extends OperatorExprToken {
    public MinusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_MINUS);
    }

    @Override
    public int getPriority() {
        return 60;
    }

    @Override
    public String getCode() {
        return "minus";
    }

    @Override
    public Memory calc(Memory o1, Memory o2) {
        return o1.minus(o2);
    }
}
