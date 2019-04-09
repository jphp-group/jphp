package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;
import java.util.regex.Pattern;

class PatternNode extends SymbolNode {
    private final Pattern pattern;

    PatternNode(Pattern pattern, Symbol symbol) {
        super(symbol);
        this.pattern = pattern;
    }

    static PatternNode ofDigits(Pattern pattern) {
        return new PatternNode(pattern, Symbol.DIGITS);
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        return pattern.matcher(tokenizer.readCharBuffer(tokens.get(cursor.value()))).matches();
    }
}
