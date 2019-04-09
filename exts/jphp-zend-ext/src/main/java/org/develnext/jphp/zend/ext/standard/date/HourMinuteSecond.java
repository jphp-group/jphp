package org.develnext.jphp.zend.ext.standard.date;

import java.util.List;
import java.util.regex.Pattern;

class HourMinuteSecond extends VariableLengthSymbol {
    private static final Pattern PATTERN = Pattern.compile("((0?[1-9]|1[0-2])|([01][0-9]|2[0-4]))[0-5][0-9][0-5][0-9]");

    HourMinuteSecond(int min, int max) {
        super(Symbol.DIGITS, min, max);
    }

    static HourMinuteSecond of(int min, int max) {
        return new HourMinuteSecond(min, max);
    }

    @Override
    public boolean matchesInternal(List<Token> tokens, Cursor cursor, DateTimeTokenizer tokenizer) {
        return PATTERN.matcher(tokenizer.readCharBuffer(tokens.get(cursor.value()))).matches();
    }
}
