package org.develnext.jphp.zend.ext.standard.date;

import java.nio.CharBuffer;

class Microseconds extends SymbolNode {
    private Microseconds() {
        super(Symbol.DIGITS);
    }

    static Microseconds of() {
        return new Microseconds();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        CharBuffer cb = ctx.readCharBufferAtCursor();
        int mult = 100_000;

        while (cb.hasRemaining()) {
            if (cb.get() == '0')
                mult /= 10;
            else
                break;
        }

        int micro = ctx.readIntAtCursor();
        micro *= mult;

        ctx.setMicroseconds(micro).cursor().inc();
    }
}
