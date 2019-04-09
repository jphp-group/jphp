package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_12;

import java.nio.CharBuffer;
import java.util.List;

class Hour12 extends VariableLengthSymbol {
    Hour12() {
        super(Symbol.DIGITS, 1, 2);
    }

    static Hour12 of() {
        return new Hour12();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        CharBuffer cb = tokenizer.readCharBuffer(tokens.get(cursor.value()));
        return HOUR_12.matcher(cb).matches();
    }
}
