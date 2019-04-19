package org.develnext.jphp.zend.ext.standard.date;

import java.util.regex.Matcher;

import php.runtime.common.Pair;

public class RelativeTimeNumber extends Node {
    @Override
    boolean matches(DateTimeParserContext ctx) {
        return matchesInternal(ctx) != null;
    }

    private Pair<Long, String> matchesInternal(DateTimeParserContext ctx) {
        int snapshot = ctx.cursor().value();

        int sign = 1;
        if (ctx.isSymbolAtCursor(Symbol.PLUS) || ctx.isSymbolAtCursor(Symbol.MINUS)) {
            sign = ctx.isSymbolAtCursor(Symbol.PLUS) ? 1 : -1;
            ctx.cursor().inc();
        }

        if (!ctx.isSymbolAtCursor(Symbol.DIGITS)) {
            ctx.withCursorValue(snapshot);
            return null;
        }

        long value = ctx.readLongAtCursorAndInc() * sign;

        if (ctx.isSymbolAtCursor(Symbol.SPACE)) {
            ctx.cursor().inc();
        }

        if (!ctx.isSymbolAtCursor(Symbol.STRING)) {
            ctx.withCursorValue(snapshot);
            return null;
        }

        Matcher matcher = DateTimeTokenizer.UNIT.matcher(ctx.readCharBufferAtCursor());
        boolean unitMatches = matcher.matches();

        if (!unitMatches) {
            ctx.withCursorValue(snapshot);
            return null;
        }

        String unit = ctx.readStringAtCursorAndInc().toLowerCase();

        return new Pair<>(value, unit);
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        Pair<Long, String> pair = matchesInternal(ctx);
        long value = pair.getA();

        switch (pair.getB()) {
            case "year":
            case "years":
                ctx.plusYears(value);
                break;
            case "month":
            case "months":
                ctx.plusMonths(value);
                break;
            case "day":
            case "days":
                ctx.plusDays(value);
                break;
            case "hour":
            case "hours":
                ctx.plusHours(value);
                break;
            case "minute":
            case "minutes":
            case "min":
            case "mins":
                ctx.plusMinutes(value);
                break;
            case "second":
            case "seconds":
            case "sec":
            case "secs":
                ctx.plusSeconds(value);
                break;
            case "week":
            case "weeks":
                ctx.plusDays(value * 7L);
                break;
            case "weekday":
            case "weekdays":
                ctx.plusWeekDays(value);
                break;
            default:
                throw new IllegalArgumentException("Unknown unit: " + pair.getB());
        }
    }
}
