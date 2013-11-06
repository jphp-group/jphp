package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

public class CloseTagToken extends Token {
    public CloseTagToken(TokenMeta meta) {
        super(meta, TokenType.T_CLOSE_TAG);
    }
}
