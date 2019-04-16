package org.develnext.jphp.zend.ext.standard.date;

class ZeroOrMore extends Node {
    private final Node node;

    private ZeroOrMore(Node node) {
        this.node = node;
    }

    static ZeroOrMore of(Node node) {
        return new ZeroOrMore(node);
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        while (node.matches(ctx)) { /*empty*/ }

        return true;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        matches(ctx);
    }
}
