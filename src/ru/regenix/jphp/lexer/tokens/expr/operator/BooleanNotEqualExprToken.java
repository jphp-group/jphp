package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class BooleanNotEqualExprToken extends OperatorExprToken {
    public BooleanNotEqualExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_NOT_EQUAL);
    }

    @Override
    public int getPriority() {
        return 80;
    }
}
