package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class NotExprToken extends OperatorExprToken {
    public NotExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 21;
    }
}
