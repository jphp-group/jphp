package org.develnext.jphp.zend.ext.standard.date;

import java.nio.CharBuffer;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Matches following pattern: YYYYMMDD
 */
class YearMonthDay extends FixedLengthSymbol {
    private static final Pattern PATTERN = Pattern.compile("[0-9]{4}[0-1][0-9]([01][0-9]|2[0-4])");

    YearMonthDay() {
        super(Symbol.DIGITS, 8);
    }

    static YearMonthDay of() {
        return new YearMonthDay();
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        CharBuffer cb = tokenizer.readCharBuffer(tokens.get(cursor.value()));

        return PATTERN.matcher(cb).matches();
    }
}
