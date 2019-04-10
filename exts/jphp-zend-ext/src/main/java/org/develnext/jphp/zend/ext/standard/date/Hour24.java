package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_24;

import java.nio.CharBuffer;

/**
 * Matches the hour in 24 hour format: "04", "07", "19"
 */
class Hour24 extends FixedLengthSymbol {
    private Hour24() {
        super(Symbol.DIGITS, 2);
    }

    static Hour24 of() {
        return new Hour24();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        CharBuffer cb = ctx.tokenizer().readCharBuffer(ctx.tokenAtCursor());
        return HOUR_24.matcher(cb).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int hour = ctx.readIntAtCursor();
        ctx.setHour(hour);
        ctx.cursor().inc();
    }
}
