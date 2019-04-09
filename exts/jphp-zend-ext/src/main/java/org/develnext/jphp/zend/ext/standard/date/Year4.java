package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;

class Year4 extends FixedLengthSymbol {
    Year4() {
        super(Symbol.DIGITS, 4);
    }

    static Year4 of() {
        return new Year4();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        return true;
    }
}
