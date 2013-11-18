package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class SmallerExprToken extends OperatorExprToken {
    public SmallerExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_SMALLER);
    }

    @Override
    public int getPriority() {
        return 70;
    }
}
