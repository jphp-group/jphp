package org.develnext.jphp.zend.ext.standard.date;

/**
 * Matches tokens with specified length.
 */
abstract class FixedLengthSymbol extends SymbolNode {
    private final int length;

    FixedLengthSymbol(Symbol symbol, int length) {
        super(symbol);
        this.length = length;
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return ctx.tokenAtCursor().length() == length;
    }
}
