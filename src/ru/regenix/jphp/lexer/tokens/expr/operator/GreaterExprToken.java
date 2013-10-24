package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class GreaterExprToken extends OperatorExprToken {
    public GreaterExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_IS_GREATER);
    }

    @Override
    public int getPriority() {
        return 70;
    }
}
