package org.develnext.jphp.zend.ext.standard.date;

import java.util.StringJoiner;

/**
 * Matches tokens with specified symbol.
 */
class SymbolNode extends Node {
    private final Symbol symbol;

    SymbolNode(Symbol symbol) {
        this.symbol = symbol;
    }

    static SymbolNode of(Symbol symbol) {
        return new SymbolNode(symbol);
    }

    public Symbol symbol() {
        return symbol;
    }

    @Override
    public boolean matches(DateTimeParserContext ctx) {
        if (ctx.tokenAtCursor().symbol() == symbol) {
            boolean match = matchesInternal(ctx);
            if (match)
                ctx.cursor().inc();

            return match;
        }

        return false;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        ctx.cursor().inc();
    }

    public boolean matchesInternal(DateTimeParserContext ctx) {
        return true;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SymbolNode.class.getSimpleName() + "[", "]")
                .add("symbol=" + symbol)
                .toString();
    }

    Node followedByOptional(Node node) {
        return OrNode.of(GroupNode.of(this, node), this);
    }
}
