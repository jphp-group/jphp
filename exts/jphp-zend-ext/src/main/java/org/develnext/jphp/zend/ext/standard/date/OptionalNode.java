package org.develnext.jphp.zend.ext.standard.date;

public class OptionalNode extends Node {
    private final Node realNode;

    OptionalNode(Node realNode) {
        this.realNode = realNode;
    }

    static OptionalNode of(Node realNode) {
        return new OptionalNode(realNode);
    }

    @Override
    public boolean matches(DateTimeParserContext ctx) {
        realNode.matches(ctx);

        return true;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        if (matches(ctx)) {
            realNode.apply(ctx);
        }
    }
}
