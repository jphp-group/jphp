package org.develnext.jphp.zend.ext.standard.date;

class Microseconds extends SymbolNode {
    private Microseconds() {
        super(Symbol.DIGITS);
    }

    static Microseconds of() {
        return new Microseconds();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int l = ctx.tokenAtCursor().length();
        int m = 1_000_000;

        while (l-- > 0) m /= 10;

        int micro = ctx.readIntAtCursor() * m;

        ctx.setMicroseconds(micro).cursor().inc();
    }
}
