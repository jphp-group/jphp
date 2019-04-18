package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.SLASH_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.UNDERSCORE_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.charAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.charBufferAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.cursorIncrementer;

import java.util.function.Consumer;
import java.util.regex.Pattern;

class TimezoneNode extends Node {
    private static final Pattern TZ_PATTERN = Pattern.compile("\\(?[A-Za-z]{1,6}\\)?|[A-Z][a-z]+([_/][A-Z][a-z]+)+");
    private static final Pattern TZ_SHORT = Pattern.compile("[A-Za-z]{1,6}");
    private static final CharacterNode BRACKET_OPEN = CharacterNode.of('(');
    private static final CharacterNode BRACKET_CLOSE = CharacterNode.of(')');
    private static final Pattern REGION = Pattern.compile("[A-Z][a-z]+");

    public static TimezoneNode of() {
        return new TimezoneNode();
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        boolean matches = matchesInternal(ctx, null);

        return matches;
    }

    private boolean matchesInternal(DateTimeParserContext ctx, StringBuilder sb) {
        int snapshot = ctx.cursor().value();

        Consumer<DateTimeParserContext> appender = charBufferAppender(sb);

        PatternNode TZ_SHORT_NODE = PatternNode.of(TZ_SHORT, Symbol.STRING, appender);
        Node underscoreOrSlash = UNDERSCORE_NODE.or(SLASH_NODE).with(charAppender(sb).andThen(cursorIncrementer()));
        Node shortTimezone = BRACKET_OPEN.then(TZ_SHORT_NODE).then(BRACKET_CLOSE).or(TZ_SHORT_NODE);
        Node regionBaseTimezoneNode = PatternNode.of(REGION, Symbol.STRING, appender)
                .then(OneOrMore.of(underscoreOrSlash, PatternNode.of(REGION, Symbol.STRING, appender)));

        OrNode tz = regionBaseTimezoneNode.or(shortTimezone);
        boolean matches = tz.matches(ctx);

        if (sb != null) {
            ctx = ctx.withCursorValue(snapshot);
            tz.apply(ctx);
        }
        return matches;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        StringBuilder tzBuff = new StringBuilder();
        boolean matches = matchesInternal(ctx, tzBuff);

        if (matches) {
            ctx.setTimezone(tzBuff.toString());
        }
    }

    static class OneOrMore extends GroupNode {
        OneOrMore(String name, Node[] nodes) {
            super(name, nodes, isRelative, DateTimeParserContext.empty());
        }

        static OneOrMore of(Node... nodes) {
            return new OneOrMore("", nodes);
        }

        @Override
        boolean matches(DateTimeParserContext ctx) {
            return matchesInternal(ctx, null);
        }

        private boolean matchesInternal(DateTimeParserContext ctx, StringBuilder buff) {
            Node[] nodes = nodes();
            int mark = ctx.cursor().value();

            int i = 0;
            while (ctx.hasMoreTokens()) {
                Node node = nodes[i % nodes.length];
                boolean matches = node.matches(ctx);

                if (!matches) {
                    ctx.cursor().setValue(mark);
                    return isFullMatch(i);
                }
                i++;
            }

            boolean fullMatch = isFullMatch(i);

            return fullMatch;
        }

        private boolean isFullMatch(int i) {
            return i != 0 && (i % nodes().length) == 0;
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            Node[] nodes = nodes();
            int i = 0;
            while (ctx.hasMoreTokens()) {
                Node node = nodes[i % nodes.length];
                node.apply(ctx);
                i++;
            }
        }
    }
}
