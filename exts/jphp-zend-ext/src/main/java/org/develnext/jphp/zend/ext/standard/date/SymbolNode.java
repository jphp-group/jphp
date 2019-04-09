package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;
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
    public boolean matches(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        if (tokens.get(cursor.value()).symbol() == symbol) {
            return matchesInternal(tokens, cursor, tokenizer);
        }

        return false;
    }

    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        return true;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SymbolNode.class.getSimpleName() + "[", "]")
                .add("symbol=" + symbol)
                .toString();
    }
}
