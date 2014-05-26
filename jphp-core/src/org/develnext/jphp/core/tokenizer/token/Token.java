package org.develnext.jphp.core.tokenizer.token;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;
import org.develnext.jphp.core.tokenizer.TokenFinder;
import org.develnext.jphp.core.tokenizer.TokenMeta;
import org.develnext.jphp.core.tokenizer.TokenType;

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

    public boolean isNamedToken() {
        return type == TokenType.T_STRING;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;

        Token token = (Token) o;
        return token.getClass() == this.getClass() && getWord().equals(token.getWord());

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
