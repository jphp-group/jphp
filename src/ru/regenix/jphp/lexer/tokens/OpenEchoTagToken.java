package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

public class OpenEchoTagToken extends Token {
    public OpenEchoTagToken(TokenMeta meta) {
        super(meta, TokenType.T_OPEN_TAG_WITH_ECHO);
    }
}
