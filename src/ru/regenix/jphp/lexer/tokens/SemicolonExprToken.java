package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

public class SemicolonExprToken extends Token {

    public SemicolonExprToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
