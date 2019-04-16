package org.develnext.jphp.zend.ext.standard.date;

class UnixTimestamp extends SymbolNode {
    private UnixTimestamp() {
        super(Symbol.DIGITS);
    }

    static UnixTimestamp of() {
        return new UnixTimestamp();
    }

    @Override
    public boolean matches(DateTimeParserContext ctx) {
        if (ctx.isSymbolAtCursor(Symbol.MINUS))
            ctx.cursor().inc();

        return super.matches(ctx);
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int sign = 1;
        if (ctx.isSymbolAtCursor(Symbol.MINUS)) {
            ctx.cursor().inc();
            sign = -1;
        }

        // FIXME: handle overflow
        long timestamp = ctx.readLongAtCursor() * sign;
        ctx.setUnixTimestamp(timestamp).cursor().inc();
    }
}
