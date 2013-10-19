package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class SemicolonExprToken extends ExprToken {

    public SemicolonExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
