package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class AssignConcatExprToken extends AssignOperatorExprToken {
    public AssignConcatExprToken(TokenMeta meta) {
        super(meta, TokenType.T_CONCAT_EQUAL);
    }
}
