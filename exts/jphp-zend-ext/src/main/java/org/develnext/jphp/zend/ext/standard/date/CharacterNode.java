package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;

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
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        if (!super.matchesInternal(tokens, cursor, tokenizer)) {
            return false;
        }

        Token current = tokens.get(cursor.value());
        return caseSensitive ? tokenizer.readChar(current) == c :
                Character.toLowerCase(tokenizer.readChar(current)) == Character.toLowerCase(c);
    }
}
