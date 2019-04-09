package org.develnext.jphp.zend.ext.standard.date;

abstract class VariableLengthSymbol extends SymbolNode {
    private final int min;
    private final int max;

    VariableLengthSymbol(Symbol symbol, int min, int max) {
        super(symbol);
        this.min = min;
        this.max = max;
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        Token current = ctx.tokens().get(ctx.cursor().value());
        return current.length() >= min && current.length() <= max;
    }
}
