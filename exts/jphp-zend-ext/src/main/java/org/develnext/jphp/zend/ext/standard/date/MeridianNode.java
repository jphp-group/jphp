package org.develnext.jphp.zend.ext.standard.date;

class MeridianNode extends SymbolNode {
    private MeridianNode() {
        super(Symbol.MERIDIAN);
    }

    static MeridianNode of() {
        return new MeridianNode();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        String s = ctx.readStringAtCursor().replaceAll("\\.", "").toLowerCase();

        ctx.setMeridian(s.equals("am")).cursor().inc();
    }
}
