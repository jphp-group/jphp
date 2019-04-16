package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_II;

class Minute2 extends FixedLengthSymbol {
    private Minute2() {
        super(Symbol.DIGITS, 2);
    }

    public static Minute2 of() {
        return new Minute2();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return MINUTE_II.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int minute = ctx.readIntAtCursor();
        ctx.setMinute(minute);
        ctx.cursor().inc();
    }
}
