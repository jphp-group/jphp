package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

abstract public class AssignOperatorExprToken extends OperatorExprToken
    implements AssignableOperatorToken {
    public AssignOperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }

    @Override
    public int getPriority() {
        return 150;
    }

    @Override
    public boolean isRightSide() {
        return true;
    }
}
