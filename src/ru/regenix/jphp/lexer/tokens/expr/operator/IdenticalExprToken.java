package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

public class IdenticalExprToken extends OperatorExprToken {
    public IdenticalExprToken(TokenMeta meta) {
        super(meta, TokenType.T_IS_IDENTICAL);
    }
}
