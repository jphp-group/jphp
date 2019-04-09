package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_24;

import java.nio.CharBuffer;
import java.util.List;

/**
 * Matches the hour in 24 hour format: "04", "07", "19"
 */
class Hour24 extends FixedLengthSymbol {
    Hour24() {
        super(Symbol.DIGITS, 2);
    }

    static Hour24 of() {
        return new Hour24();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        CharBuffer cb = tokenizer.readCharBuffer(tokens.get(cursor.value()));
        return HOUR_24.matcher(cb).matches();
    }
}
