package org.develnext.jphp.zend.ext.standard.date;

import java.util.StringJoiner;

class NotNode extends Node {
    private final Node node;

    private NotNode(Node node) {
        this.node = node;
    }

    static NotNode of(Node node) {
        return new NotNode(node);
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        boolean matches = node.matches(ctx);
        return !matches;
    }

    @Override
    void apply(DateTimeParserContext ctx) {

    }

    @Override
    public String toString() {
        return new StringJoiner(", ", NotNode.class.getSimpleName() + "[", "]")
                .add("node=" + node)
                .toString();
    }
}
