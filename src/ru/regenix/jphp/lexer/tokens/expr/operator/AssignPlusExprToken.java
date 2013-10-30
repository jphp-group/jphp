package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignPlusExprToken extends AssignOperatorExprToken {
    public AssignPlusExprToken(TokenMeta meta) {
        super(meta, TokenType.T_PLUS_EQUAL);
    }
}
