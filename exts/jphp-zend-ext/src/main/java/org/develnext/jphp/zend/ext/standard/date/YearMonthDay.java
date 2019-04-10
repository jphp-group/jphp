package org.develnext.jphp.zend.ext.standard.date;

import java.nio.CharBuffer;
import java.util.regex.Pattern;

/**
 * Matches following pattern: YYYYMMDD
 */
class YearMonthDay extends FixedLengthSymbol {
    private static final Pattern PATTERN = Pattern.compile("[0-9]{4}([0][0-9]|1[0-2])([01][0-9]|2[0-4])");

    private YearMonthDay() {
        super(Symbol.DIGITS, 8);
    }

    static YearMonthDay of() {
        return new YearMonthDay();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        CharBuffer cb = ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()));
        return PATTERN.matcher(cb).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int start = ctx.tokenAtCursor().start();

        DateTimeTokenizer tokenizer = ctx.tokenizer();
        int year = tokenizer.readInt(start, 4);
        int month = tokenizer.readInt(start + 4, 2);
        int day = tokenizer.readInt(start + 6, 2);

        ctx.setYear(year).setMonth(month).setDayOfMonth(day);
        ctx.cursor().inc();
    }
}
