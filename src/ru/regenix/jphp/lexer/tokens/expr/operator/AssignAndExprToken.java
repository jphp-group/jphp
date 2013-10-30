package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignAndExprToken extends AssignOperatorExprToken {
    public AssignAndExprToken(TokenMeta meta) {
        super(meta, TokenType.T_AND_EQUAL);
    }
}
