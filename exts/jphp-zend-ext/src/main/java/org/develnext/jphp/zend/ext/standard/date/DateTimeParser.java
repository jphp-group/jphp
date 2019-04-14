package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_OF_YEAR;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_dd;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HH_MM;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_ii;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_M;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_mm;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.SECOND_ss;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.WEEK;
import static org.develnext.jphp.zend.ext.standard.date.Token.EOF;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class DateTimeParser {
    static final SymbolNode AT_NODE = SymbolNode.of(Symbol.AT);
    static final SymbolNode DOT_NODE = SymbolNode.of(Symbol.DOT);
    static final SymbolNode SPACE_NODE = SymbolNode.of(Symbol.SPACE);
    static final SymbolNode COLON_NODE = SymbolNode.of(Symbol.COLON);
    static final SymbolNode SLASH_NODE = SymbolNode.of(Symbol.SLASH);
    static final SymbolNode MINUS_NODE = SymbolNode.of(Symbol.MINUS);
    static final SymbolNode PLUS_NODE = SymbolNode.of(Symbol.PLUS);
    private final static EnumMap<Symbol, List<GroupNode>> parseTree = new EnumMap<>(Symbol.class);
    private static final Year4 YEAR_4_DIGIT = Year4.of();
    private static final Hour24 HOUR_24_NODE = Hour24.of();
    private static final Month2 MONTH_2_DIGIT = Month2.of();
    private static final Day2 DAY_2_DIGIT = Day2.of();
    private static final Second2 SECOND_2_DIGIT = Second2.of();
    private static final Minute2 MINUTE_2_DIGIT = Minute2.of();
    private static final CharacterNode ISO_WEEK = CharacterNode.of('W');
    private static final Microseconds MICROSECONDS = Microseconds.of();
    private static final TimezoneCorrectionNode TZ_CORRECTION = new TimezoneCorrectionNode();

    private static final GroupNode EXIF = GroupNode.of(
            "EXIF",
            YEAR_4_DIGIT,
            COLON_NODE,
            MONTH_2_DIGIT,
            COLON_NODE,
            DAY_2_DIGIT,
            SPACE_NODE,
            HOUR_24_NODE,
            COLON_NODE,
            MINUTE_2_DIGIT,
            COLON_NODE,
            SECOND_2_DIGIT
    );

    private static final Pattern POSTGRESQL_DOY = Pattern.compile("[0-9]{4}(00[1-9]|0[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6])");
    private static final Pattern TZ_PATTERN = Pattern.compile("\\(?[A-Za-z]{1,6}\\)?|[A-Z][a-z]+([_/][A-Z][a-z]+)+");
    private static final PatternNode DOY_NODE = PatternNode.ofDigits(DAY_OF_YEAR, dayOfYearAdjuster());
    private static final PatternNode TZ = PatternNode.of(TZ_PATTERN, Symbol.STRING, timezoneSetter());
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
    private static final Hour12 HOUR_12_NODE = Hour12.of();
    private static final GroupNode WDDX = GroupNode.of("WDDX",
            YEAR_4_DIGIT,
            MINUS_NODE,
            PatternNode.ofDigits(MONTH_mm, monthAdjuster()),
            MINUS_NODE,
            PatternNode.ofDigits(DAY_dd, dayAdjuster()),
            CharacterNode.of('T'),
            HOUR_12_NODE,
            COLON_NODE,
            PatternNode.ofDigits(MINUTE_ii, minuteAdjuster()),
            COLON_NODE,
            PatternNode.ofDigits(SECOND_ss, secondAdjuster())
    );
    private static final GroupNode MYSQL = GroupNode.of("MYSQL", YEAR_4_DIGIT, MINUS_NODE, Month2.of(), MINUS_NODE, Day2.of(), SPACE_NODE, Hour24.of(), COLON_NODE, Minute2.of(), COLON_NODE, SECOND_2_DIGIT);
    private static final GroupNode XMLRPC_FULL = GroupNode.of("XMLRPC Full", CharacterNode.of('T'), OrNode.of(Hour12.of(), Hour24.of()), COLON_NODE, Minute2.of(), COLON_NODE, SECOND_2_DIGIT);
    private static final GroupNode XMLRPC_COMPACT = GroupNode.of("XMLRPC Compact", CharacterNode.ofCaseInsensitive('t'), HourMinuteSecond.of(5, 6));
    private static final GroupNode XMLRPC = GroupNode.of("XMLRPC", YearMonthDay.of(), OrNode.of(XMLRPC_FULL, XMLRPC_COMPACT));
    private static final GroupNode UNIX_TIMESTAMP = GroupNode.of("Unix Timestamp", AT_NODE, UnixTimestamp.of());
    private static final GroupNode POSTGRES_DOY = GroupNode.of("POSTGRES DOY", YEAR_4_DIGIT, DOT_NODE, DOY_NODE);
    private static final GroupNode ISOYearWeek = GroupNode.of(
            "ISO year with ISO week",
            YEAR_4_DIGIT,
            OrNode.of(
                    ISO_WEEK,
                    AndNode.of(MINUS_NODE, ISO_WEEK)
            ),
            PatternNode.ofDigits(WEEK, isoWeekAdjuster())
    );
    private static final Pattern DAY_OF_WEEK = Pattern.compile("[0-7]");
    private static final PatternNode DAY_OF_WEEK_NODE = PatternNode.ofDigits(DAY_OF_WEEK, dayOfWeekAdjuster());
    private static final GroupNode ISOYearWeekAndDay = GroupNode.of(
            "ISO year with ISO week and day",
            YEAR_4_DIGIT,
            OrNode.of(
                    ISO_WEEK,
                    AndNode.of(MINUS_NODE, ISO_WEEK)
            ),
            PatternNode.ofDigits(WEEK, isoWeekAdjuster()),
            OrNode.of(
                    AndNode.of(MINUS_NODE, DAY_OF_WEEK_NODE),
                    DAY_OF_WEEK_NODE
            )
    );
    private static final PatternNode MONTH_SHORT = PatternNode.of(MONTH_M, Symbol.STRING, monthStringAdjuster());
    private static final GroupNode COMMON_LOG = GroupNode.of("Common Log Format", PatternNode.ofDigits(DAY_dd, dayAdjuster()), SLASH_NODE, MONTH_SHORT, SLASH_NODE, YEAR_4_DIGIT, COLON_NODE, HOUR_24_NODE, COLON_NODE, MINUTE_2_DIGIT, COLON_NODE, SECOND_2_DIGIT, SPACE_NODE, TZ_CORRECTION);
    private static final GroupNode SOAP = GroupNode.of( // YY "-" MM "-" DD "T" HH ":" II ":" SS frac tzcorrection?
            "SOAP",
            YEAR_4_DIGIT,
            MINUS_NODE,
            MONTH_2_DIGIT,
            MINUS_NODE,
            DAY_2_DIGIT,
            CharacterNode.of('T'),
            HOUR_24_NODE,
            COLON_NODE,
            MINUTE_2_DIGIT,
            COLON_NODE,
            SECOND_2_DIGIT,
            DOT_NODE,
            MICROSECONDS.followedByOptional(TZ_CORRECTION)
    );
    private static final MeridianNode MERIDIAN_NODE = MeridianNode.of();
    private static final GroupNode HOUR_WITH_MERIDIAN = GroupNode.of(
            "Hour only, with meridian",
            HOUR_12_NODE.followedByOptional(SPACE_NODE),
            MERIDIAN_NODE
    );
    private static final OrNode DOT_OR_COLON = OrNode.of(DOT_NODE, COLON_NODE);
    private static final GroupNode HOUR_MINUTE_WITH_MERIDIAN = GroupNode.of(
            "Hour and minutes, with meridian",
            HOUR_12_NODE,
            DOT_OR_COLON,
            MINUTE_2_DIGIT.followedByOptional(SPACE_NODE),
            MERIDIAN_NODE
    );
    private static final GroupNode HOUR_MINUTE_SECOND_WITH_MERIDIAN = GroupNode.of(
            "Hour, minutes and seconds, with meridian",
            HOUR_12_NODE,
            DOT_OR_COLON,
            MINUTE_2_DIGIT,
            DOT_OR_COLON,
            SECOND_2_DIGIT.followedByOptional(SPACE_NODE),
            MERIDIAN_NODE
    );
    private static final GroupNode MSSQL_TIME = GroupNode.of(
            "MS SQL (Hour, minutes, seconds and fraction with meridian), PHP 5.3 and later only",
            HOUR_12_NODE,
            COLON_NODE,
            MINUTE_2_DIGIT,
            COLON_NODE,
            SECOND_2_DIGIT,
            DOT_OR_COLON,
            MICROSECONDS,
            MERIDIAN_NODE
    );
    private static final CharacterNode T_CI = CharacterNode.ofCaseInsensitive('t');
    private static final GroupNode HOUR_MINUTE = GroupNode.of(
            "Hour and minutes ('t'? HH [.:] MM)",
            ctx -> {
                if (ctx.isNotModified(ChronoField.SECOND_OF_MINUTE)) {
                    ctx.setSecond(0);
                }
            },
            T_CI.optionalFollowedBy(
                    HOUR_24_NODE.then(DOT_OR_COLON).then(MINUTE_2_DIGIT)
                            .or(PatternNode.ofDigits(HH_MM, c -> {
                                int start = c.tokenAtCursor().start();
                                int hour = c.tokenizer().readInt(start, 2);
                                int minute = c.tokenizer().readInt(start + 2, 2);

                                c.setHour(hour).setMinute(minute);
                            }))
            )

    );
    private static final GroupNode HOUR_MINUTE_SECOND = GroupNode.of(
            "Hour, minutes and seconds ('t'? HH [.:] MM [.:] II)",
            T_CI.optionalFollowedBy(HOUR_24_NODE),
            DOT_OR_COLON,
            MINUTE_2_DIGIT,
            DOT_OR_COLON,
            SECOND_2_DIGIT
    );

    private static final GroupNode HOUR_MINUTE_SECOND_TZ = GroupNode.of(
            "Hour, minutes, seconds and timezone ('t'? HH [.:] MM [.:] II space? ( tzcorrection | tz ))",
            T_CI.optionalFollowedBy(HOUR_24_NODE),
            DOT_OR_COLON,
            MINUTE_2_DIGIT,
            DOT_OR_COLON,
            SECOND_2_DIGIT.followedByOptional(SPACE_NODE),
            TZ_CORRECTION.or(TZ)
    );

    private static final GroupNode HOUR_MINUTE_SECOND_FRACTION = GroupNode.of(
            "Hour, minutes, seconds and fraction ('t'? HH [.:] MM [.:] II frac)",
            T_CI.optionalFollowedBy(HOUR_24_NODE),
            DOT_OR_COLON,
            MINUTE_2_DIGIT,
            DOT_OR_COLON,
            SECOND_2_DIGIT,
            DOT_NODE,
            MICROSECONDS
    );

    private static final GroupNode TIMEZONE_INFORMATION = GroupNode.of("Time zone information", TZ_CORRECTION.or(TZ));

    static {
        parseTree.put(Symbol.AT, Arrays.asList(UNIX_TIMESTAMP));
        parseTree.put(Symbol.STRING, Arrays.asList(TIMEZONE_INFORMATION));
        parseTree.put(Symbol.CHARACTER, Arrays.asList(
                HOUR_MINUTE_SECOND_FRACTION,
                HOUR_MINUTE_SECOND_TZ,
                HOUR_MINUTE_SECOND,
                HOUR_MINUTE
        ));

        parseTree.put(Symbol.DIGITS, Arrays.asList(
                // Localized Compound formats
                COMMON_LOG, SOAP, POSTGRES_DOY, EXIF, MYSQL, WDDX, XMLRPC, ISOYearWeekAndDay, ISOYearWeek,

                // 12 Hour formats
                HOUR_MINUTE_SECOND_WITH_MERIDIAN,
                MSSQL_TIME,
                HOUR_MINUTE_WITH_MERIDIAN,
                HOUR_WITH_MERIDIAN,

                // 24 Hour Formats
                HOUR_MINUTE_SECOND_FRACTION,
                HOUR_MINUTE_SECOND_TZ,
                HOUR_MINUTE_SECOND,
                HOUR_MINUTE
        ));
    }

    private final DateTimeTokenizer tokenizer;

    DateTimeParser(String dateTime) {
        this.tokenizer = new DateTimeTokenizer(dateTime);
    }

    private static Consumer<DateTimeParserContext> timezoneSetter() {
        return ctx -> {
            String s = ctx.readStringAtCursor();
            ZoneId of = ZoneId.of(s);
            ctx.setTimezone(of);
        };
    }

    private static Consumer<DateTimeParserContext> monthStringAdjuster() {
        return ctx -> {
            int month = monthNameToNumber(ctx.readStringAtCursor().toLowerCase());
            ctx.setMonth(month);
        };
    }

    private static Consumer<DateTimeParserContext> dayOfWeekAdjuster() {
        return ctx -> ctx.setDayOfWeek(ctx.readIntAtCursor());
    }

    private static Consumer<DateTimeParserContext> isoWeekAdjuster() {
        return ctx -> ctx.setWeekOfYear(ctx.readIntAtCursor());
    }

    private static Consumer<DateTimeParserContext> dayOfYearAdjuster() {
        return ctx -> ctx.setDayOfYear(ctx.readIntAtCursor());
    }

    private static Consumer<DateTimeParserContext> secondAdjuster() {
        return ctx -> ctx.setSecond(ctx.readIntAtCursor());
    }

    private static Consumer<DateTimeParserContext> minuteAdjuster() {
        return ctx -> ctx.setMinute(ctx.readIntAtCursor());
    }

    private static Consumer<DateTimeParserContext> hourAdjuster() {
        return ctx -> ctx.setHour(ctx.readIntAtCursor());
    }

    private static Consumer<DateTimeParserContext> dayAdjuster() {
        return ctx -> ctx.setDayOfMonth(ctx.readIntAtCursor());
    }

    private static Consumer<DateTimeParserContext> monthAdjuster() {
        return ctx -> ctx.setMonth(ctx.readIntAtCursor());
    }

    public static List<Token> tokenize(final String time) {
        List<Token> tokens = new ArrayList<>();
        DateTimeTokenizer tokenizer = new DateTimeTokenizer(time);

        Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);

        return tokens;
    }

    private static int monthNameToNumber(String month) {
        switch (month) {
            case "january":
            case "jan":
                return 1;
            case "febuary":
            case "feb":
                return 2;
            case "march":
            case "mar":
                return 3;
            case "april":
            case "apr":
                return 4;
            case "may":
                return 5;
            case "june":
            case "jun":
                return 6;
            case "july":
            case "jul":
                return 7;
            case "august":
            case "aug":
                return 8;
            case "september":
            case "sep":
                return 9;
            case "october":
            case "oct":
                return 10;
            case "november":
            case "nov":
                return 11;
            case "december":
            case "dec":
                return 12;
            default:
                throw new IllegalArgumentException("Not a month: " + month);
        }
    }

    public ZonedDateTime parse() {
        DateTimeParserContext context = new DateTimeParserContext(getTokens(), new Cursor(), tokenizer);
        Symbol symbol = context.symbolAtCursor();

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
