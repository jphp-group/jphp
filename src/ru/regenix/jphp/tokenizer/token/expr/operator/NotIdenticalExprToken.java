package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class NotIdenticalExprToken extends OperatorExprToken {
    public NotIdenticalExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_NOT_IDENTICAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }
}
