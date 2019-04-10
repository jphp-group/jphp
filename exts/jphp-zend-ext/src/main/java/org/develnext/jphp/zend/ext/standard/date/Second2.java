package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.TWO_DIGIT_SECOND;

import java.nio.CharBuffer;

class Second2 extends FixedLengthSymbol {
    private Second2() {
        super(Symbol.DIGITS, 2);
    }

    public static Second2 of() {
        return new Second2();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        CharBuffer input = ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()));
        return TWO_DIGIT_SECOND.matcher(input).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int sec = ctx.readIntAtCursor();

        ctx.setSecond(sec);
        ctx.cursor().inc();
    }
}
