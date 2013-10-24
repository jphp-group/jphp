package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class BooleanXorExprToken extends OperatorExprToken {
    public BooleanXorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_BOOLEAN_XOR);
    }

    @Override
    public int getPriority() {
        return 180;
    }
}
