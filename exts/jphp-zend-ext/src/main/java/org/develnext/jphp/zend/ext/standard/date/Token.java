package org.develnext.jphp.zend.ext.standard.date;

import java.util.Objects;
import java.util.StringJoiner;

class Token {
    public static Token EOF = Token.of(Symbol.EOF, -1, 0);

    private final Symbol symbol;
    private final int start;
    private final int length;

    private Token(Symbol symbol, int start, int length) {
        this.symbol = symbol;
        this.start = start;
        this.length = length;
    }

    static Token of(Symbol type, int start, int length) {
        return new Token(type, start, length);
    }

    public Symbol symbol() {
        return symbol;
    }

    public int start() {
        return start;
    }

    public int length() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return start == token.start &&
                length == token.length &&
                symbol == token.symbol;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Token.class.getSimpleName() + "[", "]")
                .add("symbol=" + symbol)
                .add("start=" + start)
                .add("length=" + length)
                .toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, start, length);
    }
}
