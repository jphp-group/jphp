package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;

/**
 * Matches tokens with specified length.
 */
class FixedLengthSymbol extends SymbolNode {
    private final int length;

    FixedLengthSymbol(Symbol symbol, int length) {
        super(symbol);
        this.length = length;
    }

    static FixedLengthSymbol of(Symbol symbol, int length) {
        return new FixedLengthSymbol(symbol, length);
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        return tokens.get(cursor.value()).length() == length;
    }
}
