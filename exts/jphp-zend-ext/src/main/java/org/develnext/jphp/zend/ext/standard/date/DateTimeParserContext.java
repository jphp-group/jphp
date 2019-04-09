package org.develnext.jphp.zend.ext.standard.date;

import java.time.ZonedDateTime;
import java.util.List;

class DateTimeParserContext {
    private final List<Token> tokens;
    private final Cursor cursor;
    private final DateTimeTokenizer tokenizer;
    private ZonedDateTime dateTime;

    DateTimeParserContext(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        this.tokens = tokens;
        this.cursor = cursor;
        this.tokenizer = tokenizer;
        dateTime = ZonedDateTime.now();
    }

    public List<Token> tokens() {
        return tokens;
    }

    public Cursor cursor() {
        return cursor;
    }

    public Token tokenAtCursor() {
        return tokens.get(cursor.value());
    }

    public int readIntAtCursor() {
        return tokenizer.readInt(tokenAtCursor());
    }

    public DateTimeParserContext withCursorValue(int value) {
        cursor.setValue(value);
        return this;
    }

    public boolean hasMoreTokens() {
        return cursor.value() < tokens.size();
    }

    public DateTimeTokenizer tokenizer() {
        return tokenizer;
    }

    public ZonedDateTime dateTime() {
        return dateTime;
    }

    public DateTimeParserContext dateTime(ZonedDateTime apply) {
        dateTime = apply;
        return this;
    }
}
