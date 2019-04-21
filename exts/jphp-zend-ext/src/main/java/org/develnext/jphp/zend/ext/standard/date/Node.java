package org.develnext.jphp.zend.ext.standard.date;

abstract class Node {
    abstract boolean matches(DateTimeParserContext ctx);

    abstract void apply(DateTimeParserContext ctx);

    Node followedByOptional(Node node) {
        return OrNode.of(GroupNode.builder().nodes(this, node).build(), this);
    }

    Node optionalFollowedBy(Node node) {
        return OrNode.of(GroupNode.builder().nodes(this, node).build(), node);
    }

    OrNode or(Node alt) {
        return OrNode.of(this, alt);
    }

    AndNode then(Node node) {
        return AndNode.of(this, node);
    }
}
