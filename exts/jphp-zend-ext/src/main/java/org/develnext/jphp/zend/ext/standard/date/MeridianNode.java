package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.DOT_NODE;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.charAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.charBufferAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.charLowerAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.cursorIncrementer;

import java.util.regex.Pattern;

class MeridianNode extends Node {
    private static final OrNode A_OR_P = CharacterNode.ofCaseInsensitive('a').or(CharacterNode.ofCaseInsensitive('p'));
    private static final Pattern AM_PM = Pattern.compile("[ap]m", Pattern.CASE_INSENSITIVE);

    static MeridianNode of() {
        return new MeridianNode();
    }

    @Override
    boolean matches(DateTimeParserContext ctx) {
        return matchesInternal(ctx, null);
    }

    private boolean matchesInternal(DateTimeParserContext ctx, StringBuilder buff) {
        GroupNode[] nodes = new GroupNode[]{
                GroupNode.of(A_OR_P.with(charAppender(buff).andThen(cursorIncrementer())), DOT_NODE, CharacterNode.ofCaseInsensitive('m', charLowerAppender(buff)), DOT_NODE),
                GroupNode.of(A_OR_P.with(charAppender(buff).andThen(cursorIncrementer())), DOT_NODE, CharacterNode.ofCaseInsensitive('m', charLowerAppender(buff))),
                GroupNode.of(PatternNode.of(AM_PM, Symbol.STRING, charBufferAppender(buff)), DOT_NODE),
                GroupNode.of(PatternNode.of(AM_PM, Symbol.STRING, charBufferAppender(buff))),
        };

        for (GroupNode groupNode : nodes) {
            int snapshot = ctx.cursor().value();
            boolean matches = groupNode.matches(ctx);

            if (matches) {
                if (buff != null) {
                    ctx = ctx.withCursorValue(snapshot);
                    groupNode.apply(ctx);
                }

                return true;
            }
        }

        return false;
    }

    @Override
    void apply(DateTimeParserContext ctx) {
        StringBuilder buff = new StringBuilder();
        boolean matches = matchesInternal(ctx, buff);

        if (matches) {
            String s = buff.toString().toLowerCase();
            ctx.setMeridian("am".equals(s));
        }
    }
}
