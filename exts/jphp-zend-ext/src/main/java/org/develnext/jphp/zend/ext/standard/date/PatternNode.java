package org.develnext.jphp.zend.ext.standard.date;

import java.util.StringJoiner;
import java.util.function.Consumer;
import java.util.regex.Pattern;

class PatternNode extends SymbolNode {
    private final Pattern pattern;
    private final Consumer<DateTimeParserContext> consumer;

    private PatternNode(Pattern pattern, Symbol symbol, Consumer<DateTimeParserContext> consumer) {
        super(symbol);
        this.pattern = pattern;
        this.consumer = consumer;
    }

    static PatternNode ofDigits(Pattern pattern, Consumer<DateTimeParserContext> adjuster) {
        return new PatternNode(pattern, Symbol.DIGITS, adjuster);
    }

    static PatternNode ofDigits(Pattern pattern) {
        return new PatternNode(pattern, Symbol.DIGITS, DateTimeParserContext.empty());
    }

    static PatternNode of(Pattern pattern, Symbol symbol, Consumer<DateTimeParserContext> consumer) {
        return new PatternNode(pattern, symbol, consumer);
    }

    static PatternNode ofString(Pattern pattern, Consumer<DateTimeParserContext> consumer) {
        return new PatternNode(pattern, Symbol.STRING, consumer);
    }

    static PatternNode of(Pattern pattern, Symbol symbol) {
        return new PatternNode(pattern, symbol, DateTimeParserContext.empty());
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return pattern.matcher(ctx.readCharBufferAtCursor()).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        consumer.accept(ctx);
        ctx.cursor().inc();
    }

    public PatternNode withConsumer(Consumer<DateTimeParserContext> consumer) {
        return new PatternNode(pattern, symbol(), consumer);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PatternNode.class.getSimpleName() + "[", "]")
                .add("pattern=" + pattern.pattern())
                .toString();
    }
}
