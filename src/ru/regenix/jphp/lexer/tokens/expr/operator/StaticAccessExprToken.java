package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class StaticAccessExprToken extends OperatorExprToken {
    public StaticAccessExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DOUBLE_COLON);
    }
}
