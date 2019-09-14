package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.Token.EOF;

import java.nio.CharBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Consumer;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
class DateTimeParserContext {
    static final ZoneId ZERO_OFFSET = ZoneId.of("+00:00");
    private static final Consumer<DateTimeParserContext> EMPTY_CONSUMER = ctx -> {};
    private static final List<ChronoField> TIME_CHRONO_FIELDS = Arrays.asList(ChronoField.MICRO_OF_SECOND, ChronoField.SECOND_OF_MINUTE, ChronoField.MINUTE_OF_HOUR, ChronoField.HOUR_OF_DAY);
    private final List<DateTimeTokenizer.Token> tokens;
    private final Cursor cursor;
    private final DateTimeTokenizer tokenizer;
    private final Set<TemporalField> modified;
    private Map<String, Long> relativeContribution;
    private LocalDateTime dateTime;
    private ZoneId zoneId;
    private String parsedZone;

    DateTimeParserContext(List<DateTimeTokenizer.Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer,
                          ZonedDateTime dateTime) {
        this.tokens = tokens;
        this.cursor = cursor;
        this.tokenizer = tokenizer;
        this.modified = new HashSet<>();
        this.dateTime = dateTime.toLocalDateTime();
        this.zoneId = dateTime.getZone();
    }

    public boolean hasModifications() {
        return !modified.isEmpty();
    }

    public List<DateTimeTokenizer.Token> tokens() {
        return tokens;
    }

    public Cursor cursor() {
        return cursor;
    }

    public DateTimeTokenizer.Token tokenAtCursor() {
        return hasMoreTokens() ? tokens.get(cursor.value()) : EOF;
    }

    public DateTimeTokenizer.Token tokenAt(int idx) {
        try {
            return tokens.get(idx);
        } catch (IndexOutOfBoundsException e) {
            return EOF;
        }
    }

    public DateTimeTokenizer.Symbol symbolAtCursor() {
        return tokenAtCursor().symbol();
    }

    public boolean isSymbolAtCursor(DateTimeTokenizer.Symbol symbol) {
        return tokenAtCursor().symbol() == symbol;
    }

    public void skipWhitespaces() {
        while (isSymbolAtCursor(DateTimeTokenizer.Symbol.SPACE))
            cursor.inc();
    }

    public int readIntAtCursor() {
        return tokenizer.readInt(tokenAtCursor());
    }

    public long readLongAtCursorAndInc() {
        long ret = tokenizer.readLong(tokenAtCursor());
        cursor.inc();

        return ret;
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

    public String readStringAt(int idx) {
        return tokenizer.readString(tokenAt(idx));
    }

    public long readLongAt(int idx) {
        return tokenizer.readLong(tokenAt(idx));
    }

    public String readStringAtCursorAndInc() {
        String str = tokenizer.readString(tokenAtCursor());
        cursor.inc();

        return str;
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
        if (isNotModified(ChronoField.MICRO_OF_SECOND) && isNotModified(ChronoField.MILLI_OF_SECOND))
            dateTime = dateTime.with(ChronoField.MICRO_OF_SECOND, 0);

        return this.dateTime.atZone(zoneId);
    }

    public DateTimeParseResult result() {
        return new DateTimeParseResult(dateTime(), modified, parsedZone, relativeContribution);
    }

    public DateTimeParserContext setYear(long year) {
        dateTime = adjust(dateTime, ChronoField.YEAR, year);
        return this;
    }

    @SuppressWarnings("unchecked")
    private <T extends Temporal> T adjust(T temporal, TemporalField field, long value) {
        modified.add(field);
        return (T) temporal.with(field, value);
    }

    public DateTimeParserContext setMonth(int month) {
        dateTime = adjust(dateTime, ChronoField.MONTH_OF_YEAR, month);

        return this;
    }

    public DateTimeParserContext setDayOfMonth(long day) {
        dateTime = adjust(dateTime, ChronoField.DAY_OF_MONTH, day);

        return this;
    }

    public DateTimeParserContext setHour(int hour) {
        dateTime = adjust(dateTime, ChronoField.HOUR_OF_DAY, hour);
        return this;
    }

    public DateTimeParserContext setMinute(int minute) {
        dateTime = adjust(dateTime, ChronoField.MINUTE_OF_HOUR, minute);
        return this;
    }

    public DateTimeParserContext setSecond(int second) {
        dateTime = adjust(dateTime, ChronoField.SECOND_OF_MINUTE, second);
        return this;
    }

    public DateTimeParserContext setDayOfYear(int dayOfYear) {
        if (!isTimeModified())
            atStartOfDay();

        dateTime = adjust(dateTime, ChronoField.DAY_OF_YEAR, dayOfYear);
        return this;
    }

    public DateTimeParserContext withAdjuster(TemporalAdjuster adjuster, TemporalField field) {
        dateTime = dateTime.with(adjuster);

        modified.add(field);
        return this;
    }

    public boolean isTimeModified() {
        return modified.contains(ChronoField.SECOND_OF_MINUTE) ||
                modified.contains(ChronoField.MINUTE_OF_HOUR) ||
                modified.contains(ChronoField.HOUR_OF_DAY) ||
                modified.contains(ChronoField.HOUR_OF_AMPM) ||
                modified.contains(ChronoField.MICRO_OF_SECOND);
    }

    public DateTimeParserContext setWeekOfYear(int weekOfYear) {
        if (!isTimeModified())
            atStartOfDay();

        // old value was ChronoField.ALIGNED_WEEK_OF_YEAR
        dateTime = adjust(dateTime, IsoFields.WEEK_OF_WEEK_BASED_YEAR, weekOfYear);

        return this;
    }

    public DateTimeParserContext setTimezone(ZoneId timezone) {
        // UNIX timestamp was set.
        if (modified.contains(ChronoField.EPOCH_DAY))
            return this;

        zoneId = timezone;
        modified.add(TimezoneField.INSTANSE);
        return this;
    }

    public DateTimeParserContext plusDays(long days) {
        dateTime = dateTime.plusDays(days);
        modified.add(ChronoField.DAY_OF_MONTH);

        return this;
    }

    public DateTimeParserContext plusHours(long hours) {
        dateTime = dateTime.plusHours(hours);
        modified.add(ChronoField.HOUR_OF_DAY);

        return this;
    }

    public DateTimeParserContext plusSeconds(long seconds) {
        dateTime = dateTime.plusSeconds(seconds);
        modified.add(ChronoField.SECOND_OF_MINUTE);

        return this;
    }

    public DateTimeParserContext setTimezone(String timezone) {
        ZoneId zoneId = ZoneIdFactory.of(timezone);
        parsedZone = timezone;
        return setTimezone(zoneId);
    }

    public DateTimeParserContext setDayOfWeek(int dayOfWeek) {
        dateTime = adjust(dateTime, ChronoField.DAY_OF_WEEK, dayOfWeek);
        return this;
    }

    public DateTimeParserContext setMicroseconds(int nano) {
        dateTime = adjust(dateTime, ChronoField.MICRO_OF_SECOND, nano);
        return this;
    }

    public DateTimeParserContext setUnixTimestamp(long timestamp) {
        setTimezone(ZERO_OFFSET);
        dateTime = Instant.ofEpochSecond(timestamp).atZone(zoneId).toLocalDateTime();
        modified.add(ChronoField.EPOCH_DAY);

        return this;
    }

    public DateTimeParserContext setMeridian(boolean am) {
        if (!isTimeModified())
            throw new IllegalStateException("The time should be initialized at this point!");

        dateTime = dateTime.with(Adjusters.meridian(am));

        if (isNotModified(ChronoField.MINUTE_OF_HOUR)) {
            dateTime = dateTime.withMinute(0);
        }

        if (isNotModified(ChronoField.SECOND_OF_MINUTE)) {
            dateTime = dateTime.withSecond(0);
        }

        return this;
    }

    public DateTimeParserContext addRelativeContributor(String unit, long value) {
        if (relativeContribution == null) {
            relativeContribution = new HashMap<>(2);
        }

        relativeContribution.put(unit, value);

        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DateTimeParserContext.class.getSimpleName() + "[", "]")
                .add("cursor=" + cursor)
                .add("tokenAtCursor=" + tokenAtCursor())
                .add("value=" + (tokenAtCursor() == EOF ? "" : readStringAtCursor()))
                .toString();
    }

    public boolean isModified(TemporalField field) {
        return modified.contains(field);
    }

    public boolean isModifiedTimezone() {
        return modified.contains(TimezoneField.INSTANSE);
    }

    public boolean isNotModified(TemporalField field) {
        return !isModified(field);
    }

    public char readCharAtCursor() {
        return tokenizer.readChar(tokenAtCursor());
    }

    public DateTimeParserContext atStartOfDay() {
        dateTime = dateTime.truncatedTo(ChronoUnit.DAYS);

        return this;
    }

    public DateTimeParserContext atStartOfDayWithMod() {
        atStartOfDay();

        modified.addAll(TIME_CHRONO_FIELDS);
        return this;
    }

    public boolean isRelativeContributorPresent(String unit) {
        if (relativeContribution == null) {
            return false;
        }

        return relativeContribution.containsKey(unit);
    }

    private static class TimezoneField implements TemporalField {
        private static final TimezoneField INSTANSE = new TimezoneField();

        @Override
        public TemporalUnit getBaseUnit() {
            return null;
        }

        @Override
        public TemporalUnit getRangeUnit() {
            return null;
        }

        @Override
        public ValueRange range() {
            return null;
        }

        @Override
        public boolean isDateBased() {
            return false;
        }

        @Override
        public boolean isTimeBased() {
            return false;
        }

        @Override
        public boolean isSupportedBy(TemporalAccessor temporal) {
            return false;
        }

        @Override
        public ValueRange rangeRefinedBy(TemporalAccessor temporal) {
            return null;
        }

        @Override
        public long getFrom(TemporalAccessor temporal) {
            return 0;
        }

        @Override
        public <R extends Temporal> R adjustInto(R temporal, long newValue) {
            return null;
        }
    }

    /**
     * Grouping class for all consumers.
     */
    static final class Consumers {
        private Consumers() {
        }

        static Consumer<DateTimeParserContext> simpleYearAdjuster() {
            return ctx -> ctx.setYear(ctx.readLongAtCursor());
        }

        static Consumer<DateTimeParserContext> yearAdjuster() {
            return ctx -> {
                String yearStr = ctx.readStringAtCursor();
                long year = Long.parseLong(yearStr);
                if (yearStr.length() == 4) {
                    ctx.setYear(year);
                } else {
                    ctx.withAdjuster(Adjusters.year(year), ChronoField.YEAR);
                }
            };
        }

        static Consumer<DateTimeParserContext> charBufferAppender(StringBuilder sb) {
            if (sb == null) return empty();

            return ctx -> sb.append(ctx.readCharBufferAtCursor());
        }

        static Consumer<DateTimeParserContext> empty() {
            return EMPTY_CONSUMER;
        }

        static Consumer<DateTimeParserContext> charAppender(StringBuilder sb) {
            if (sb == null) return empty();
            return ctx -> sb.append(ctx.readCharAtCursor());
        }

        static Consumer<DateTimeParserContext> charLowerAppender(StringBuilder sb) {
            if (sb == null) return empty();
            return ctx -> sb.append(Character.toLowerCase(ctx.readCharAtCursor()));
        }

        static Consumer<DateTimeParserContext> cursorIncrementer() {
            return ctx -> ctx.cursor().inc();
        }

        static Consumer<DateTimeParserContext> isoWeekAdjuster() {
            return ctx -> ctx.setWeekOfYear(ctx.readIntAtCursor());
        }

        static Consumer<DateTimeParserContext> dayAdjuster() {
            return ctx -> ctx.setDayOfMonth(ctx.readIntAtCursor());
        }

        static Consumer<DateTimeParserContext> monthAdjuster() {
            return ctx -> ctx.setMonth(ctx.readIntAtCursor());
        }
    }

    static class Cursor {
        private int value;

        void inc() {
            value++;
        }

        void dec() {
            value--;
        }

        int value() {
            return value;
        }

        void setValue(int cursor) {
            value = cursor;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Cursor.class.getSimpleName() + "[", "]")
                    .add("value=" + value)
                    .toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Cursor)) return false;
            Cursor cursor = (Cursor) o;
            return value == cursor.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
