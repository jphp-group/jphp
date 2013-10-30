package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignShiftLeftExprToken extends AssignOperatorExprToken {
    public AssignShiftLeftExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SL_EQUAL);
    }
}
