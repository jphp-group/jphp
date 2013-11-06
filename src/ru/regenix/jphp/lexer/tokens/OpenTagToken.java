package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

public class OpenTagToken extends Token {
    public OpenTagToken(TokenMeta meta) {
        super(meta, TokenType.T_OPEN_TAG);
    }
}
