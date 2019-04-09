package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;
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
    public boolean matches(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        return l.matches(tokens, cursor, tokenizer) || r.matches(tokens, cursor, tokenizer);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", OrNode.class.getSimpleName() + "[", "]")
                .add("l=" + l)
                .add("r=" + r)
                .toString();
    }
}
