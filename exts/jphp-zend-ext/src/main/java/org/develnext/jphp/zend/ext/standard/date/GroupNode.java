package org.develnext.jphp.zend.ext.standard.date;

import java.util.Arrays;
import java.util.Iterator;
import java.util.StringJoiner;

class GroupNode extends Node implements Iterable<Node> {
    private final String name;
    private final Node[] nodes;

    GroupNode(String name, Node[] nodes) {
        this.name = name;
        this.nodes = nodes;
    }

    static GroupNode of(String name, Node... nodes) {
        return new GroupNode(name, nodes);
    }

    static GroupNode of(Node... nodes) {
        return new GroupNode("noname", nodes);
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        Iterator<Node> nodeIt = iterator();
        Cursor cursor = ctx.cursor();
        int mark = cursor.value();

        for (; cursor.value() < ctx.tokens().size() && nodeIt.hasNext();) {
            Node node = nodeIt.next();
            boolean matches = node.matches(ctx);

            if (!matches) {
                cursor.setValue(mark);
                return false;
            }
        }

        System.out.println("=== " + name + " ===");
        return true;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        for (Node node : nodes) {
            node.apply(ctx);
        }
    }

    @Override
    public Iterator<Node> iterator() {
        return Arrays.asList(nodes).iterator();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GroupNode.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("nodes=" + Arrays.toString(nodes))
                .toString();
    }
}
