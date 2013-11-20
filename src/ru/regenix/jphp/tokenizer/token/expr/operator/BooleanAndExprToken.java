package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;

public class BooleanAndExprToken extends LogicOperatorExprToken {
    public BooleanAndExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_AND);
    }

    @Override
    public int getPriority() {
        return 120;
    }
}
