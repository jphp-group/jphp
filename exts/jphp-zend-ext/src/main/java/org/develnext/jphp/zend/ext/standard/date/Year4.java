package org.develnext.jphp.zend.ext.standard.date;

class Year4 extends FixedLengthSymbol {
    private Year4() {
        super(Symbol.DIGITS, 4);
    }

    static Year4 of() {
        return new Year4();
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return true;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int year = ctx.readIntAtCursor();

        ctx.setYear(year);
        ctx.cursor().inc();
    }
}
