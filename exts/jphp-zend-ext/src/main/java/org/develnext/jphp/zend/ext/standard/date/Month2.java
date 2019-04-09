package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.TWO_DIGIT_MONTH;

class Month2 extends FixedLengthSymbol {
    Month2() {
        super(Symbol.DIGITS, 2);
    }

    public static Month2 of() {
        return new Month2();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        boolean matches = TWO_DIGIT_MONTH.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
        return matches;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int month = ctx.readIntAtCursor();

        ctx.dateTime(ctx.dateTime().withMonth(month));
    }
}
