package org.develnext.jphp.zend.ext.standard.date;

class UnixTimestamp extends SymbolNode {
    private UnixTimestamp() {
        super(Symbol.DIGITS);
    }

    static UnixTimestamp of() {
        return new UnixTimestamp();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        // FIXME: handle overflow
        long timestamp = ctx.readLongAtCursor();
        ctx.setUnixTimestamp(timestamp).cursor().inc();
    }
}
