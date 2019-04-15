package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.COLON_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.MINUS_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.PLUS_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.TWO_DIGIT_MINUTE;

import java.nio.CharBuffer;
import java.time.ZoneId;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class TimezoneCorrectionNode extends Node {
    private static final Pattern OFFSET_PREFIX = Pattern.compile("GMT|UTC|UT");
    private static final Pattern hhSigned = Pattern.compile("[+-]0?[1-9]|1[0-2]");
    private static final Pattern hh = Pattern.compile("0?[1-9]|1[0-2]");
    private static final Pattern hhMM = Pattern.compile("(0?[1-9]|1[0-2])[0-5][0-9]?");
    private static final Pattern hhMMSigned = Pattern.compile("[+-](0?[1-9]|1[0-2])[0-5][0-9]?");
    private static final Pattern OPT_SIGNED_CORRECTION = Pattern.compile("([+-])?(0?[1-9]|1[0-2])(\\:)?([0-5][0-9])?");
    private ZoneId zoneId;
    private int nodeLength;

    TimezoneCorrectionNode() {
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        StringBuilder sb = new StringBuilder();
        PatternNode MM_NODE = PatternNode.ofDigits(TWO_DIGIT_MINUTE, c -> append(sb, c));
        PatternNode PREFIX_NODE = PatternNode.of(OFFSET_PREFIX, Symbol.STRING, c -> append(sb, c));
        Consumer<DateTimeParserContext> signedHour = c -> appendSignedHour(sb, c);

        GroupNode[] groupNodes = {
                GroupNode.of(
                        "OP? [+-]hh:?MM",
                        PREFIX_NODE
                                .optionalFollowedBy(PatternNode.ofDigits(hhSigned, signedHour))
                                .followedByOptional(COLON_NODE),
                        MM_NODE
                ),
                GroupNode.of(
                        "OP? [+-]hh(:MM)?",
                        PREFIX_NODE
                                .optionalFollowedBy(PatternNode.ofDigits(hhSigned, signedHour))
                                .followedByOptional(COLON_NODE.then(MM_NODE))
                ),
                GroupNode.of(
                        "OP? [+-]hhMM",
                        PREFIX_NODE.optionalFollowedBy(PatternNode.ofDigits(hhMMSigned, signedHour))
                ),
                GroupNode.of(
                        "[+-] hh (:MM)?",
                        PLUS_NODE.or(MINUS_NODE)
                                .then(PatternNode.ofDigits(hh, signedHour)
                                        .followedByOptional(COLON_NODE.then(MM_NODE))
                                        .or(PatternNode.ofDigits(hhMM, signedHour))
                                )
                ),
        };

        for (GroupNode groupNode : groupNodes) {
            int snapshot = ctx.cursor().value();
            boolean matches = groupNode.matches(ctx);

            if (matches) {
                ctx.cursor().setValue(snapshot);
                groupNode.apply(ctx);

                nodeLength = ctx.cursor().value() - snapshot;
                zoneId = ZoneId.of(sb.toString());
                return true;
            }
        }

        return false;
    }

    private void appendSignedHour(StringBuilder sb, DateTimeParserContext c) {
        CharBuffer cb = c.readCharBufferAtCursor();

        Matcher matcher = OPT_SIGNED_CORRECTION.matcher(cb);
        if (!matcher.matches()) {
            // if this method is invoked this should not be executed.
            throw new IllegalStateException("DUP!");
        }

        if (matcher.group(1) != null) {
            sb.append(matcher.group(1));
        } else {
            sb.append(c.tokenizer().readChar(c.tokenAtCursor().start() - 1));
        }

        sb.append(String.format("%02d", Integer.parseInt(matcher.group(2))));

        if (matcher.group(3) != null) {
            sb.append(matcher.group(3));
        }

        if (matcher.group(4) != null) {
            sb.append(matcher.group(4));
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
