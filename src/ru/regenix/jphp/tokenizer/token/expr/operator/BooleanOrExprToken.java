package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class BooleanOrExprToken extends LogicOperatorExprToken {
    public BooleanOrExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_OR);
    }

    @Override
    public int getPriority() {
        return 130;
    }
}
