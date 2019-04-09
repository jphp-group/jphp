package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.TWO_DIGIT_SECOND;

import java.nio.CharBuffer;
import java.util.List;

class Second2 extends FixedLengthSymbol {
    Second2() {
        super(Symbol.DIGITS, 2);
    }

    public static Second2 of() {
        return new Second2();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        CharBuffer input = tokenizer.readCharBuffer(tokens.get(cursor.value()));
        return TWO_DIGIT_SECOND.matcher(input).matches();
    }
}
