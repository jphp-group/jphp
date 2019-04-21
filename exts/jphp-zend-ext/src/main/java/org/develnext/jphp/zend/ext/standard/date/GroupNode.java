package org.develnext.jphp.zend.ext.standard.date;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.function.Consumer;

class GroupNode extends Node implements Iterable<Node> {
    public static final int PRIORITY_HIGH = 1000;
    public static final int PRIORITY_NORMAL = PRIORITY_HIGH - 500;
    public static final int PRIORITY_LOW = PRIORITY_NORMAL - 500;

    private final String name;
    private final boolean relative;
    private final int priority;
    private final Node[] nodes;
    private final Consumer<DateTimeParserContext> afterApply;

    GroupNode(String name, Node[] nodes, boolean relative, int priority, Consumer<DateTimeParserContext> afterApply) {
        this.name = name;
        this.nodes = nodes;
        this.relative = relative;
        this.priority = priority;
        this.afterApply = afterApply;
    }

    static GroupNode of(String name, Node... nodes) {
        return new GroupNode(name, nodes, false, PRIORITY_LOW, DateTimeParserContext.empty());
    }

    static GroupNode of(String name, Consumer<DateTimeParserContext> afterApply, Node... nodes) {
        return new GroupNode(name, nodes, false, PRIORITY_LOW, afterApply);
    }

    static GroupNode of(Node... nodes) {
        return new GroupNode("noname", nodes, false, PRIORITY_LOW, DateTimeParserContext.empty());
    }

    static GroupNodeBuilder builder() {
        return new GroupNodeBuilder();
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        Iterator<Node> nodeIt = iterator();
        Cursor cursor = ctx.cursor();
        int mark = cursor.value();

        while (ctx.hasMoreTokens() && nodeIt.hasNext()) {
            Node node = nodeIt.next();

            if (!node.matches(ctx)) {
                cursor.setValue(mark);
                return false;
            }
        }

        if (nodeIt.hasNext()) {
            cursor.setValue(mark);
            return false;
        }

        return true;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        if (nodes.length == 1) {
            nodes[0].apply(ctx);
        } else {
            for (Node node : nodes) {
                node.apply(ctx);
            }
        }

        afterApply.accept(ctx);
    }

    boolean isRelative() {
        return relative;
    }

    int priority() {
        return priority;
    }

    @Override
    public Iterator<Node> iterator() {
        if (nodes.length == 1)
            return Collections.singletonList(nodes[0]).iterator();

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
                .add("relative=" + relative)
                .toString();
    }

    static final class GroupNodeBuilder {
        private String name = "noname";
        private boolean relative;
        private int priority = PRIORITY_LOW;
        private Node[] nodes;
        private Consumer<DateTimeParserContext> afterApply = DateTimeParserContext.empty();

        private GroupNodeBuilder() {
        }

        public GroupNodeBuilder name(String name) {
            this.name = name;
            return this;
        }

        GroupNodeBuilder relative(boolean relative) {
            this.relative = relative;
            return this;
        }

        GroupNodeBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        GroupNodeBuilder priorityHigh() {
            return priority(PRIORITY_HIGH);
        }

        public GroupNodeBuilder priorityNormal() {
            return priority(PRIORITY_NORMAL);
        }

        public GroupNodeBuilder priorityLow() {
            return priority(PRIORITY_LOW);
        }

        GroupNodeBuilder nodes(Node... nodes) {
            this.nodes = nodes;
            return this;
        }

        public GroupNodeBuilder afterApply(Consumer<DateTimeParserContext> afterApply) {
            this.afterApply = afterApply;
            return this;
        }

        public GroupNodeBuilder afterApplyResetTime() {
            return afterApply(DateTimeParserContext::atStartOfDay);
        }

        GroupNode build() {
            return new GroupNode(name, nodes, relative, priority, afterApply);
        }
    }
}
