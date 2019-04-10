package org.develnext.jphp.zend.ext.standard.date;

class CharacterNode extends SymbolNode {
    private final char c;
    private final boolean caseSensitive;

    CharacterNode(Symbol symbol, char c, boolean caseSensitive) {
        super(symbol);
        this.c = c;
        this.caseSensitive = caseSensitive;
    }

    static CharacterNode of(char c) {
        return new CharacterNode(Symbol.CHARACTER, c, true);
    }

    static CharacterNode ofCaseInsensitive(char c) {
        return new CharacterNode(Symbol.CHARACTER, c, false);
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
        ctx.cursor().inc();
    }
}
