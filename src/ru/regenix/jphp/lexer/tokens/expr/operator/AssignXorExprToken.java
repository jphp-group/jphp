package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignXorExprToken extends AssignOperatorExprToken {
    public AssignXorExprToken(TokenMeta meta) {
        super(meta, TokenType.T_XOR_EQUAL);
    }
}
