package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.TWO_DIGIT_MONTH;

import java.util.List;

class Month2 extends FixedLengthSymbol {
    Month2() {
        super(Symbol.DIGITS, 2);
    }

    public static Month2 of() {
        return new Month2();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        boolean matches = TWO_DIGIT_MONTH.matcher(tokenizer.readCharBuffer(tokens.get(cursor.value()))).matches();
        return matches;
    }
}
