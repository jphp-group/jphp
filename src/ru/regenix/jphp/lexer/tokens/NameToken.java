package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

public class NameToken extends Token {

    private String name;

    public NameToken(TokenMeta meta) {
        super(meta, TokenType.T_STRING);
        this.name = meta.getWord();
    }

    public String getName() {
        return name;
    }
}
