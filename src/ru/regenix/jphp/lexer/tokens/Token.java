package ru.regenix.jphp.lexer.tokens;

public class Token {

    protected final TokenMeta meta;

    public Token(TokenMeta meta) {
        this.meta = meta;
    }

    @Override
    public String toString(){
        return getClass().getSimpleName() + "[" + meta.getWord() + "]";
    }
}
