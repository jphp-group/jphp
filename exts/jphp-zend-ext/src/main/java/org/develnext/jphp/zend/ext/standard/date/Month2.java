package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_MM;

class Month2 extends FixedLengthSymbol {
    Month2() {
        super(Symbol.DIGITS, 2);
    }

    public static Month2 of() {
        return new Month2();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return MONTH_MM.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int month = ctx.readIntAtCursor();

        ctx.setMonth(month);
        ctx.cursor().inc();
    }
}
