package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class BooleanNotExprToken extends OperatorExprToken {
    public BooleanNotExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BOOLEAN_NOT);
    }

    @Override
    public int getPriority() {
        return 30;
    }
}
