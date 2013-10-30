package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignDivExprToken extends AssignOperatorExprToken {
    public AssignDivExprToken(TokenMeta meta) {
        super(meta, TokenType.T_DIV_EQUAL);
    }
}
