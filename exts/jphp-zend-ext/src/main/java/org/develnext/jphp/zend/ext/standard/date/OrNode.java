package org.develnext.jphp.zend.ext.standard.date;

import java.util.function.Consumer;

class OrNode extends Node {
    private final Node l;
    private final Node r;
    private final Consumer<DateTimeParserContext> apply;
    //private Node matched;

    private OrNode(Node l, Node r, Consumer<DateTimeParserContext> apply) {
        this.l = l;
        this.r = r;
        this.apply = apply;
    }

    static OrNode of(Node l, Node r) {
        return new OrNode(l, r, DateTimeParserContext.empty());
    }

    static OrNode of(Node l, Node r, Consumer<DateTimeParserContext> apply) {
        return new OrNode(l, r, apply);
    }

    OrNode with(Consumer<DateTimeParserContext> apply) {
        return of(l, r, apply);
    }

    @Override
    public boolean matches(DateTimeParserContext ctx) {
        return l.matches(ctx) || r.matches(ctx);
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int snapshot = ctx.cursor().value();
        Node matched = null;

        if (l.matches(ctx)) {
            matched = l;
        } else if (r.matches(ctx)) {
            matched = r;
        }

        if (matched != null) {
            ctx = ctx.withCursorValue(snapshot);

            if (apply == DateTimeParserContext.empty()) {
                matched.apply(ctx);
            } else {
                apply.accept(ctx);
            }
        }
    }

    @Override
    public String toString() {
        return "(" + l + " OR " + r + ")";
    }
}
