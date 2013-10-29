package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

/**
 * and symbol
 */
public class BooleanAnd2ExprToken extends LogicOperatorExprToken {
    public BooleanAnd2ExprToken(TokenMeta meta) {
        super(meta, TokenType.T_BOOLEAN_AND);
    }

    @Override
    public int getPriority() {
        return 170;
    }
}
