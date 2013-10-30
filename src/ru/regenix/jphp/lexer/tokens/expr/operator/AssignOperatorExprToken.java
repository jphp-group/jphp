package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.OperatorExprToken;

abstract public class AssignOperatorExprToken extends OperatorExprToken {
    public AssignOperatorExprToken(TokenMeta meta, TokenType type) {
        super(meta, type);
    }
}
