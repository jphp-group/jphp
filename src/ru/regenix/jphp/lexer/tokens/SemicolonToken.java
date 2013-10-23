package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

public class SemicolonToken extends Token {

    public SemicolonToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
