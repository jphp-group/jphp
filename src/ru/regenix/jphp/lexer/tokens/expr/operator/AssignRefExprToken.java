package ru.regenix.jphp.lexer.tokens.expr.operator;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ExprToken;

public class AssignRefExprToken extends ExprToken {
    public AssignRefExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_EQUAL);
    }
}
