package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignMinusExprToken extends AssignOperatorExprToken {
    public AssignMinusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MINUS_EQUAL);
    }
}
