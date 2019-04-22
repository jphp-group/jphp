package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.COLON_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.MINUS_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.PLUS_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_II;

import java.nio.CharBuffer;
import java.time.ZoneId;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TimezoneCorrectionNode extends Node {
    private static final Pattern OFFSET_PREFIX = Pattern.compile("GMT|UTC|UT");
    private static final Pattern hh = Pattern.compile("0?[0-9]|1[0-2]");
    private static final Pattern hhMM = Pattern.compile("(0?[1-9]|1[0-2])[0-5][0-9]?");
    private static final Pattern CORRECTION = Pattern.compile("(0?[0-9]|1[0-2])(\\:)?([0-5][0-9])?");
    private static final Node SIGN = PLUS_NODE.or(MINUS_NODE);
    private ZoneId zoneId;
    private int nodeLength;

    TimezoneCorrectionNode() {
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        StringBuilder sb = new StringBuilder();
        Consumer<DateTimeParserContext> signedHour = c -> appendSignedHour(sb, c);

        PatternNode hh = PatternNode.ofDigits(TimezoneCorrectionNode.hh, signedHour);
        PatternNode hhMM = PatternNode.ofDigits(TimezoneCorrectionNode.hhMM, signedHour);
        PatternNode MM_NODE = PatternNode.ofDigits(MINUTE_II, c -> append(sb, c));
        PatternNode PREFIX_NODE = PatternNode.of(OFFSET_PREFIX, Symbol.STRING, c -> append(sb, c));

        Node node = PREFIX_NODE.optionalFollowedBy(SIGN.then(hh.then(COLON_NODE).then(MM_NODE).or(hhMM.or(hh))));

        int snapshot = ctx.cursor().value();

        if (node.matches(ctx)) {
            ctx.cursor().setValue(snapshot);
            node.apply(ctx);

            nodeLength = ctx.cursor().value() - snapshot;
            zoneId = ZoneId.of(sb.toString());
            return true;
        }

        return false;
    }

    private void appendSignedHour(StringBuilder sb, DateTimeParserContext c) {
        c.cursor().dec();
        if (c.isSymbolAtCursor(Symbol.PLUS) || c.isSymbolAtCursor(Symbol.MINUS)) {
            sb.append(c.readCharAtCursor());
        }
        c.cursor().inc();

        CharBuffer cb = c.readCharBufferAtCursor();

        Matcher matcher = CORRECTION.matcher(cb);
        if (!matcher.matches()) {
            // if this method is invoked this should not be executed.
            throw new IllegalStateException("DUP!");
        }

        sb.append(String.format("%02d", Integer.parseInt(matcher.group(1))));

        if (matcher.group(2) != null) {
            sb.append(matcher.group(2));
        }

        if (matcher.group(3) != null) {
            sb.append(matcher.group(3));
        }
    }

    private void append(StringBuilder sb, DateTimeParserContext c) {
        sb.append(c.readCharBufferAtCursor());
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        if (zoneId != null) {
            ctx.setTimezone(zoneId);
            ctx.cursor().setValue(ctx.cursor().value() + nodeLength);
            zoneId = null;
            nodeLength = 0;
        }
    }
}
