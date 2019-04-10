package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_II;

import java.time.ZoneId;
import java.util.regex.Pattern;

class TimezoneCorrectionNode extends Node {
    private static final Pattern HOUR_hh = Pattern.compile("[+-](0?[1-9]|1[0-2])");
    private static final Pattern TZ_CORRECTION = Pattern.compile("[+-](0?[1-9]|1[0-2]):?[0-5][0-9]");
    private ZoneId zoneId;

    TimezoneCorrectionNode() {
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        int snapshot = ctx.cursor().value();

        StringBuilder sb = new StringBuilder();
        if (ctx.tokenAtCursor().symbol() == Symbol.STRING) {
            sb.append(ctx.readCharBufferAtCursor());
            ctx.cursor().inc(); // consume the timezone
        }

        if (ctx.tokenAtCursor().symbol() != Symbol.DIGITS) {
            ctx.withCursorValue(snapshot);
            return false;
        }

        int length = ctx.tokenAtCursor().length();
        Pattern pattern = HOUR_hh;

        if (length == 5) { // +0700
            pattern = TZ_CORRECTION;
        }

        if (notMatch(ctx, snapshot, sb, pattern)) {
            return false;
        }

        boolean hasShortLength = length == 5 || length == 3 || length == 2;
        boolean notColon = ctx.hasMoreTokens() && ctx.tokenAtCursor().symbol() != Symbol.COLON;
        if ((notColon || !ctx.hasMoreTokens()) && hasShortLength) { // -07, -0700
            zoneId = ZoneId.of(sb.toString());
            return true;
        }

        if (ctx.tokenAtCursor().symbol() == Symbol.COLON)
            ctx.cursor().inc();

        sb.append(':');

        if (ctx.tokenAtCursor().symbol() == Symbol.DIGITS) {
            if (notMatch(ctx, snapshot, sb, MINUTE_II)) {
                return false;
            }
        } else {
            sb.append("00");
        }

        zoneId = ZoneId.of(sb.toString());
        return true;
    }

    private boolean notMatch(DateTimeParserContext ctx, int snapshot, StringBuilder sb, Pattern pattern) {
        PatternNode node = PatternNode.ofDigits(pattern, context -> sb.append(context.readCharBufferAtCursor()));
        if (!node.matches(ctx)) {
            ctx.withCursorValue(snapshot);
            return true;
        }

        ctx.cursor().dec();
        node.apply(ctx);
        return false;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        if (zoneId != null) {
            ctx.setTimezone(zoneId);
            zoneId = null;
            ctx.cursor().inc();
        }
    }
}
