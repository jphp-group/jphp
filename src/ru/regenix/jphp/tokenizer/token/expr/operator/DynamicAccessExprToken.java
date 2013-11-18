package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class DynamicAccessExprToken extends OperatorExprToken {
    public DynamicAccessExprToken(TokenMeta meta) {
        super(meta, TokenType.T_OBJECT_OPERATOR);
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
