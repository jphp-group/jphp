package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class ConcatExprToken extends OperatorExprToken {
    public ConcatExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CONCAT);
    }

    @Override
    public int getPriority() {
        return 50;
    }
}
