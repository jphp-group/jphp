package ru.regenix.jphp.tokenizer.token;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
import ru.regenix.jphp.tokenizer.TokenFinder;
import ru.regenix.jphp.tokenizer.TokenMeta;
import ru.regenix.jphp.tokenizer.TokenType;

import java.lang.reflect.InvocationTargetException;

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

    public TokenType getType() {
        return type;
    }

    public TokenMeta getMeta() {
        return meta;
    }

    public TraceInfo toTraceInfo(Context context){
        return getMeta().toTraceInfo(context);
    }

    public String getWord(){
        return getMeta().getWord();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;

        Token token = (Token) o;
        if (token.getClass() != this.getClass()) return false;
        if (!getWord().equals(token.getWord())) return false;

        return true;
    }

    public static Token of(TokenMeta meta){
        TokenFinder finder = new TokenFinder();
        Class<? extends Token> clazz = finder.find(meta);

        try {
            return clazz.getConstructor(TokenMeta.class).newInstance(meta);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Token of(String word){
        return of(new TokenMeta(word, 0, 0, 0, 0));
    }
}
