package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

/**
 * :
 */
public class ColonToken extends Token {
    public ColonToken(TokenMeta meta) {
        super(meta, TokenType.T_J_CUSTOM);
    }
}
