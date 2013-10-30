package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignOrExprToken extends AssignOperatorExprToken {
    public AssignOrExprToken(TokenMeta meta) {
        super(meta, TokenType.T_OR_EQUAL);
    }
}
