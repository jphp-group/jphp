package org.develnext.jphp.zend.ext.standard.date;

class CursorAwareNode extends Node implements Comparable<CursorAwareNode> {
    private final int cursor;
    private final GroupNode node;

    CursorAwareNode(int cursor, GroupNode node) {
        this.cursor = cursor;
        this.node = node;
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        return node.matches(ctx.withCursorValue(cursor));
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        node.apply(ctx.withCursorValue(cursor));
    }

    @Override
    public int compareTo(CursorAwareNode o) {
        return Integer.compare(node.priority(), o.node.priority());
    }
}
