package org.develnext.jphp.zend.ext.standard.date;

import java.nio.CharBuffer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

class DateTimeParserContext {
    private final List<Token> tokens;
    private final Cursor cursor;
    private final DateTimeTokenizer tokenizer;
    private final Map<TemporalField, Boolean> modified;
    private LocalDate date;
    private LocalTime time;
    private ZoneId zone;

    DateTimeParserContext(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        this.tokens = tokens;
        this.cursor = cursor;
        this.tokenizer = tokenizer;
        modified = new HashMap<>();
    }

    public List<Token> tokens() {
        return tokens;
    }

    public Cursor cursor() {
        return cursor;
    }

    public Token tokenAtCursor() {
        return hasMoreTokens() ? tokens.get(cursor.value()) : Token.EOF;
    }

    public Symbol symbolAtCursor() {
        return tokenAtCursor().symbol();
    }

    public boolean isSymbolAtCursor(Symbol symbol) {
        return tokenAtCursor().symbol() == symbol;
    }

    public int readIntAtCursor() {
        return tokenizer.readInt(tokenAtCursor());
    }

    public long readLongAtCursor() {
        return tokenizer.readLong(tokenAtCursor());
    }

    public CharBuffer readCharBufferAtCursor() {
        return tokenizer.readCharBuffer(tokenAtCursor());
    }

    public String readStringAtCursor() {
        return tokenizer.readString(tokenAtCursor());
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
        initDate();
        initTime();

        zone = zone == null ? ZoneId.systemDefault() : zone;

        return ZonedDateTime.of(date, time, zone);
    }

    public DateTimeParserContext setYear(int year) {
        initDate();

        date = adjust(date, ChronoField.YEAR, year);
        return this;
    }

    private <T extends Temporal> T adjust(T temporal, TemporalField field, int value) {
        modified.put(field, Boolean.TRUE);
        return (T) temporal.with(field, value);
    }

    public DateTimeParserContext setMonth(int month) {
        initDate();

        date = adjust(date, ChronoField.MONTH_OF_YEAR, month);

        return this;
    }

    public DateTimeParserContext setDayOfMonth(int day) {
        initDate();

        date = adjust(date, ChronoField.DAY_OF_MONTH, day);

        return this;
    }

    private void initDate() {
        if (date == null)
            date = LocalDate.now();
    }

    public DateTimeParserContext setHour12(int hour) {
        initTime();

        time = adjust(time, ChronoField.HOUR_OF_AMPM, hour);
        return this;
    }

    public DateTimeParserContext setHour(int hour) {
        initTime();

        time = adjust(time, ChronoField.HOUR_OF_DAY, hour);
        return this;
    }

    private void initTime() {
        if (time == null)
            time = LocalTime.now();
    }

    public DateTimeParserContext setMinute(int minute) {
        initTime();

        time = adjust(time, ChronoField.MINUTE_OF_HOUR, minute);
        return this;
    }

    public DateTimeParserContext setSecond(int second) {
        initTime();

        time = adjust(time, ChronoField.SECOND_OF_MINUTE, second);
        return this;
    }

    public DateTimeParserContext setDayOfYear(int dayOfYear) {
        initDate();
        if (time == null)
            time = LocalTime.MIDNIGHT;

        date = adjust(date, ChronoField.DAY_OF_YEAR, dayOfYear);

        return this;
    }

    public DateTimeParserContext setWeekOfYear(int weekOfYear) {
        initDate();
        if (time == null)
            time = LocalTime.MIDNIGHT;

        date = adjust(date, ChronoField.ALIGNED_WEEK_OF_YEAR, weekOfYear);

        return this;
    }

    public DateTimeParserContext setTimezone(ZoneId timezone) {
        zone = timezone;
        return this;
    }

    public DateTimeParserContext setDayOfWeek(int dayOfWeek) {
        initDate();

        date = adjust(date, ChronoField.DAY_OF_WEEK, dayOfWeek);
        return this;
    }

    public DateTimeParserContext setMicroseconds(int nano) {
        initTime();
        time = adjust(time, ChronoField.MICRO_OF_SECOND, nano);
        return this;
    }

    public DateTimeParserContext setUnixTimestamp(long timestamp) {
        setTimezone(ZoneId.of("UTC"));
        ZonedDateTime zonedDateTime = Instant.ofEpochSecond(timestamp).atZone(zone);

        date = zonedDateTime.toLocalDate();
        time = zonedDateTime.toLocalTime();
        return this;
    }

    public DateTimeParserContext setMeridian(boolean am) {
        if (time == null)
            throw new IllegalStateException("The time should be initialized at this point!");

        if (am) {
            time = time.withHour(time.getHour() % 12);
        } else {
            time = time.withHour(time.getHour() + 12);
        }

        if (isNotModified(ChronoField.MINUTE_OF_HOUR)) {
            time = time.withMinute(0);
        }

        if (isNotModified(ChronoField.SECOND_OF_MINUTE)) {
            time = time.withSecond(0);
        }

        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DateTimeParserContext.class.getSimpleName() + "[", "]")
                .add("cursor=" + cursor)
                .add("tokenAtCursor=" + tokenAtCursor())
                .toString();
    }

    public boolean isModified(TemporalField field) {
        return modified.containsKey(field);
    }

    public boolean isNotModified(TemporalField field) {
        return !isModified(field);
    }

    public char readCharAtCursor() {
        return tokenizer.readChar(tokenAtCursor());
    }
}
