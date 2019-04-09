package org.develnext.jphp.zend.ext.standard.date;

class AndNode extends Node {
    private final Node l;
    private final Node r;

    AndNode(Node l, Node r) {
        this.l = l;
        this.r = r;
    }

    static OrNode of(Node l, Node r) {
        return new OrNode(l, r);
    }

    @Override
    public boolean matches(DateTimeParserContext ctx) {
        return l.matches(ctx) && r.matches(ctx);
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        Cursor cursor = ctx.cursor();
        int snapshot = cursor.value();
        if (matches(ctx)) {
            cursor.setValue(snapshot);
            l.apply(ctx);
            r.apply(ctx);
        }
    }
}
