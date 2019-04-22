package org.develnext.jphp.zend.ext.standard.date;

import static java.time.temporal.TemporalAdjusters.dayOfWeekInMonth;
import static org.develnext.jphp.zend.ext.standard.date.Adjusters.nextOrSameDayOfWeek;
import static org.develnext.jphp.zend.ext.standard.date.Adjusters.relativeDayOfWeek;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.cursorIncrementer;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.dayAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.dayOfWeekAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.dayOfYearAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.empty;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.isoWeekAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.minuteAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.monthAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.secondAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.yearAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_DD;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_NAME;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_OF_YEAR;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_SUFFIX;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_dd;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HH_MM;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HH_MM_SS;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_ii;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_M;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_ROMAN;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_mm;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.ORDINAL;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.RELTEXT;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.SECOND_ss;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.UNIT;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.WEEK;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.WEEK_WEEK_DAY;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YEAR_y;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YEAR_yy;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YY_MM_DD;
import static org.develnext.jphp.zend.ext.standard.date.Token.EOF;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import php.runtime.common.Pair;

public class DateTimeParser {
    static final SymbolNode AT_NODE = SymbolNode.of(Symbol.AT);
    static final SymbolNode DOT_NODE = SymbolNode.of(Symbol.DOT);
    static final SymbolNode SPACE_NODE = SymbolNode.of(Symbol.SPACE);
    static final SymbolNode COLON_NODE = SymbolNode.of(Symbol.COLON);
    static final SymbolNode MINUS_NODE = SymbolNode.of(Symbol.MINUS);
    static final SymbolNode PLUS_NODE = SymbolNode.of(Symbol.PLUS);
    static final SymbolNode DIGITS_NODE = SymbolNode.of(Symbol.DIGITS);
    static final Node COMMA_NODE = SymbolNode.of(Symbol.COMMA);
    static final CharacterNode SLASH_NODE = CharacterNode.of('/');
    static final CharacterNode UNDERSCORE_NODE = CharacterNode.of('_');
    static final CharacterNode TAB_NODE = CharacterNode.of('\t', Symbol.SPACE);
    private static final CharacterNode T_CI = CharacterNode.ofCaseInsensitive('t');
    private static final CharacterNode ISO_WEEK = CharacterNode.of('W');
    private final static EnumMap<Symbol, List<GroupNode>> parseTree = new EnumMap<>(Symbol.class);
    private static final Year4 YEAR_4_DIGIT = Year4.of();
    private static final Hour24 HOUR_24_NODE = Hour24.of();
    private static final Month2 MONTH_2_DIGIT = Month2.of();
    private static final Day2 DAY_2_DIGIT = Day2.of();
    private static final Second2 SECOND_2_DIGIT = Second2.of();
    private static final Minute2 MINUTE_2_DIGIT = Minute2.of();
    private static final Microseconds MICROSECONDS = Microseconds.of();
    private static final TimezoneCorrectionNode TZ_CORRECTION = new TimezoneCorrectionNode();
    private static final PatternNode DAY_SUFFIX_NODE = PatternNode.of(DAY_SUFFIX, Symbol.STRING, empty());
    private static final Hour12 HOUR_12_NODE = Hour12.of();
    private static final Node MONTH_mm_NODE = PatternNode.ofDigits(MONTH_mm, monthAdjuster());
    private static final Node MONTH_M_NODE = PatternNode.of(MONTH_M, Symbol.STRING, monthStringAdjuster());
    private static final Node MONTH_m_NODE = MONTH_M_NODE.or(PatternNode.of(MONTH_ROMAN, Symbol.STRING))
            .or(PatternNode.of(MONTH, Symbol.STRING))
            .with(monthStringAdjuster().andThen(cursorIncrementer()));
    private static final Node YEAR_y_NODE = PatternNode.ofDigits(YEAR_y, yearAdjuster());
    private static final Node YEAR_yy_NODE = PatternNode.ofDigits(YEAR_yy, yearAdjuster());
    private static final Node DAY_dd_NODE = PatternNode.ofDigits(DAY_dd, dayAdjuster());
    private static final Node DAY_DD_NODE = PatternNode.ofDigits(DAY_DD, dayAdjuster());
    private static final Node DAY_dd_OPT_SUFFIX_NODE = DAY_dd_NODE.followedByOptional(DAY_SUFFIX_NODE);

    private static final GroupNode EXIF = GroupNode.of(
            "EXIF",
            YEAR_4_DIGIT.then(COLON_NODE).then(MONTH_2_DIGIT).then(COLON_NODE).then(DAY_2_DIGIT).then(SPACE_NODE)
                    .then(HOUR_24_NODE).then(COLON_NODE).then(MINUTE_2_DIGIT).then(COLON_NODE).then(SECOND_2_DIGIT)
    );

    private static final Pattern POSTGRESQL_DOY = Pattern.compile("[0-9]{4}(00[1-9]|0[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6])");
    private static final Pattern MYSQL_TIMESTAMP = Pattern.compile("[0-9]{4}(0[0-9]|1[0-2])(0[0-9]|[1-2][0-9]|3[01])([01][0-9]|2[0-4])([0-5][0-9]){2}");
    private static final PatternNode DOY_NODE = PatternNode.ofDigits(DAY_OF_YEAR, dayOfYearAdjuster());
    private static final Node TZ = new TimezoneNode();
    private static final GroupNode WDDX = GroupNode.of("WDDX", YEAR_4_DIGIT, MINUS_NODE, MONTH_mm_NODE, MINUS_NODE, DAY_dd_NODE, CharacterNode.of('T'), HOUR_12_NODE, COLON_NODE, PatternNode.ofDigits(MINUTE_ii, minuteAdjuster()), COLON_NODE, PatternNode.ofDigits(SECOND_ss, secondAdjuster()));
    private static final GroupNode MYSQL = GroupNode
            .builder()
            .name("MYSQL")
            .nodes(PatternNode.of(MYSQL_TIMESTAMP, Symbol.DIGITS, mySqlTimestamp()))
            .build();
    private static final GroupNode XMLRPC_FULL = GroupNode.of("XMLRPC Full", CharacterNode.of('T'), OrNode.of(Hour12.of(), Hour24.of()), COLON_NODE, Minute2.of(), COLON_NODE, SECOND_2_DIGIT);
    private static final GroupNode XMLRPC_COMPACT = GroupNode.of("XMLRPC Compact", CharacterNode.ofCaseInsensitive('t'), HourMinuteSecond.of(5, 6));
    private static final GroupNode XMLRPC = GroupNode.of("XMLRPC", YearMonthDay.of(), OrNode.of(XMLRPC_FULL, XMLRPC_COMPACT));
    private static final GroupNode UNIX_TIMESTAMP = GroupNode.of("Unix Timestamp", AT_NODE.then(UnixTimestamp.of()));
    private static final GroupNode POSTGRES_DOY = GroupNode.builder().relative(false)
            .name("PostgreSQL: Year with day-of-year (YY '.'? doy)")
            .nodes(YEAR_4_DIGIT.then(DOT_NODE).then(DOY_NODE).or(PatternNode.ofDigits(POSTGRESQL_DOY, ctx -> {
                int start = ctx.tokenAtCursor().start();
                long year = ctx.tokenizer().readLong(start, 4);
                int doy = ctx.tokenizer().readInt(start + 4, 3);
                ctx.setYear(year).setDayOfYear(doy);
            })))
            .build();
    private static final GroupNode ISOYearWeek = GroupNode.of("ISO year with ISO week",
            YEAR_4_DIGIT.then(ISO_WEEK).then(PatternNode.ofDigits(WEEK, isoWeekAdjuster()))
    );
    private static final Pattern DAY_OF_WEEK = Pattern.compile("[0-7]");
    private static final PatternNode DAY_OF_WEEK_NODE = PatternNode.ofDigits(DAY_OF_WEEK, dayOfWeekAdjuster());
    private static final GroupNode ISOYearWeekAndDay = GroupNode.of(
            "ISO year with ISO week and day",
            YEAR_4_DIGIT.then(ISO_WEEK).then(
                    PatternNode.ofDigits(WEEK, isoWeekAdjuster())
                            .or(PatternNode.ofDigits(WEEK_WEEK_DAY, c -> {
                                int start = c.tokenAtCursor().start();
                                int week = c.tokenizer().readInt(start, 2);
                                int weekDay = c.tokenizer().readInt(start + 2, 1);

                                c.setWeekOfYear(week).setDayOfWeek(weekDay);
                            }))
            )
    );
    private static final PatternNode MONTH_SHORT = PatternNode.of(MONTH_M, Symbol.STRING, monthStringAdjuster());
    private static final GroupNode COMMON_LOG = GroupNode.of("Common Log Format", PatternNode.ofDigits(DAY_dd, dayAdjuster()), SLASH_NODE, MONTH_SHORT, SLASH_NODE, YEAR_4_DIGIT, COLON_NODE, HOUR_24_NODE, COLON_NODE, MINUTE_2_DIGIT, COLON_NODE, SECOND_2_DIGIT, SPACE_NODE, TZ_CORRECTION);
    private static final GroupNode SOAP = GroupNode.of(
            "SOAP (YY \"-\" MM \"-\" DD \"T\" HH \":\" II \":\" SS frac tzcorrection?)",
            YEAR_4_DIGIT, MINUS_NODE, MONTH_2_DIGIT, MINUS_NODE, DAY_2_DIGIT, CharacterNode.of('T'), HOUR_24_NODE, COLON_NODE, MINUTE_2_DIGIT, COLON_NODE, SECOND_2_DIGIT, DOT_NODE, MICROSECONDS.followedByOptional(TZ_CORRECTION)
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
            HOUR_12_NODE, DOT_OR_COLON, MINUTE_2_DIGIT, DOT_OR_COLON, SECOND_2_DIGIT.followedByOptional(SPACE_NODE), MERIDIAN_NODE
    );
    private static final GroupNode MSSQL_TIME = GroupNode.of(
            "MS SQL (Hour, minutes, seconds and fraction with meridian), PHP 5.3 and later only",
            HOUR_12_NODE.then(COLON_NODE).then(MINUTE_2_DIGIT).then(COLON_NODE).then(SECOND_2_DIGIT).then(DOT_OR_COLON)
                    .then(MICROSECONDS).then(MERIDIAN_NODE)
    );
    private static final GroupNode HOUR_MINUTE = GroupNode.builder().relative(false)
            .name("Hour and minutes ('t'? HH [.:] MM)")
            .afterApply(ctx -> {
                if (ctx.isNotModified(ChronoField.SECOND_OF_MINUTE)) {
                    ctx.setSecond(0);
                }
            })
            .nodes(T_CI.optionalFollowedBy(
                    HOUR_24_NODE.then(DOT_OR_COLON).then(MINUTE_2_DIGIT)
                            .or(PatternNode.ofDigits(HH_MM, c -> {
                                if (c.isTimeModified()) {
                                    c.setYear(c.readLongAtCursor());
                                } else {
                                    int start = c.tokenAtCursor().start();
                                    int hour = c.tokenizer().readInt(start, 2);
                                    int minute = c.tokenizer().readInt(start + 2, 2);
                                    c.setHour(hour).setMinute(minute);
                                }
                            }))
            ))
            .build();
    private static final PatternNode HH_MM_SS_NODE = PatternNode.ofDigits(HH_MM_SS, c -> {
        int start = c.tokenAtCursor().start();
        int hour = c.tokenizer().readInt(start, 2);
        int minute = c.tokenizer().readInt(start + 2, 2);
        int second = c.tokenizer().readInt(start + 4, 2);

        c.setHour(hour).setMinute(minute).setSecond(second);
    });
    private static final GroupNode HOUR_MINUTE_SECOND = GroupNode.of(
            "Hour, minutes and seconds ('t'? HH [.:] MM [.:] II)",
            T_CI.optionalFollowedBy(
                    HOUR_24_NODE.then(DOT_OR_COLON).then(MINUTE_2_DIGIT).then(DOT_OR_COLON).then(SECOND_2_DIGIT)
                            .or(HH_MM_SS_NODE)
            )
    );
    private static final GroupNode HOUR_MINUTE_SECOND_TZ = GroupNode.of(
            "Hour, minutes, seconds and timezone ('t'? HH [.:]? MM [.:]? II space? ( tzcorrection | tz ))",
            T_CI.optionalFollowedBy(
                    HOUR_24_NODE.then(DOT_OR_COLON).then(MINUTE_2_DIGIT).then(DOT_OR_COLON).then(SECOND_2_DIGIT)
                            .or(HH_MM_SS_NODE)
            )
                    .followedByOptional(SPACE_NODE)
                    .then(TZ_CORRECTION.or(TZ))
    );
    private static final GroupNode HOUR_MINUTE_SECOND_FRACTION = GroupNode.of(
            "Hour, minutes, seconds and fraction ('t'? HH [.:] MM [.:] II frac)",
            T_CI.optionalFollowedBy(HOUR_24_NODE), DOT_OR_COLON, MINUTE_2_DIGIT, DOT_OR_COLON, SECOND_2_DIGIT, DOT_NODE, MICROSECONDS
    );
    private static final GroupNode AMERICAN_MONTH_DAY_YEAR = GroupNode.of(
            "American month, day and optional year (mm '/' dd ('/'y)?)",
            MONTH_mm_NODE.then(SLASH_NODE).then(DAY_dd_OPT_SUFFIX_NODE).followedByOptional(SLASH_NODE.then(YEAR_y_NODE))
    );
    private static final GroupNode YY_mm_dd_NODE = GroupNode.of(
            "Four digit year, month and day with slashes (YY '/' mm '/' dd)",
            YEAR_4_DIGIT.then(SLASH_NODE).then(MONTH_mm_NODE).then(SLASH_NODE).then(DAY_dd_OPT_SUFFIX_NODE)
    );
    private static final GroupNode GNU_DATE = GroupNode.of(
            "Four digit year and month (YY '-' mm)",
            YEAR_4_DIGIT.then(MINUS_NODE).then(MONTH_mm_NODE)
    );
    private static final GroupNode y_mm_dd_NODE = GroupNode.of(
            "Year, month and day with dashes (y '-' mm '-' dd)",
            YEAR_y_NODE.then(MINUS_NODE).then(MONTH_mm_NODE).then(MINUS_NODE).then(DAY_dd_NODE)
    );
    private static final GroupNode dd_mm_YY_NODE = GroupNode.builder().relative(false)
            .name("Day, month and four digit year, with dots, tabs or dashes (dd [.\\t-] mm [.-] YY)")
            .afterApplyResetTime()
            .nodes(DAY_dd_NODE.then(DOT_NODE.or(TAB_NODE).or(MINUS_NODE)).then(MONTH_mm_NODE).then(DOT_NODE.or(MINUS_NODE)).then(YEAR_4_DIGIT))
            .build();
    private static final GroupNode dd_mm_yy_NODE = GroupNode.builder().relative(false)
            .name("Day, month and two digit year, with dots or tabs (dd [.\\t] mm '.' yy)")
            .afterApplyResetTime()
            .nodes(DAY_dd_NODE.then(DOT_NODE.or(TAB_NODE)).then(MONTH_mm_NODE).then(DOT_NODE).then(YEAR_yy_NODE))
            .build();
    private static final OrNode SPACE_OR_DOT_OR_MINUS = SPACE_NODE.or(DOT_NODE).or(MINUS_NODE);
    private static final GroupNode dd_m_y_NODE = GroupNode.builder().relative(false)
            .name("Day, textual month and year (dd ([ \\t.-])* m ([ \\t.-])* y)")
            .afterApplyResetTime()
            .nodes(DAY_dd_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(MONTH_m_NODE).then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(YEAR_y_NODE))
            .build();
    private static final GroupNode m_YY_NODE = GroupNode.of(
            "Textual month and four digit year (Day reset to 1) (m ([ \\t.-])* YY)",
            ctx -> {
                if (ctx.isNotModified(ChronoField.DAY_OF_MONTH))
                    ctx.setDayOfMonth(1);
                ctx.atStartOfDay();
            },
            MONTH_m_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(YEAR_4_DIGIT)
    );
    private static final GroupNode YY_m_NODE = GroupNode.of(
            "Four digit year and textual month (Day reset to 1) (YY ([ \\t.-])* m)",
            ctx -> {
                if (ctx.isNotModified(ChronoField.DAY_OF_MONTH))
                    ctx.setDayOfMonth(1);
            },
            YEAR_4_DIGIT.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(MONTH_m_NODE)
    );
    private static final GroupNode m_dd_y_NODE = GroupNode.builder().relative(false)
            .afterApply(ctx -> {
                if (!ctx.isTimeModified())
                    ctx.atStartOfDay();

                if (ctx.isSymbolAtCursor(Symbol.COLON)) {
                    ctx.setYear(Year.now().getValue());
                    ctx.cursor().dec();
                }
            })
            .name("Textual month, day and year (m ([ .\\t-])* dd [,.stndrh\\t ]+ y)")
            .nodes(MONTH_m_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(DAY_dd_OPT_SUFFIX_NODE).then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS.or(COMMA_NODE))).then(YEAR_y_NODE))
            .build();
    private static final GroupNode m_dd_NODE = GroupNode.of(
            "Textual month and day (m ([ .\\t-])* dd [,.stndrh\\t ]*)",
            DateTimeParserContext::atStartOfDay,
            MONTH_m_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS))
                    .then(DAY_dd_OPT_SUFFIX_NODE)
                    .then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS.or(COMMA_NODE)))
    );
    private static final GroupNode dd_m_NODE = GroupNode.of(
            "Day and textual month (d ([ .\\t-])* m)",
            DateTimeParserContext::atStartOfDay,
            DAY_dd_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(MONTH_m_NODE)
    );
    private static final GroupNode M_DD_y_NODE = GroupNode.of(
            "Month abbreviation, day and year (M '-' DD '-' y)",
            DateTimeParserContext::atStartOfDay,
            MONTH_M_NODE.then(MINUS_NODE).then(DAY_DD_NODE).then(MINUS_NODE).then(YEAR_y_NODE)
    );
    private static final GroupNode y_M_DD_NODE = GroupNode.of(
            "Year, month abbreviation and day (y '-' M '-' DD)",
            DateTimeParserContext::atStartOfDay,
            YEAR_y_NODE.then(MINUS_NODE).then(MONTH_M_NODE).then(MINUS_NODE).then(DAY_DD_NODE)
    );
    private static final GroupNode ISO8601_8DIGIT_YY_MM_DD = GroupNode.of(
            "Eight digit year, month and day (YY MM DD)",
            DateTimeParserContext::atStartOfDay,
            PatternNode.ofDigits(YY_MM_DD, ctx -> {
                int start = ctx.tokenAtCursor().start();
                int year = ctx.tokenizer().readInt(start, 4);
                int month = ctx.tokenizer().readInt(start + 4, 2);
                int day = ctx.tokenizer().readInt(start + 6, 2);

                ctx.setYear(year).setMonth(month).setDayOfMonth(day);
            })
    );
    private static final GroupNode ISO8601_YY_MM_DD_WITH_SLASHES = GroupNode.of(
            "Four digit year, month and day with slashes (YY '/' MM '/' DD)",
            DateTimeParserContext::atStartOfDay,
            YEAR_4_DIGIT.then(SLASH_NODE).then(MONTH_2_DIGIT).then(SLASH_NODE).then(DAY_2_DIGIT)
    );
    private static final GroupNode ISO8601_yy_MM_DD_WITH_DASHES = GroupNode.of(
            "Two digit year, month and day with dashes (yy '-' MM '-' DD)",
            DateTimeParserContext::atStartOfDay,
            YEAR_yy_NODE.then(MINUS_NODE).then(MONTH_2_DIGIT).then(MINUS_NODE).then(DAY_2_DIGIT)
    );
    private static final GroupNode ISO8601_YY_MM_DD = GroupNode.of(
            "Four digit year with optional sign, month and day ([+-]? YY '-' MM '-' DD)",
            DateTimeParserContext::atStartOfDay,
            PLUS_NODE.or(MINUS_NODE).optionalFollowedBy(YEAR_4_DIGIT.then(MINUS_NODE).then(MONTH_2_DIGIT).then(MINUS_NODE).then(DAY_2_DIGIT))
    );
    private static final GroupNode JUST_YEAR = GroupNode.of("Year (and just the year) (YY)", YEAR_4_DIGIT);
    private static final GroupNode JUST_MONTH_m = GroupNode.builder().relative(false).resetTimeIfNotModified()
            .name("Textual month (and just the month) (m)").nodes(MONTH_m_NODE).build();
    private static final GroupNode TIMEZONE_INFORMATION = GroupNode.of("Time zone information", TZ_CORRECTION.or(TZ));
    private static final Node YESTERDAY = StringNode.ofCaseInsensitive("yesterday", ctx -> ctx.plusDays(-1).atStartOfDay());
    private static final Node MIDNIGHT = StringNode.ofCaseInsensitive("midnight", DateTimeParserContext::atStartOfDayWithMod);
    private static final Node TODAY = StringNode.ofCaseInsensitive("today", DateTimeParserContext::atStartOfDayWithMod);
    private static final Node NOW = StringNode.ofCaseInsensitive("now", ctx -> ctx.plusSeconds(1).plusSeconds(-1));
    private static final Node NOON = StringNode.ofCaseInsensitive("noon", ctx -> ctx.setHour(12).setMinute(0).setSecond(0));
    private static final Node TOMORROW = StringNode.ofCaseInsensitive("tomorrow", ctx -> ctx.plusDays(1).atStartOfDay());
    // Relative formats
    private static final GroupNode YESTERDAY_AND_FRIENDS = GroupNode.of("Yesterday", YESTERDAY.or(MIDNIGHT).or(TODAY).or(NOW).or(NOON).or(TOMORROW));
    private static final Node STRING_OF_NODE = StringNode.ofCaseInsensitive("of");
    private static final GroupNode BACK_OF_HOUR = GroupNode.builder()
            .relative(true)
            .afterApply(ctx -> ctx.setMinute(15).setSecond(0))
            .nodes(StringNode.ofCaseInsensitive("back").then(SPACE_NODE).then(STRING_OF_NODE).then(SPACE_NODE).then(HOUR_WITH_MERIDIAN.or(HOUR_24_NODE).or(HOUR_12_NODE)))
            .build();
    private static final GroupNode FRONT_OF_HOUR = GroupNode.builder()
            .relative(true)
            .afterApply(ctx -> ctx.plusHours(-1).setMinute(45).setSecond(0))
            .nodes(StringNode.ofCaseInsensitive("front").then(SPACE_NODE).then(STRING_OF_NODE).then(SPACE_NODE).then(HOUR_WITH_MERIDIAN.or(HOUR_24_NODE).or(HOUR_12_NODE)))
            .build();
    private static final GroupNode FIRST_DAY_OF = GroupNode.builder().relative(true)
            .afterApply(ctx -> {
                if (!ctx.hasModifications()) ctx.setDayOfMonth(1);
            })
            .nodes(StringNode.ofCaseInsensitive("first").then(SPACE_NODE).then(StringNode.ofCaseInsensitive("day")).then(SPACE_NODE).then(STRING_OF_NODE))
            .build();
    private static final GroupNode LAST_DAY_OF = GroupNode.builder()
            .relative(true)
            .name("Sets the day to the last day of the current month. This phrase is best used together with a month name following it.")
            .priorityNormal()
            .afterApply(ctx -> ctx.withAdjuster(TemporalAdjusters.lastDayOfMonth(), ChronoField.DAY_OF_MONTH))
            .nodes(StringNode.ofCaseInsensitive("last").then(SPACE_NODE).then(StringNode.ofCaseInsensitive("day")).then(SPACE_NODE).then(STRING_OF_NODE))
            .build();
    private static final PatternNode ORDINAL_NODE = PatternNode.of(ORDINAL, Symbol.STRING);
    private static final PatternNode DAY_NAME_NODE = PatternNode.ofString(DAY_NAME, nthWeekDayConsumer());
    private static final PatternNode UNIT_NODE = PatternNode.ofString(UNIT, relTimeTextConsumer());
    private static final GroupNode ORDINAL_N_TH_WEEKDAY = GroupNode.builder()
            .relative(true)
            .name("Calculates the x-th week day of the current month. (last sat of July 2008)")
            .priorityNormal()
            .afterApplyResetTime()
            .nodes(ORDINAL_NODE.then(SPACE_NODE).then(DAY_NAME_NODE).then(SPACE_NODE).then(STRING_OF_NODE))
            .build();
    private static final GroupNode NUMBER_N_TH_WEEKDAY = GroupNode.builder()
            .relative(true)
            .name("Calculates the x-th week day of the current month. (1 sat)")
            .priorityNormal()
            .afterApply(DateTimeParserContext::atStartOfDay)
            .nodes(DIGITS_NODE.then(SPACE_NODE)
                    .then(DAY_NAME_NODE.withConsumer(numberNthWeekDay())))
            .build();
    private static final GroupNode WEEK_DAY_NAME = GroupNode.builder()
            .relative(true)
            .name("Moves to the next day of this name.")
            .afterApply(ctx -> {
                if (!ctx.isTimeModified())
                    ctx.atStartOfDay();
            })
            .nodes(DAY_NAME_NODE.withConsumer(ctx -> ctx.withAdjuster(nextOrSameDayOfWeek(ctx.readStringAtCursor()), ChronoField.DAY_OF_WEEK)))
            .build();
    private static final GroupNode REL_WEEK = GroupNode.builder()
            .relative(true)
            .name("Handles the special format \"weekday + last/this/next week\".")
            .priorityNormal()
            .nodes(PatternNode.ofString(RELTEXT).then(SPACE_NODE).then(StringNode.ofCaseInsensitive("week", relWeek())))
            .build();
    private static final GroupNode REL_TIME_TEXT = GroupNode.builder()
            .relative(true)
            .name("Handles relative time items where the value is text. (\"fifth day\", \"second month\")")
            .priorityHigh()
            .afterApply(ctx -> {
                if (ctx.isModified(ChronoField.DAY_OF_WEEK))
                    ctx.atStartOfDay();
            })
            .nodes(ORDINAL_NODE.then(SPACE_NODE).then(UNIT_NODE.or(DAY_NAME_NODE.withConsumer(relWeekDayConsumer()))))
            .build();
    private static final GroupNode REL_TIME = GroupNode.builder()
            .relative(true)
            .priorityLow()
            .name("Handles relative time items where the value is a number. (\"+5 weeks\", \"12 day\", \"-7 weekdays\")")
            .nodes(new RelativeTimeNumber())
            .build();

    static {
        parseTree.put(Symbol.AT, Collections.singletonList(UNIX_TIMESTAMP));
        parseTree.put(Symbol.PLUS, Arrays.asList(ISO8601_YY_MM_DD, REL_TIME, TIMEZONE_INFORMATION));
        parseTree.put(Symbol.MINUS, Arrays.asList(ISO8601_YY_MM_DD, REL_TIME, TIMEZONE_INFORMATION));
        parseTree.put(Symbol.STRING, Arrays.asList(
                HOUR_MINUTE_SECOND_FRACTION,
                HOUR_MINUTE_SECOND_TZ,
                HOUR_MINUTE_SECOND,
                HOUR_MINUTE,
                YESTERDAY_AND_FRIENDS,
                BACK_OF_HOUR,
                FRONT_OF_HOUR,
                WEEK_DAY_NAME,
                ORDINAL_N_TH_WEEKDAY,
                FIRST_DAY_OF,
                LAST_DAY_OF,
                REL_WEEK,
                REL_TIME_TEXT,
                M_DD_y_NODE,
                m_YY_NODE,
                m_dd_y_NODE,
                m_dd_NODE,
                JUST_MONTH_m,
                TIMEZONE_INFORMATION
        ));

        parseTree.put(Symbol.DIGITS, Arrays.asList(
                // Localized Compound formats
                COMMON_LOG, SOAP, POSTGRES_DOY, EXIF, MYSQL, WDDX, XMLRPC, ISOYearWeekAndDay, ISOYearWeek,

                // Relative formats
                REL_TIME,

                // 24 Hour Formats
                HOUR_MINUTE_SECOND_FRACTION,

                // Date Formats
                ISO8601_YY_MM_DD,
                ISO8601_8DIGIT_YY_MM_DD,
                ISO8601_YY_MM_DD_WITH_SLASHES,
                ISO8601_yy_MM_DD_WITH_DASHES,

                AMERICAN_MONTH_DAY_YEAR,
                dd_mm_YY_NODE,
                dd_mm_yy_NODE,
                dd_m_y_NODE,
                dd_m_NODE,
                y_M_DD_NODE,
                YY_mm_dd_NODE,
                YY_m_NODE,
                y_mm_dd_NODE,
                GNU_DATE,

                // 12 Hour formats
                HOUR_MINUTE_SECOND_WITH_MERIDIAN,
                MSSQL_TIME,
                HOUR_MINUTE_WITH_MERIDIAN,
                HOUR_WITH_MERIDIAN,

                // 24 Hour Formats
                HOUR_MINUTE_SECOND,
                HOUR_MINUTE_SECOND_TZ,
                HOUR_MINUTE,

                NUMBER_N_TH_WEEKDAY,
                // timezone
                TIMEZONE_INFORMATION,

                // Weird formats
                JUST_YEAR
        ));
    }

    /**
     * The input tokenizer.
     */
    private final DateTimeTokenizer tokenizer;
    /**
     * The relative date/time statements for delayed execution.
     */
    private final Queue<CursorAwareNode> relatives;
    /**
     * The base time for relative statements.
     */
    private final ZonedDateTime baseDateTime;

    DateTimeParser(String dateTime) {
        this(dateTime, ZoneId.systemDefault());
    }

    DateTimeParser(String dateTime, ZoneId zoneId) {
        this(dateTime, ZonedDateTime.now().withZoneSameInstant(zoneId));
    }

    DateTimeParser(String dateTime, ZonedDateTime baseDateTime) {
        this.tokenizer = new DateTimeTokenizer(dateTime);
        this.relatives = new PriorityQueue<>(Comparator.reverseOrder()); // process bigger priorities first
        this.baseDateTime = baseDateTime;
    }

    private static Consumer<DateTimeParserContext> mySqlTimestamp() {
        return ctx -> {
            int s = ctx.tokenAtCursor().start();
            DateTimeTokenizer t = ctx.tokenizer();
            ctx.setYear(t.readInt(s, 4))
                    .setMonth(t.readInt(s + 4, 2))
                    .setDayOfMonth(t.readInt(s + 6, 2))
                    .setHour(t.readInt(s + 8, 2))
                    .setMinute(t.readInt(s + 10, 2))
                    .setSecond(t.readInt(s + 12, 2));
        };
    }

    private static Consumer<DateTimeParserContext> numberNthWeekDay() {
        return ctx -> {
            long value = ctx.readLongAt(ctx.cursor().value() - 2);
            DayOfWeek currentDow = Adjusters.dayOfWeek(ctx.readStringAtCursor());
            DayOfWeek dayOfWeek = ctx.dateTime().withDayOfMonth(Math.toIntExact(value)).getDayOfWeek();

            if (currentDow != dayOfWeek) {
                ctx.withAdjuster(dayOfWeekInMonth(Math.toIntExact(value), currentDow), ChronoField.DAY_OF_WEEK);
            } else {
                ctx.setDayOfMonth(value);
            }
        };
    }

    private static Consumer<DateTimeParserContext> relWeek() {
        return ctx -> {
            if (ctx.hasModifications())
                ctx.atStartOfDay();

            ctx.withAdjuster(relativeDayOfWeek(ctx.readStringAt(ctx.cursor().value() - 2), DayOfWeek.MONDAY), ChronoField.DAY_OF_WEEK);
        };
    }

    private static Consumer<DateTimeParserContext> nthWeekDayConsumer() {
        return ctx -> {
            int snapshot = ctx.cursor().value();
            String dayOfWeek = ctx.readStringAtCursor();
            String ordinal = ctx.withCursorValue(snapshot - 2).readStringAtCursor();
            ctx.withAdjuster(Adjusters.dayOfWeekInMonth(ordinal, dayOfWeek), ChronoField.DAY_OF_WEEK);
        };
    }

    private static Consumer<DateTimeParserContext> relWeekDayConsumer() {
        return ctx -> {
            int snapshot = ctx.cursor().value();
            String dayOfWeek = ctx.readStringAtCursor();
            String ordinal = ctx.withCursorValue(snapshot - 2).readStringAtCursor().toLowerCase();
            final TemporalAdjuster adjuster;

            switch (ordinal) {
                case "this":
                    adjuster = nextOrSameDayOfWeek(dayOfWeek);
                    break;
                case "previous":
                case "last":
                    adjuster = Adjusters.previousDayOfWeek(dayOfWeek);
                    break;
                case "next":
                default:
                    adjuster = Adjusters.nthDayOfWeek(dayOfWeek, ordinal);
                    break;
            }

            ctx.withAdjuster(adjuster, ChronoField.DAY_OF_WEEK);
        };
    }

    private static Consumer<DateTimeParserContext> relTimeTextConsumer() {
        return ctx -> {
            int snapshot = ctx.cursor().value();
            Pair<TemporalAdjuster, TemporalField> pair = Adjusters.relativeUnit(ctx.readStringAtCursor(),
                    ctx.withCursorValue(snapshot - 2).readStringAtCursor());
            ctx.withAdjuster(pair.getA(), pair.getB());
        };
    }

    private static Consumer<DateTimeParserContext> monthStringAdjuster() {
        return ctx -> ctx.withAdjuster(Adjusters.month(ctx.readStringAtCursor()), ChronoField.MONTH_OF_YEAR);
    }

    public ZonedDateTime parse() {
        List<Token> tokens = getTokens();
        DateTimeParserContext ctx = new DateTimeParserContext(tokens, new Cursor(), tokenizer, baseDateTime);

        parseNormal(ctx);

        // saving cursor value before apply relative statements
        int snapshot = ctx.cursor().value();

        // the normal process is over its time to apply all relative nodes
        while (relatives.peek() != null) {
            CursorAwareNode relativeNode = relatives.poll();
            relativeNode.apply(ctx);
        }

        if (!ctx.hasModifications()) {
            throw new DateTimeException("DateTimeParserContext should have modifications!");
        }

        // restoring cursor
        if (ctx.withCursorValue(snapshot).hasMoreTokens()) {
            throw new DateTimeException("Unparsed tokens are present!");
        }

        return ctx.dateTime();
    }

    private void parseNormal(DateTimeParserContext ctx) {
        int lastMatchIdx = 0;
        boolean matches = true;

        while (ctx.hasMoreTokens() && matches) {
            if (ctx.isSymbolAtCursor(Symbol.SPACE) || ctx.isSymbolAtCursor(Symbol.COMMA)) {
                ctx.cursor().inc();
                lastMatchIdx++;
                continue;
            }

            Symbol symbol = ctx.symbolAtCursor();
            List<GroupNode> groupNodes = parseTree.get(symbol);

            if (groupNodes == null) {
                throw new DateTimeException("Cannot find parser tree for symbol: " + symbol);
            }

            for (GroupNode nodes : groupNodes) {
                matches = nodes.matches(ctx);
                if (matches) {
                    // we should delay processing the relative groups
                    if (nodes.isRelative()) {
                        relatives.offer(new CursorAwareNode(lastMatchIdx, nodes));
                    } else {
                        ctx.cursor().setValue(lastMatchIdx);
                        nodes.apply(ctx);
                    }

                    lastMatchIdx = ctx.cursor().value();
                    break;
                }
            }
        }
    }

    private List<Token> getTokens() {
        List<Token> tokens = new ArrayList<>();

        Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);
        return tokens;
    }
}
