package org.develnext.jphp.zend.ext.standard.date;

import java.util.regex.Pattern;

class HourMinuteSecond extends VariableLengthSymbol {
    private static final Pattern PATTERN = Pattern.compile("((0?[1-9]|1[0-2])|([01][0-9]|2[0-4]))[0-5][0-9][0-5][0-9]");

    private HourMinuteSecond(int min, int max) {
        super(Symbol.DIGITS, min, max);
    }

    static HourMinuteSecond of(int min, int max) {
        return new HourMinuteSecond(min, max);
    }

    @Override
    public boolean matchesInternal(DateTimeParserContext ctx) {
        return PATTERN.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        Token token = ctx.tokenAtCursor();
        int start = token.start();
        int offset = token.length() - max();

        DateTimeTokenizer tokenizer = ctx.tokenizer();
        int hour = tokenizer.readInt(start, 2 + offset);
        int minute = tokenizer.readInt(start + 2 + offset, 2);
        int second = tokenizer.readInt(start + 4 + offset, 2);

        ctx.setHour(hour).setMinute(minute).setSecond(second);
        ctx.cursor().inc();
    }
}
