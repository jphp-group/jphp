package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_OF_YEAR;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_dd;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_hh;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_ii;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_mm;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.SECOND_ss;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.WEEK;
import static org.develnext.jphp.zend.ext.standard.date.Token.EOF;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Pattern;

public class DateTimeParser {
    private final static EnumMap<Symbol, List<GroupNode>> parseTree = new EnumMap<>(Symbol.class);

    private static final Year4 YEAR_4_DIGIT = Year4.of();
    private static final SymbolNode MINUS_NODE = SymbolNode.of(Symbol.MINUS);
    private static final CharacterNode ISO_WEEK_NODE = CharacterNode.of('W');

    private static final SymbolNode SPACE_NODE = SymbolNode.of(Symbol.SPACE);
    private static final SymbolNode COLON_NODE = SymbolNode.of(Symbol.COLON);
    private static final GroupNode EXIF = GroupNode.of(
            "EXIF",
            Month2.of(),
            COLON_NODE,
            Day2.of(),
            SymbolNode.of(Symbol.SPACE),
            Hour24.of(),
            COLON_NODE,
            Minute2.of(),
            COLON_NODE,
            Second2.of()
    );

    private static final Pattern POSTGRESQL_DOY = Pattern.compile("[0-9]{4}(00[1-9]|0[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6])");
    private static final PatternNode DOY_NODE = PatternNode.ofDigits(DAY_OF_YEAR, dayOfYearAdjuster());
    private static final GroupNode PostgreSQLYearWithDayOfYear = GroupNode.of(
            "PostgreSQL: Year with day-of-year",
            OrNode.of(
                    AndNode.of(
                            YEAR_4_DIGIT,
                            OrNode.of(
                                    AndNode.of(SymbolNode.of(Symbol.DOT), DOY_NODE),
                                    DOY_NODE
                            )
                    ),
                    PatternNode.ofDigits(POSTGRESQL_DOY)
            )
    );
    private static final GroupNode WDDX = GroupNode.of("WDDX",
            PatternNode.ofDigits(MONTH_mm, monthAdjuster()),
            MINUS_NODE,
            PatternNode.ofDigits(DAY_dd, dayAdjuster()),
            CharacterNode.of('T'),
            PatternNode.ofDigits(HOUR_hh, hourAdjuster()),
            COLON_NODE,
            PatternNode.ofDigits(MINUTE_ii, minuteAdjuster()),
            COLON_NODE,
            PatternNode.ofDigits(SECOND_ss, secondAdjuster())
    );
    private static final GroupNode MYSQL = GroupNode.of(
            "MYSQL", Month2.of(), MINUS_NODE, Day2.of(), SPACE_NODE, Hour24.of(), COLON_NODE, Minute2.of(), COLON_NODE, Second2.of()
    );
    private static final SymbolNode DOT_NODE = SymbolNode.of(Symbol.DOT);
    private static final GroupNode XMLRPC_FULL = GroupNode.of("XMLRPC Full", CharacterNode.of('T'), OrNode.of(Hour12.of(), Hour24.of()), COLON_NODE, Minute2.of(), COLON_NODE, Second2.of());
    private static final GroupNode XMLRPC_COMPACT = GroupNode.of("XMLRPC Compact", CharacterNode.ofCaseInsensitive('t'), HourMinuteSecond.of(5, 6));
    private static final GroupNode XMLRPC = GroupNode.of("XMLRPC", YearMonthDay.of(), OrNode.of(XMLRPC_FULL, XMLRPC_COMPACT));
    private static final GroupNode UNIX_TIMESTAMP = GroupNode.of("Unix Timestamp", SymbolNode.of(Symbol.AT), SymbolNode.of(Symbol.DIGITS));

    private static final GroupNode COMPOUND_ROOT = GroupNode.of(
            "Compound Root",
            YEAR_4_DIGIT,
            OrNode.of(
                    OrNode.of(
                            GroupNode.of("EXIF", COLON_NODE, EXIF),
                            GroupNode.of("WDDX OR MYSQL", MINUS_NODE, OrNode.of(WDDX, MYSQL))
                    ),
                    GroupNode.of("PostgreSQL: Year with day-of-year", DOT_NODE, DOY_NODE)
            )
    );

    private static final GroupNode ISOYearWeek = GroupNode.of(
            "ISO year with ISO week",
            YEAR_4_DIGIT,
            OrNode.of(
                    ISO_WEEK_NODE,
                    AndNode.of(MINUS_NODE, ISO_WEEK_NODE)
            ),
            PatternNode.ofDigits(WEEK)
    );

    static {
        parseTree.put(Symbol.AT, Arrays.asList(UNIX_TIMESTAMP));

        parseTree.put(Symbol.DIGITS, Arrays.asList(
                // Localized Compound formats
                COMPOUND_ROOT, XMLRPC
        ));
    }

    private final DateTimeTokenizer tokenizer;

    DateTimeParser(String dateTime) {
        this.tokenizer = new DateTimeTokenizer(dateTime);
    }

    private static BiFunction<Integer, ZonedDateTime, ZonedDateTime> dayOfYearAdjuster() {
        return (doy, dt) -> dt.withDayOfYear(doy);
    }

    private static BiFunction<Integer, ZonedDateTime, ZonedDateTime> secondAdjuster() {
        return (s, z) -> z.withSecond(s);
    }

    private static BiFunction<Integer, ZonedDateTime, ZonedDateTime> minuteAdjuster() {
        return (m, zoned) -> zoned.withMinute(m);
    }

    private static BiFunction<Integer, ZonedDateTime, ZonedDateTime> hourAdjuster() {
        return (h, zoned) -> zoned.withHour(h);
    }

    private static BiFunction<Integer, ZonedDateTime, ZonedDateTime> dayAdjuster() {
        return (d, zonedDateTime) -> zonedDateTime.withDayOfMonth(d);
    }

    private static BiFunction<Integer, ZonedDateTime, ZonedDateTime> monthAdjuster() {
        return (m, zonedDateTime) -> zonedDateTime.withMonth(m);
    }

    public static List<Token> tokenize(final String time) {
        List<Token> tokens = new ArrayList<>();
        DateTimeTokenizer tokenizer = new DateTimeTokenizer(time);

        Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);

        return tokens;
    }

    public ZonedDateTime parse() {
        DateTimeParserContext context = new DateTimeParserContext(getTokens(), new Cursor(), tokenizer);
        Symbol symbol = context.tokenAtCursor().symbol();

        for (GroupNode nodes : parseTree.get(symbol)) {
            boolean matches = nodes.matches(context);

            if (matches) {
                context.cursor().setValue(0);
                nodes.apply(context);
                break;
            }
        }

        return context.dateTime();
    }

    private LinkedList<Token> getTokens() {
        LinkedList<Token> tokens = new LinkedList<>();

        Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);
        return tokens;
    }

}
