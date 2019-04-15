package org.develnext.jphp.zend.ext.standard.date;

import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.function.Consumer;

class GroupNode extends Node implements Iterable<Node> {
    private final String name;
    private final Node[] nodes;
    private final Consumer<DateTimeParserContext> afterApply;

    GroupNode(String name, Node[] nodes, Consumer<DateTimeParserContext> afterApply) {
        this.name = name;
        this.nodes = nodes;
        this.afterApply = afterApply;
    }

    static GroupNode of(String name, Node... nodes) {
        return new GroupNode(name, nodes, DateTimeParserContext.empty());
    }

    static GroupNode of(String name, Consumer<DateTimeParserContext> afterApply, Node... nodes) {
        return new GroupNode(name, nodes, afterApply);
    }

    static GroupNode of(Node... nodes) {
        return new GroupNode("noname", nodes, DateTimeParserContext.empty());
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        Iterator<Node> nodeIt = iterator();
        Cursor cursor = ctx.cursor();
        int mark = cursor.value();

        for (; ctx.hasMoreTokens() && nodeIt.hasNext(); ) {
            Node node = nodeIt.next();
            boolean matches = node.matches(ctx);

            if (!matches) {
                cursor.setValue(mark);
                return false;
            }
        }

        if (nodeIt.hasNext()) {
            cursor.setValue(mark);
            return false;
        }

        // System.out.println("=== " + name + " ===");
        return true;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        for (Node node : nodes) {
            node.apply(ctx);
        }

        afterApply.accept(ctx);
    }

    @Override
    public Iterator<Node> iterator() {
        return Arrays.asList(nodes).iterator();
    }

    Node[] nodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GroupNode.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("nodes=" + Arrays.toString(nodes))
                .toString();
    }
}
