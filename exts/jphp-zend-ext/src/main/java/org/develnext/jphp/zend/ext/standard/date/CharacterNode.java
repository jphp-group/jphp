package org.develnext.jphp.zend.ext.standard.date;

import java.util.function.Consumer;

class CharacterNode extends SymbolNode {
    private final char c;
    private final boolean caseSensitive;
    private final Consumer<DateTimeParserContext> apply;

    private CharacterNode(Symbol symbol, char c, Consumer<DateTimeParserContext> apply, boolean caseSensitive) {
        super(symbol);
        this.c = c;
        this.caseSensitive = caseSensitive;
        this.apply = apply;
    }

    static CharacterNode of(char c) {
        return new CharacterNode(Symbol.STRING, c, DateTimeParserContext.empty(), true);
    }

    static CharacterNode of(char c, Symbol symbol) {
        return new CharacterNode(symbol, c, DateTimeParserContext.empty(), true);
    }

    static CharacterNode of(char c, Consumer<DateTimeParserContext> apply) {
        return new CharacterNode(Symbol.STRING, c, apply, true);
    }

    static CharacterNode ofCaseInsensitive(char c) {
        return new CharacterNode(Symbol.STRING, c, DateTimeParserContext.empty(), false);
    }

    static CharacterNode ofCaseInsensitive(char c, Consumer<DateTimeParserContext> apply) {
        return new CharacterNode(Symbol.STRING, c, apply, false);
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        if (!super.matchesInternal(ctx)) {
            return false;
        }

        DateTimeTokenizer tokenizer = ctx.tokenizer();
        Token current = ctx.tokenAtCursor();
        return caseSensitive ? tokenizer.readChar(current) == c :
                Character.toLowerCase(tokenizer.readChar(current)) == Character.toLowerCase(c);
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        if (apply != DateTimeParserContext.empty())
            apply.accept(ctx);
        ctx.cursor().inc();
    }

    @Override
    public String toString() {
        if (caseSensitive)
            return "(" + c + ")";

        return "(" + Character.toLowerCase(c) + " OR " + Character.toUpperCase(c) + ")";
    }
}
