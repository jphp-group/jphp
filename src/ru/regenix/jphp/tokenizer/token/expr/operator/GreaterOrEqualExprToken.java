package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class GreaterOrEqualExprToken extends OperatorExprToken {
    public GreaterOrEqualExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_GREATER_OR_EQUAL);
    }

    @Override
    public int getPriority() {
        return 70;
    }
}
