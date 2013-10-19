package ru.regenix.jphp.lexer.tokens.expr.value;

import ru.regenix.jphp.lexer.TokenType;
import ru.regenix.jphp.lexer.tokens.TokenMeta;
import ru.regenix.jphp.lexer.tokens.expr.ValueExprToken;

public class StringExprToken extends ValueExprToken {
    public enum Quote { SINGLE, DOUBLE, SHELL }

    protected final Quote quote;
    private String value;

    public StringExprToken(TokenMeta meta, Quote quote) {
        super(meta, TokenType.T_CONSTANT_ENCAPSED_STRING);
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
