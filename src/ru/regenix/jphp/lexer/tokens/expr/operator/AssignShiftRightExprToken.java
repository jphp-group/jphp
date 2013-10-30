package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignShiftRightExprToken extends AssignOperatorExprToken {
    public AssignShiftRightExprToken(TokenMeta meta) {
        super(meta, TokenType.T_SR_EQUAL);
    }
}
