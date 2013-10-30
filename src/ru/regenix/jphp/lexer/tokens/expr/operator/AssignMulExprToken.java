package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignMulExprToken extends AssignOperatorExprToken {
    public AssignMulExprToken(TokenMeta meta) {
        super(meta, TokenType.T_MUL_EQUAL);
    }
}
