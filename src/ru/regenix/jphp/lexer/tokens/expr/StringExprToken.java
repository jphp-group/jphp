package ru.regenix.jphp.lexer.tokens.expr;

import ru.regenix.jphp.lexer.tokens.TokenMeta;

public class StringExprToken extends ExprToken {
    public enum Quote { SINGLE, DOUBLE, SHELL }

    protected final Quote quote;
    private String value;

    public StringExprToken(TokenMeta meta, Quote quote) {
        super(meta);
        this.quote = quote;
        this.value = meta.getWord();
    }

    public Quote getQuote() {
        return quote;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "String("+ quote.name() +")[" + meta.getWord() + "]";
    }
}
