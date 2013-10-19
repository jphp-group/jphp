package ru.regenix.jphp.lexer.tokens;

import ru.regenix.jphp.lexer.TokenType;

public class Token {
    protected final TokenMeta meta;
    protected final TokenType type;

    public Token(TokenMeta meta, TokenType type) {
        this.meta = meta;
        this.type = type;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "[" + meta.getWord() + "]";
    }

    public boolean isTransitional(){
        return false;
    }

    public TokenType getType() {
        return type;
    }

    public TokenMeta getMeta() {
        return meta;
    }
}
