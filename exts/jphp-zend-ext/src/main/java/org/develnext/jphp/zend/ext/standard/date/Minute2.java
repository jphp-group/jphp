package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.TWO_DIGIT_MINUTE;

import java.util.List;

class Minute2 extends FixedLengthSymbol {
    Minute2() {
        super(Symbol.DIGITS, 2);
    }

    public static Minute2 of() {
        return new Minute2();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        return TWO_DIGIT_MINUTE.matcher(tokenizer.readCharBuffer(tokens.get(cursor.value()))).matches();
    }
}
