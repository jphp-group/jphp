package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class NotExprToken extends OperatorExprToken {
    public NotExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
