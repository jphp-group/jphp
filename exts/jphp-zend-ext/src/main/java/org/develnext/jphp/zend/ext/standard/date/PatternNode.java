package org.develnext.jphp.zend.ext.standard.date;

import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.regex.Pattern;

class PatternNode extends SymbolNode {
    private final Pattern pattern;
    private final Consumer<DateTimeParserContext> adjuster;

    private PatternNode(Pattern pattern, Symbol symbol, Consumer<DateTimeParserContext> adjuster) {
        super(symbol);
        this.pattern = pattern;
        this.adjuster = adjuster;
    }

    static PatternNode ofDigits(Pattern pattern, Consumer<DateTimeParserContext> adjuster) {
        return new PatternNode(pattern, Symbol.DIGITS, adjuster);
    }

    static PatternNode ofDigits(Pattern pattern) {
        return new PatternNode(pattern, Symbol.DIGITS, DateTimeParserContext.empty());
    }

    static PatternNode of(Pattern pattern, Symbol symbol, Consumer<DateTimeParserContext> adjuster) {
        return new PatternNode(pattern, symbol, adjuster);
    }

    static Node of(Pattern pattern, Symbol symbol) {
        return new PatternNode(pattern, symbol, DateTimeParserContext.empty());
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return pattern.matcher(ctx.readCharBufferAtCursor()).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        adjuster.accept(ctx);
        ctx.cursor().inc();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PatternNode.class.getSimpleName() + "[", "]")
                .add("pattern=" + pattern.pattern())
                .toString();
    }
}
