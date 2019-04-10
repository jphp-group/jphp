package org.develnext.jphp.zend.ext.standard.date;

import java.util.StringJoiner;

class OrNode extends Node {
    private final Node l;
    private final Node r;

    OrNode(Node l, Node r) {
        this.l = l;
        this.r = r;
    }

    static OrNode of(Node l, Node r) {
        return new OrNode(l, r);
    }

    @Override
    public boolean matches(DateTimeParserContext ctx) {
        return l.matches(ctx) || r.matches(ctx);
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int snapshot = ctx.cursor().value();
        if (l.matches(ctx)) {
            l.apply(ctx.withCursorValue(snapshot));
        } else if (r.matches(ctx)) {
            r.apply(ctx.withCursorValue(snapshot));
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrNode.class.getSimpleName() + "[", "]")
                .add("l=" + l)
                .add("r=" + r)
                .toString();
    }
}
