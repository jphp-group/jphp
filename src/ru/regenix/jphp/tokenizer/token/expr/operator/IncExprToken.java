package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class IncExprToken extends OperatorExprToken {
    public IncExprToken(TokenMeta meta) {
        super(meta, TokenType.T_INC);
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
