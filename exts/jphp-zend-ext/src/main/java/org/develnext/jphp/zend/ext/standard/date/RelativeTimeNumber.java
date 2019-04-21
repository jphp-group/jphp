package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.Adjusters.relativeUnit;

import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
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

        // try to find "ago" token.
        if (ctx.isSymbolAtCursor(Symbol.SPACE)) {
            snapshot = ctx.cursor().value();
            ctx.cursor().inc();

            if (ctx.isSymbolAtCursor(Symbol.STRING) && "ago".equalsIgnoreCase(ctx.readStringAtCursor())) {
                value = -value;
                ctx.cursor().inc();
            } else {
                ctx.withCursorValue(snapshot);
            }
        }

        return new Pair<>(value, unit);
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        Pair<Long, String> pair = matchesInternal(ctx);
        Pair<TemporalAdjuster, TemporalField> pair1 = relativeUnit(pair.getB(), pair.getA());
        ctx.withAdjuster(pair1.getA(), pair1.getB());
    }
}
