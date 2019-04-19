package org.develnext.jphp.zend.ext.standard.date;

import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Consumer;

class StringNode extends FixedLengthSymbol {
    private final String value;
    private final BiFunction<String, String, Boolean> matcher;
    private final Consumer<DateTimeParserContext> applier;

    private StringNode(String value, boolean caseSensitive, Consumer<DateTimeParserContext> applier) {
        super(Symbol.STRING, value.length());
        this.value = value;
        this.matcher = caseSensitive ? String::equals : String::equalsIgnoreCase;
        this.applier = applier == DateTimeParserContext.empty() ?
                DateTimeParserContext.cursorIncrementer() :
                applier.andThen(DateTimeParserContext.cursorIncrementer());
    }

    static Node of(String value) {
        return of(value, DateTimeParserContext.empty());
    }

    static Node of(String value, Consumer<DateTimeParserContext> applier) {
        if (value.length() == 1)
            return CharacterNode.of(value.charAt(0));

        return new StringNode(value, true, applier);
    }

    static Node ofCaseInsensitive(String value) {
        return ofCaseInsensitive(value, DateTimeParserContext.empty());
    }

    static Node ofCaseInsensitive(String value, Consumer<DateTimeParserContext> applier) {
        if (value.length() == 1)
            return CharacterNode.ofCaseInsensitive(value.charAt(0));

        return new StringNode(value, false, applier);
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        if (!super.matchesInternal(ctx))
            return false;

        return matcher.apply(value, ctx.readStringAtCursor());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", StringNode.class.getSimpleName() + "[", "]")
                .add("value='" + value + "'")
                .toString();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        applier.accept(ctx);
    }
}
