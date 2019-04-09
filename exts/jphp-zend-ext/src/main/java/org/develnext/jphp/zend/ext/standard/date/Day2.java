package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.TWO_DIGIT_DAY;

import java.util.List;

class Day2 extends FixedLengthSymbol {
    Day2() {
        super(Symbol.DIGITS, 2);
    }

    public static Day2 of() {
        return new Day2();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        boolean matches = TWO_DIGIT_DAY.matcher(tokenizer.readCharBuffer(tokens.get(cursor.value()))).matches();
        return matches;
    }
}
