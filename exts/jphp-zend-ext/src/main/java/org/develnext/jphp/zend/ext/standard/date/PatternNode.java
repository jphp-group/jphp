package org.develnext.jphp.zend.ext.standard.date;

import java.time.ZonedDateTime;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

class PatternNode extends SymbolNode {
    private final Pattern pattern;
    private final BiFunction<Integer, ZonedDateTime, ZonedDateTime> adjuster;

    private PatternNode(Pattern pattern, Symbol symbol, BiFunction<Integer, ZonedDateTime, ZonedDateTime> adjuster) {
        super(symbol);
        this.pattern = pattern;
        this.adjuster = adjuster;
    }

    static PatternNode ofDigits(Pattern pattern, BiFunction<Integer, ZonedDateTime, ZonedDateTime> adjuster) {
        return new PatternNode(pattern, Symbol.DIGITS, adjuster);
    }

    static PatternNode ofDigits(Pattern pattern) {
        return new PatternNode(pattern, Symbol.DIGITS, (integer, zonedDateTime) -> zonedDateTime);
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return pattern.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        int value = ctx.readIntAtCursor();
        ctx.dateTime(adjuster.apply(value, ctx.dateTime()));
    }
}
