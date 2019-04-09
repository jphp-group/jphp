package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_12;

import java.nio.CharBuffer;

class Hour12 extends VariableLengthSymbol {
    private Hour12() {
        super(Symbol.DIGITS, 1, 2);
    }

    static Hour12 of() {
        return new Hour12();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        CharBuffer cb = ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()));
        return HOUR_12.matcher(cb).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        // FIXME create new node
        int hour = ctx.readIntAtCursor();
        ctx.dateTime(ctx.dateTime().withHour(hour));
    }
}
