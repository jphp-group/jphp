package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class IdenticalExprToken extends OperatorExprToken {
    public IdenticalExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_IDENTICAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }
}
