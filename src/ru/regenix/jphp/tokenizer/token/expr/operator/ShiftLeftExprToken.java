package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.token.expr.OperatorExprToken;

/**
 * <<
 */
public class ShiftLeftExprToken extends OperatorExprToken {
    public ShiftLeftExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SL);
    }

    @Override
    public int getPriority() {
        return 60;
    }
}
