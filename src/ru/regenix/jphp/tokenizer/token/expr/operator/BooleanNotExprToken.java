package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class BooleanNotExprToken extends OperatorExprToken {
    public BooleanNotExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BOOLEAN_NOT);
    }

    @Override
    public int getPriority() {
        return 30;
    }

    @Override
    public boolean isBinary() {
        return false;
    }
}
