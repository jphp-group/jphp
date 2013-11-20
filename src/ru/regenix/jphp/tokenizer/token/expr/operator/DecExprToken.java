package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

public class DecExprToken extends OperatorExprToken {
    public DecExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DEC);
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
