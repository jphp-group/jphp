package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class GreaterExprToken extends OperatorExprToken {
    public GreaterExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_IS_GREATER);
    }

    @Override
    public int getPriority() {
        return 70;
    }
}
