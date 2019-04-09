package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;

abstract class VariableLengthSymbol extends SymbolNode {
    private final int min;
    private final int max;

    VariableLengthSymbol(Symbol symbol, int min, int max) {
        super(symbol);
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        Token current = tokens.get(cursor.value());
        return current.length() >= min && current.length() <= max;
    }
}
