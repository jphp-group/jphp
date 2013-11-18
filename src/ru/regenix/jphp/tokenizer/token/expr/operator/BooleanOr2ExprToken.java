package ru.regenix.jphp.tokenizer.token.expr.operator;

import ru.regenix.jphp.tokenizer.TokenType;
import ru.regenix.jphp.tokenizer.token.TokenMeta;

/**
 * or symbol
 */
public class BooleanOr2ExprToken extends LogicOperatorExprToken {
    public BooleanOr2ExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_OR);
    }

    @Override
    public int getPriority() {
        return 190;
    }
}
