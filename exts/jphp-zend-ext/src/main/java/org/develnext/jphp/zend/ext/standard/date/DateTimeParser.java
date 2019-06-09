package org.develnext.jphp.zend.ext.standard.date;

import static java.time.temporal.TemporalAdjusters.dayOfWeekInMonth;
import static org.develnext.jphp.zend.ext.standard.date.Adjusters.nextOrSameDayOfWeek;
import static org.develnext.jphp.zend.ext.standard.date.Adjusters.relativeDayOfWeek;
import static org.develnext.jphp.zend.ext.standard.date.Adjusters.relativeUnit;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.charAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.charBufferAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.charLowerAppender;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.cursorIncrementer;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.dayAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.empty;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.isoWeekAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.monthAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.simpleYearAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParserContext.Consumers.yearAdjuster;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_DD;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_NAME;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_OF_YEAR;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_SUFFIX;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_dd;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HH_MM;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HH_MM_SS;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_12;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.HOUR_24;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_II;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_ii;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_FULL;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_M;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_MM;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_ROMAN;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_mm;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.ORDINAL;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.RELTEXT;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.SECOND_SS;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.SECOND_ss;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.UNIT;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.WEEK;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.WEEK_WEEK_DAY;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YEAR_y;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YEAR_yy;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YY_MM_DD;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.Token.EOF;

import java.nio.CharBuffer;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import php.runtime.common.Pair;

public class DateTimeParser {
    private static final SymbolNode AT_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.AT);
    private static final SymbolNode DOT_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.DOT);
    private static final SymbolNode SPACE_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.SPACE);
    private static final SymbolNode COLON_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.COLON);
    private static final SymbolNode MINUS_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.MINUS);
    private static final SymbolNode PLUS_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.PLUS);
    private static final SymbolNode DIGITS_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.DIGITS);
    private static final Node COMMA_NODE = SymbolNode.of(DateTimeTokenizer.Symbol.COMMA);
    private static final CharacterNode SLASH_NODE = CharacterNode.of('/');
    private static final CharacterNode UNDERSCORE_NODE = CharacterNode.of('_');
    private static final CharacterNode TAB_NODE = CharacterNode.of('\t', DateTimeTokenizer.Symbol.SPACE);
    private static final CharacterNode T_CI = CharacterNode.ofCaseInsensitive('t');
    private static final CharacterNode ISO_WEEK = CharacterNode.of('W');
    private final static EnumMap<DateTimeTokenizer.Symbol, List<GroupNode>> parseTree = new EnumMap<>(DateTimeTokenizer.Symbol.class);
    private static final Year4 YEAR_4_DIGIT = Year4.of();
    private static final Hour24 HOUR_24_NODE = Hour24.of();
    private static final Month2 MONTH_2_DIGIT = Month2.of();
    private static final Day2 DAY_2_DIGIT = Day2.of();
    private static final Second2 SECOND_2_DIGIT = Second2.of();
    private static final Minute2 MINUTE_2_DIGIT = Minute2.of();
    private static final Microseconds MICROSECONDS = Microseconds.of();
    private static final TimezoneCorrectionNode TZ_CORRECTION = new TimezoneCorrectionNode();
    private static final PatternNode DAY_SUFFIX_NODE = PatternNode.of(DAY_SUFFIX, DateTimeTokenizer.Symbol.STRING, empty());
    private static final Hour12 HOUR_12_NODE = Hour12.of();
    private static final Node MONTH_mm_NODE = PatternNode.ofDigits(MONTH_mm, monthAdjuster());
    private static final Node MONTH_M_NODE = PatternNode.of(MONTH_M, DateTimeTokenizer.Symbol.STRING, monthStringAdjuster());
    private static final Node MONTH_m_NODE = MONTH_M_NODE.or(PatternNode.of(MONTH_ROMAN, DateTimeTokenizer.Symbol.STRING))
            .or(PatternNode.of(MONTH_FULL, DateTimeTokenizer.Symbol.STRING))
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
    private static final PatternNode DOY_NODE = PatternNode.ofDigits(DAY_OF_YEAR, ctx -> ctx.setDayOfYear(ctx.readIntAtCursor()));
    private static final PatternNode WDDX_hh = PatternNode.ofDigits(TimezoneCorrectionNode.TZ_HOUR_PART, ctx -> ctx.setHour(ctx.readIntAtCursor()));
    private static final Node TZ = new TimezoneNode();
    private static final PatternNode MINUTE_ii_NODE = PatternNode.ofDigits(MINUTE_ii, ctx -> ctx.setMinute(ctx.readIntAtCursor()));
    private static final GroupNode WDDX = GroupNode.builder()
            .name("WDDX")
            .nodes(YEAR_4_DIGIT.then(MINUS_NODE).then(MONTH_mm_NODE).then(MINUS_NODE).then(DAY_dd_NODE).then(CharacterNode.of('T'))
                            .then(WDDX_hh)
                            .then(COLON_NODE)
                            .then(MINUTE_ii_NODE)
                            .then(COLON_NODE),
                    PatternNode.ofDigits(SECOND_ss, ctx -> ctx.setSecond(ctx.readIntAtCursor())).followedByOptional(TZ_CORRECTION)
            )
            .build();
    private static final GroupNode MYSQL = GroupNode
            .builder()
            .name("MYSQL")
            .nodes(PatternNode.of(MYSQL_TIMESTAMP, DateTimeTokenizer.Symbol.DIGITS, mySqlTimestamp()))
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
    private static final PatternNode MONTH_SHORT = PatternNode.of(MONTH_M, DateTimeTokenizer.Symbol.STRING, monthStringAdjuster());
    private static final GroupNode COMMON_LOG = GroupNode.of("Common Log Format", PatternNode.ofDigits(DAY_dd, dayAdjuster()), SLASH_NODE, MONTH_SHORT, SLASH_NODE, YEAR_4_DIGIT, COLON_NODE, HOUR_24_NODE, COLON_NODE, MINUTE_2_DIGIT, COLON_NODE, SECOND_2_DIGIT, SPACE_NODE, TZ_CORRECTION);
    private static final GroupNode SOAP = GroupNode.of(
            "SOAP (YY \"-\" MM \"-\" DD \"T\" HH \":\" II \":\" SS frac tzcorrection?)",
            YEAR_4_DIGIT, MINUS_NODE, MONTH_2_DIGIT, MINUS_NODE, DAY_2_DIGIT, CharacterNode.of('T'), HOUR_24_NODE, COLON_NODE, MINUTE_2_DIGIT, COLON_NODE, SECOND_2_DIGIT, DOT_NODE, MICROSECONDS.followedByOptional(TZ_CORRECTION)
    );
    private static final MeridianNode MERIDIAN_NODE = MeridianNode.of();
    private static final GroupNode HOUR_WITH_MERIDIAN = GroupNode.of(
            "Hour only, with meridian",
            HOUR_12_NODE.followedByOptional(SPACE_NODE), MERIDIAN_NODE
    );
    private static final OrNode DOT_OR_COLON = OrNode.of(DOT_NODE, COLON_NODE);
    private static final GroupNode HOUR_MINUTE_WITH_MERIDIAN = GroupNode.of(
            "Hour and minutes, with meridian",
            HOUR_12_NODE,
            DOT_OR_COLON,
            MINUTE_2_DIGIT.followedByOptional(SPACE_NODE),
            MERIDIAN_NODE
    );
    private static final GroupNode HOUR_MINUTE_NO_MERIDIAN = GroupNode.builder()
            .name("Hour and minutes")
            .afterApply(ctx -> {
                if (ctx.isNotModified(ChronoField.SECOND_OF_MINUTE))
                    ctx.setSecond(0);
            })
            .nodes(HOUR_12_NODE, DOT_OR_COLON, MINUTE_ii_NODE)
            .build();
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
    private static final GroupNode YY_mm_dd_NODE = GroupNode.builder()
            .name("Four digit year, month and day with slashes (YY '/' mm '/' dd)")
            .resetTimeIfNotModified()
            .nodes(YEAR_4_DIGIT.then(SLASH_NODE).then(MONTH_mm_NODE).then(SLASH_NODE).then(DAY_dd_OPT_SUFFIX_NODE))
            .build();
    private static final GroupNode GNU_DATE = GroupNode.builder()
            .name("Four digit year and month (YY '-' mm)")
            .afterApply(ctx -> {
                if (!ctx.isTimeModified() && ctx.isNotModified(ChronoField.DAY_OF_MONTH)) {
                    ctx.setDayOfMonth(1).atStartOfDay();
                }
            })
            .nodes(YEAR_4_DIGIT.then(MINUS_NODE).then(MONTH_mm_NODE))
            .build();
    private static final GroupNode y_mm_dd_NODE = GroupNode.builder()
            .resetTimeIfNotModified()
            .name("Year, month and day with dashes (y '-' mm '-' dd)")
            .nodes(YEAR_y_NODE.then(MINUS_NODE).then(MONTH_mm_NODE).then(MINUS_NODE).then(DAY_dd_NODE))
            .build();
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
            .nodes(DAY_dd_OPT_SUFFIX_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(MONTH_m_NODE).then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(YEAR_y_NODE))
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
    private static final GroupNode YY_m_NODE = GroupNode.builder()
            .name("Four digit year and textual month (Day reset to 1) (YY ([ \\t.-])* m)")
            .afterApply(ctx -> {
                if (!ctx.isTimeModified())
                    ctx.atStartOfDay();

                if (ctx.isNotModified(ChronoField.DAY_OF_MONTH))
                    ctx.setDayOfMonth(1);
            })
            .nodes(YEAR_4_DIGIT.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(MONTH_m_NODE))
            .build();
    private static final GroupNode m_dd_y_NODE = GroupNode.builder().relative(false)
            .afterApply(ctx -> {
                if (!ctx.isTimeModified())
                    ctx.atStartOfDay();

                if (ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.COLON)) {
                    ctx.setYear(Year.now().getValue());
                    ctx.cursor().dec();
                }
            })
            .name("Textual month, day and year (m ([ .\\t-])* dd [,.stndrh\\t ]+ y)")
            .nodes(MONTH_m_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(DAY_dd_OPT_SUFFIX_NODE).then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS.or(COMMA_NODE)))
                    .then(YEAR_y_NODE).thenNot(COLON_NODE))
            .build();
    private static final GroupNode m_dd_NODE = GroupNode.builder()
            .name("Textual month and day (m ([ .\\t-])* dd [,.stndrh\\t ]*)")
            .resetTimeIfNotModified()
            .nodes(MONTH_m_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(DAY_dd_OPT_SUFFIX_NODE).then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS.or(COMMA_NODE))))
            .build();
    private static final GroupNode dd_m_NODE = GroupNode.of(
            "Day and textual month (d ([ .\\t-])* m)",
            DateTimeParserContext::atStartOfDay,
            DAY_dd_OPT_SUFFIX_NODE.then(ZeroOrMore.of(SPACE_OR_DOT_OR_MINUS)).then(MONTH_m_NODE)
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
    private static final GroupNode ISO8601_YY_MM_DD = GroupNode.builder()
            .resetTimeIfNotModified()
            .name("Four digit year with optional sign, month and day ([+-]? YY '-' MM '-' DD)")
            .nodes(PLUS_NODE.or(MINUS_NODE)
                    .optionalFollowedBy(YEAR_4_DIGIT.withConsumer(simpleYearAdjuster().andThen(ctx -> {
                        if (ctx.tokenAt(ctx.cursor().value() - 1).symbol() == DateTimeTokenizer.Symbol.MINUS) {
                            ctx.setYear(-ctx.dateTime().getYear());
                        }
                    })).then(MINUS_NODE).then(MONTH_2_DIGIT).then(MINUS_NODE).then(DAY_2_DIGIT)))
            .build();

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
    private static final GroupNode FIRST_DAY_OF = GroupNode.builder()
            .relative(true)
            .name("First day of")
            .afterApply(ctx -> {
                ctx.setDayOfMonth(1);
                if (ctx.isRelativeContributorPresent("month")) {
                    ctx.addRelativeContributor("first_day_of_month", 1);
                }
            })
            .priorityLow()
            .nodes(StringNode.ofCaseInsensitive("first").then(SPACE_NODE).then(StringNode.ofCaseInsensitive("day")).then(SPACE_NODE).then(STRING_OF_NODE))
            .build();
    private static final GroupNode LAST_DAY_OF = GroupNode.builder()
            .relative(true)
            .name("Sets the day to the last day of the current month. This phrase is best used together with a month name following it.")
            .priorityNormal()
            .afterApply(ctx -> {
                ctx.withAdjuster(TemporalAdjusters.lastDayOfMonth(), ChronoField.DAY_OF_MONTH);
                if (ctx.isRelativeContributorPresent("month")) {
                    ctx.addRelativeContributor("last_day_of_month", 1);
                }
            })
            .nodes(StringNode.ofCaseInsensitive("last").then(SPACE_NODE).then(StringNode.ofCaseInsensitive("day")).then(SPACE_NODE).then(STRING_OF_NODE))
            .build();
    private static final PatternNode ORDINAL_NODE = PatternNode.of(ORDINAL, DateTimeTokenizer.Symbol.STRING);
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
    private static final RelativeTimeNumber RELATIVE_TIME_NUMBER = new RelativeTimeNumber();

    private static final GroupNode REL_TIME = GroupNode.builder()
            .relative(true)
            .priorityLow()
            .name("Handles relative time items where the value is a number. (\"+5 weeks\", \"12 day\", \"-7 weekdays\")")
            .nodes(RELATIVE_TIME_NUMBER)
            .build();

    private static final GroupNode REL_TIME_SPECIAL = GroupNode.builder()
            .relative(true)
            .priorityLow()
            .name("number is an integer number; if a decimal number is given, the dot (or comma) is likely interpreted as delimiter. For instance, '+1.5 hours' is parsed like '+1 5 hours', not as '+1 hour +30 minutes'.")
            .nodes(TIMEZONE_INFORMATION.then(DOT_NODE).then(RELATIVE_TIME_NUMBER))
            .build();

    /**
     * The token symbols that occurs between groups should be skipped away.
     */
    private static final Set<DateTimeTokenizer.Symbol> SKIP_SYMBOLS = EnumSet.of(DateTimeTokenizer.Symbol.SPACE, DateTimeTokenizer.Symbol.COMMA, DateTimeTokenizer.Symbol.NEWLINE);

    static {
        parseTree.put(DateTimeTokenizer.Symbol.AT, Collections.singletonList(UNIX_TIMESTAMP));
        parseTree.put(DateTimeTokenizer.Symbol.PLUS, Arrays.asList(ISO8601_YY_MM_DD, REL_TIME, REL_TIME_SPECIAL, TIMEZONE_INFORMATION));
        parseTree.put(DateTimeTokenizer.Symbol.MINUS, Arrays.asList(ISO8601_YY_MM_DD, REL_TIME, REL_TIME_SPECIAL, TIMEZONE_INFORMATION));
        parseTree.put(DateTimeTokenizer.Symbol.STRING, Arrays.asList(
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

        parseTree.put(DateTimeTokenizer.Symbol.DIGITS, Arrays.asList(
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

                // 12 Hour formats
                HOUR_MINUTE_NO_MERIDIAN,

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
     * Already matched non relative nodes.
     */
    private final Queue<GroupNode> matched;

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
        this.matched = new ArrayDeque<>();
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
            Pair<TemporalAdjuster, TemporalField> pair = Adjusters.relativeUnit(ctx, ctx.readStringAtCursor(),
                    ctx.withCursorValue(snapshot - 2).readStringAtCursor());
            ctx.withAdjuster(pair.getA(), pair.getB());
        };
    }

    private static Consumer<DateTimeParserContext> monthStringAdjuster() {
        return ctx -> ctx.withAdjuster(Adjusters.month(ctx.readStringAtCursor()), ChronoField.MONTH_OF_YEAR);
    }

    public ZonedDateTime parse() throws DateTimeParserException {
        return parseResult().dateTime();
    }

    public DateTimeParseResult parseResult() throws DateTimeParserException {
        List<DateTimeTokenizer.Token> tokens = tokenize();
        DateTimeParserContext ctx = new DateTimeParserContext(tokens, new DateTimeParserContext.Cursor(), tokenizer, baseDateTime);

        parseNormal(ctx);

        // saving cursor value before apply relative statements
        int snapshot = ctx.cursor().value();

        // the normal process is over its time to apply all relative nodes
        relatives.forEach(relNode -> relNode.apply(ctx));

        if (!ctx.hasModifications()) {
            throw dateTimeParserException(snapshot);
        }

        // restoring cursor
        if (ctx.withCursorValue(snapshot).hasMoreTokens()) {
            throw dateTimeParserException(snapshot);
        }

        return ctx.result();
    }

    private DateTimeParserException dateTimeParserException(int snapshot) {
        return new DateTimeParserException(tokenizer.original(), snapshot);
    }

    private void parseNormal(DateTimeParserContext ctx) {
        int lastMatchIdx = 0;
        boolean matches = true;

        while (ctx.hasMoreTokens() && matches) {
            if (SKIP_SYMBOLS.contains(ctx.symbolAtCursor())) {
                ctx.cursor().inc();
                lastMatchIdx++;
                continue;
            }

            DateTimeTokenizer.Symbol symbol = ctx.symbolAtCursor();
            List<GroupNode> groupNodes = parseTree.get(symbol);

            if (groupNodes == null) {
                throw new DateTimeException("Cannot find parser tree for symbol: " + symbol);
            }

            for (GroupNode group : groupNodes) {
                matches = group.matches(ctx);
                if (matches) {
                    // we should delay processing the relative groups
                    if (group.isRelative()) {
                        relatives.offer(new CursorAwareNode(lastMatchIdx, group));
                    } else {
                        matched.offer(group);
                        ctx.cursor().setValue(lastMatchIdx);
                        group.apply(ctx);
                    }

                    lastMatchIdx = ctx.cursor().value();
                    break;
                }
            }
        }
    }

    private List<DateTimeTokenizer.Token> tokenize() {
        List<DateTimeTokenizer.Token> tokens = new ArrayList<>();

        DateTimeTokenizer.Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);
        return tokens;
    }

    private static class ZeroOrMore extends Node {
        private final Node node;

        private ZeroOrMore(Node node) {
            this.node = node;
        }

        static ZeroOrMore of(Node node) {
            return new ZeroOrMore(node);
        }

        @Override
        boolean matches(DateTimeParserContext ctx) {
            //noinspection StatementWithEmptyBody
            while (node.matches(ctx)) { /*empty*/ }

            return true;
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int snapshot = ctx.cursor().value();
            while (node.matches(ctx)) {
                if (snapshot != ctx.cursor().value()) {
                    node.apply(ctx.withCursorValue(snapshot));
                    snapshot = ctx.cursor().value();
                }
            }
        }
    }

    private static class UnixTimestamp extends SymbolNode {
        private UnixTimestamp() {
            super(DateTimeTokenizer.Symbol.DIGITS);
        }

        static UnixTimestamp of() {
            return new UnixTimestamp();
        }

        @Override
        public boolean matches(DateTimeParserContext ctx) {
            if (ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.MINUS))
                ctx.cursor().inc();

            return super.matches(ctx);
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int sign = 1;
            if (ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.MINUS)) {
                ctx.cursor().inc();
                sign = -1;
            }

            // FIXME: handle overflow
            long timestamp = ctx.readLongAtCursor() * sign;
            ctx.setUnixTimestamp(timestamp).cursor().inc();
        }
    }

    private abstract static class VariableLengthSymbol extends SymbolNode {
        private final int min;
        private final int max;

        VariableLengthSymbol(DateTimeTokenizer.Symbol symbol, int min, int max) {
            super(symbol);
            this.min = min;
            this.max = max;
        }

        int min() {
            return min;
        }

        int max() {
            return max;
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            DateTimeTokenizer.Token current = ctx.tokens().get(ctx.cursor().value());
            return current.length() >= min && current.length() <= max;
        }
    }

    private static class Year4 extends FixedLengthSymbol {
        private final Consumer<DateTimeParserContext> applier;

        private Year4(Consumer<DateTimeParserContext> applier) {
            super(DateTimeTokenizer.Symbol.DIGITS, 4);
            this.applier = applier;
        }

        static Year4 of() {
            return new Year4(simpleYearAdjuster());
        }

        public Year4 withConsumer(Consumer<DateTimeParserContext> consumer) {
            return new Year4(consumer);
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            applier.accept(ctx);
            ctx.cursor().inc();
        }
    }

    /**
     * Matches following pattern: YYYYMMDD
     */
    private static class YearMonthDay extends FixedLengthSymbol {
        private static final Pattern PATTERN = Pattern.compile("[0-9]{4}([0][0-9]|1[0-2])([01][0-9]|2[0-4])");

        private YearMonthDay() {
            super(DateTimeTokenizer.Symbol.DIGITS, 8);
        }

        static YearMonthDay of() {
            return new YearMonthDay();
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            CharBuffer cb = ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()));
            return PATTERN.matcher(cb).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int start = ctx.tokenAtCursor().start();

            DateTimeTokenizer tokenizer = ctx.tokenizer();
            int year = tokenizer.readInt(start, 4);
            int month = tokenizer.readInt(start + 4, 2);
            int day = tokenizer.readInt(start + 6, 2);

            ctx.setYear(year).setMonth(month).setDayOfMonth(day);
            ctx.cursor().inc();
        }
    }

    /**
     * Matches tokens with specified symbol.
     */
    private static class SymbolNode extends Node {
        private final DateTimeTokenizer.Symbol symbol;

        SymbolNode(DateTimeTokenizer.Symbol symbol) {
            this.symbol = symbol;
        }

        static SymbolNode of(DateTimeTokenizer.Symbol symbol) {
            return new SymbolNode(symbol);
        }

        DateTimeTokenizer.Symbol symbol() {
            return symbol;
        }

        @Override
        public boolean matches(DateTimeParserContext ctx) {
            if (ctx.isSymbolAtCursor(symbol)) {
                boolean match = matchesInternal(ctx);
                if (match)
                    ctx.cursor().inc();

                return match;
            }

            return false;
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            ctx.cursor().inc();
        }

        public boolean matchesInternal(DateTimeParserContext ctx) {
            return true;
        }

        @Override
        public String toString() {
            return symbol.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SymbolNode)) return false;
            SymbolNode that = (SymbolNode) o;
            return symbol == that.symbol;
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol);
        }
    }

    private static class TimezoneCorrectionNode extends Node {
        private static final Pattern OFFSET_PREFIX = Pattern.compile("GMT|UTC|UT");
        private static final Pattern TIMEZONE_COMMENT = Pattern.compile("\\(([a-zA-Z]{1,6})\\)");
        private static final Pattern TZ_HOUR_PART = Pattern.compile("0?[0-9]|1[0-2]");
        private static final Pattern TZ_MINUTE_PART = Pattern.compile("[0-9]|[0-5][0-9]");
        private static final Pattern hhMM = Pattern.compile("(0?[0-9]|1[0-2])[0-5][0-9]?");
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

            PatternNode hh = PatternNode.ofDigits(TimezoneCorrectionNode.TZ_HOUR_PART, signedHour);
            PatternNode hhMM = PatternNode.ofDigits(TimezoneCorrectionNode.hhMM, signedHour);
            PatternNode MM_NODE = PatternNode.ofDigits(TZ_MINUTE_PART, c -> sb.append(String.format("%02d", c.readIntAtCursor())));
            PatternNode PREFIX_NODE = PatternNode.of(OFFSET_PREFIX, DateTimeTokenizer.Symbol.STRING, c -> append(sb, c));
            // TODO: check valid comment
            PatternNode TZ_COMMENT = PatternNode.of(TIMEZONE_COMMENT, DateTimeTokenizer.Symbol.STRING);

            Node node = PREFIX_NODE.optionalFollowedBy(
                    SIGN.then(hh.then(COLON_NODE).then(MM_NODE).or(hhMM.or(hh)))
                            .followedByOptional(SPACE_NODE.then(TZ_COMMENT))
            );

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
            if (c.isSymbolAtCursor(DateTimeTokenizer.Symbol.PLUS) || c.isSymbolAtCursor(DateTimeTokenizer.Symbol.MINUS)) {
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

    /**
     * The full timezone node.
     */
    private static class TimezoneNode extends Node {
        private static final Pattern TZ_SHORT = Pattern.compile("[A-Za-z]{1,6}");
        private static final CharacterNode BRACKET_OPEN = CharacterNode.of('(');
        private static final CharacterNode BRACKET_CLOSE = CharacterNode.of(')');
        private static final Pattern REGION = Pattern.compile("[A-Z][A-Za-z]+");
        private static final Pattern REGION_CI = Pattern.compile("[A-Za-z]+");

        public static TimezoneNode of() {
            return new TimezoneNode();
        }

        @Override
        boolean matches(DateTimeParserContext ctx) {
            return matchesInternal(ctx, null);
        }

        private boolean matchesInternal(DateTimeParserContext ctx, StringBuilder sb) {
            int snapshot = ctx.cursor().value();

            Consumer<DateTimeParserContext> appender = charBufferAppender(sb);

            PatternNode TZ_SHORT_NODE = PatternNode.of(TZ_SHORT, DateTimeTokenizer.Symbol.STRING, appender);
            Node underscoreSlashOrMinus = UNDERSCORE_NODE.or(SLASH_NODE).or(MINUS_NODE)
                    .with(charAppender(sb).andThen(cursorIncrementer()));
            Node shortTimezone = BRACKET_OPEN.then(TZ_SHORT_NODE).then(BRACKET_CLOSE).or(TZ_SHORT_NODE);
            PatternNode REGION_NODE = PatternNode.of(REGION, DateTimeTokenizer.Symbol.STRING, appender);
            Node regionBaseTimezoneNode = REGION_NODE
                    .then(SLASH_NODE.with(appender))
                    .then(REGION_NODE)
                    .then(ZeroOrMore.of(
                            underscoreSlashOrMinus.then(PatternNode.of(REGION_CI, DateTimeTokenizer.Symbol.STRING, appender))
                    ));

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
    }

    private static class OneOrMore extends GroupNode {
        OneOrMore(String name, Node[] nodes) {
            super(name, nodes, false, PRIORITY_LOW, empty());
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

            return isFullMatch(i);
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

    private static class StringNode extends FixedLengthSymbol {
        private final String value;
        private final BiFunction<String, String, Boolean> matcher;
        private final Consumer<DateTimeParserContext> applier;

        private StringNode(String value, boolean caseSensitive, Consumer<DateTimeParserContext> applier) {
            super(DateTimeTokenizer.Symbol.STRING, value.length());
            this.value = value;
            this.matcher = caseSensitive ? String::equals : String::equalsIgnoreCase;
            this.applier = applier == empty() ?
                    cursorIncrementer() :
                    applier.andThen(cursorIncrementer());
        }

        static Node of(String value) {
            return of(value, empty());
        }

        static Node of(String value, Consumer<DateTimeParserContext> applier) {
            if (value.length() == 1)
                return CharacterNode.of(value.charAt(0));

            return new StringNode(value, true, applier);
        }

        static Node ofCaseInsensitive(String value) {
            return ofCaseInsensitive(value, empty());
        }

        static Node ofCaseInsensitive(String value, Consumer<DateTimeParserContext> applier) {
            if (value.length() == 1)
                return CharacterNode.ofCaseInsensitive(value.charAt(0));

            return new StringNode(value, false, applier);
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            if (!super.matchesInternal(ctx))
                return false;

            return matcher.apply(value, ctx.readStringAtCursor());
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", StringNode.class.getSimpleName() + "[", "]")
                    .add("value='" + value + "'")
                    .toString();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            applier.accept(ctx);
        }
    }

    private static class Second2 extends FixedLengthSymbol {
        private Second2() {
            super(DateTimeTokenizer.Symbol.DIGITS, 2);
        }

        public static Second2 of() {
            return new Second2();
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            CharBuffer input = ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()));
            return SECOND_SS.matcher(input).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int sec = ctx.readIntAtCursor();

            ctx.setSecond(sec);
            ctx.cursor().inc();
        }
    }

    private static class RelativeTimeNumber extends Node {
        @Override
        boolean matches(DateTimeParserContext ctx) {
            return matchesInternal(ctx) != null;
        }

        private Pair<Long, String> matchesInternal(DateTimeParserContext ctx) {
            int snapshot = ctx.cursor().value();

            int sign = 1;
            if (ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.PLUS) || ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.MINUS)) {
                sign = ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.PLUS) ? 1 : -1;
                ctx.cursor().inc();
                ctx.skipWhitespaces();
            }

            if (!ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.DIGITS)) {
                ctx.withCursorValue(snapshot);
                return null;
            }

            long value = ctx.readLongAtCursorAndInc() * sign;

            ctx.skipWhitespaces();
            if (!ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.STRING)) {
                ctx.withCursorValue(snapshot);
                return null;
            }

            Matcher matcher = UNIT.matcher(ctx.readCharBufferAtCursor());
            boolean unitMatches = matcher.matches();

            if (!unitMatches) {
                ctx.withCursorValue(snapshot);
                return null;
            }

            String unit = ctx.readStringAtCursorAndInc().toLowerCase();

            // try to find "ago" token.
            if (ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.SPACE)) {
                snapshot = ctx.cursor().value();
                ctx.cursor().inc();

                if (ctx.isSymbolAtCursor(DateTimeTokenizer.Symbol.STRING) && "ago".equalsIgnoreCase(ctx.readStringAtCursor())) {
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
            Pair<TemporalAdjuster, TemporalField> pair1 = relativeUnit(ctx, pair.getB(), pair.getA());
            ctx.withAdjuster(pair1.getA(), pair1.getB());
        }
    }

    private static class PatternNode extends SymbolNode {
        private final Pattern pattern;
        private final Consumer<DateTimeParserContext> consumer;

        private PatternNode(Pattern pattern, DateTimeTokenizer.Symbol symbol, Consumer<DateTimeParserContext> consumer) {
            super(symbol);
            this.pattern = pattern;
            this.consumer = consumer;
        }

        static PatternNode ofDigits(Pattern pattern, Consumer<DateTimeParserContext> adjuster) {
            return new PatternNode(pattern, DateTimeTokenizer.Symbol.DIGITS, adjuster);
        }

        static PatternNode ofDigits(Pattern pattern) {
            return new PatternNode(pattern, DateTimeTokenizer.Symbol.DIGITS, empty());
        }

        static PatternNode of(Pattern pattern, DateTimeTokenizer.Symbol symbol, Consumer<DateTimeParserContext> consumer) {
            return new PatternNode(pattern, symbol, consumer);
        }

        static PatternNode ofString(Pattern pattern, Consumer<DateTimeParserContext> consumer) {
            return new PatternNode(pattern, DateTimeTokenizer.Symbol.STRING, consumer);
        }

        static PatternNode ofString(Pattern pattern) {
            return ofString(pattern, empty());
        }

        static PatternNode of(Pattern pattern, DateTimeTokenizer.Symbol symbol) {
            return new PatternNode(pattern, symbol, empty());
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            return pattern.matcher(ctx.readCharBufferAtCursor()).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            consumer.accept(ctx);
            ctx.cursor().inc();
        }

        public PatternNode withConsumer(Consumer<DateTimeParserContext> consumer) {
            return new PatternNode(pattern, symbol(), consumer);
        }

        @Override
        public String toString() {
            return pattern.pattern();
        }
    }

    private static class OrNode extends Node {
        private final Node l;
        private final Node r;
        private final Consumer<DateTimeParserContext> apply;

        private OrNode(Node l, Node r, Consumer<DateTimeParserContext> apply) {
            this.l = l;
            this.r = r;
            this.apply = apply;
        }

        static OrNode of(Node l, Node r) {
            return new OrNode(l, r, empty());
        }

        static OrNode of(Node l, Node r, Consumer<DateTimeParserContext> apply) {
            return new OrNode(l, r, apply);
        }

        OrNode with(Consumer<DateTimeParserContext> apply) {
            return of(l, r, apply);
        }

        @Override
        public boolean matches(DateTimeParserContext ctx) {
            return l.matches(ctx) || r.matches(ctx);
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int snapshot = ctx.cursor().value();
            Node matched = null;

            if (l.matches(ctx)) {
                matched = l;
            } else if (r.matches(ctx)) {
                matched = r;
            }

            if (matched != null) {
                ctx = ctx.withCursorValue(snapshot);

                if (apply == empty()) {
                    matched.apply(ctx);
                } else {
                    apply.accept(ctx);
                }
            }
        }

        @Override
        public String toString() {
            return "(" + l + " OR " + r + ")";
        }
    }

    private abstract static class Node {
        abstract boolean matches(DateTimeParserContext ctx);

        abstract void apply(DateTimeParserContext ctx);

        Node followedByOptional(Node node) {
            return OrNode.of(GroupNode.builder().nodes(this, node).build(), this);
        }

        Node optionalFollowedBy(Node node) {
            return OrNode.of(GroupNode.builder().nodes(this, node).build(), node);
        }

        OrNode or(Node alt) {
            return OrNode.of(this, alt);
        }

        AndNode then(Node node) {
            return AndNode.of(this, node);
        }

        AndNode thenNot(Node node) {
            return AndNode.of(this, NotNode.of(node));
        }
    }

    private static class Month2 extends FixedLengthSymbol {
        Month2() {
            super(DateTimeTokenizer.Symbol.DIGITS, 2);
        }

        public static Month2 of() {
            return new Month2();
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            return MONTH_MM.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int month = ctx.readIntAtCursor();

            ctx.setMonth(month);
            ctx.cursor().inc();
        }
    }

    private static class Minute2 extends FixedLengthSymbol {
        private Minute2() {
            super(DateTimeTokenizer.Symbol.DIGITS, 2);
        }

        public static Minute2 of() {
            return new Minute2();
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            return MINUTE_II.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int minute = ctx.readIntAtCursor();
            ctx.setMinute(minute);
            ctx.cursor().inc();
        }
    }

    private static class Microseconds extends SymbolNode {
        private Microseconds() {
            super(DateTimeTokenizer.Symbol.DIGITS);
        }

        static Microseconds of() {
            return new Microseconds();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int l = ctx.tokenAtCursor().length();
            int m = 1_000_000;

            while (l-- > 0) m /= 10;

            int micro = ctx.readIntAtCursor() * m;

            ctx.setMicroseconds(micro).cursor().inc();
        }
    }

    private static class MeridianNode extends Node {
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
                    GroupNode.of(PatternNode.of(AM_PM, DateTimeTokenizer.Symbol.STRING, charBufferAppender(buff)), DOT_NODE),
                    GroupNode.of(PatternNode.of(AM_PM, DateTimeTokenizer.Symbol.STRING, charBufferAppender(buff))),
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

    private static class HourMinuteSecond extends VariableLengthSymbol {
        private static final Pattern PATTERN = Pattern.compile("((0?[1-9]|1[0-2])|([01][0-9]|2[0-4]))[0-5][0-9][0-5][0-9]");

        private HourMinuteSecond(int min, int max) {
            super(DateTimeTokenizer.Symbol.DIGITS, min, max);
        }

        static HourMinuteSecond of(int min, int max) {
            return new HourMinuteSecond(min, max);
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            return PATTERN.matcher(ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()))).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            DateTimeTokenizer.Token token = ctx.tokenAtCursor();
            int start = token.start();
            int offset = token.length() - max();

            DateTimeTokenizer tokenizer = ctx.tokenizer();
            int hour = tokenizer.readInt(start, 2 + offset);
            int minute = tokenizer.readInt(start + 2 + offset, 2);
            int second = tokenizer.readInt(start + 4 + offset, 2);

            ctx.setHour(hour).setMinute(minute).setSecond(second);
            ctx.cursor().inc();
        }
    }

    /**
     * Matches the hour in 24 hour format: "04", "07", "19" including "24"
     */
    private static class Hour24 extends FixedLengthSymbol {
        private Hour24() {
            super(DateTimeTokenizer.Symbol.DIGITS, 2);
        }

        static Hour24 of() {
            return new Hour24();
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            CharBuffer cb = ctx.tokenizer().readCharBuffer(ctx.tokenAtCursor());
            return HOUR_24.matcher(cb).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int hour = ctx.readIntAtCursor();

            if (hour == 24) {
                ctx.plusDays(1).setHour(0);
            } else {
                ctx.setHour(hour);
            }
            ctx.cursor().inc();
        }
    }

    private static class Hour12 extends VariableLengthSymbol {
        private Hour12() {
            super(DateTimeTokenizer.Symbol.DIGITS, 1, 2);
        }

        static Hour12 of() {
            return new Hour12();
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            CharBuffer cb = ctx.tokenizer().readCharBuffer(ctx.tokens().get(ctx.cursor().value()));
            return HOUR_12.matcher(cb).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            int hour = ctx.readIntAtCursor();
            ctx.setHour(hour).cursor().inc();
        }
    }

    private static class GroupNode extends Node implements Iterable<Node> {
        static final int PRIORITY_HIGH = 1000;
        static final int PRIORITY_NORMAL = PRIORITY_HIGH - 500;
        static final int PRIORITY_LOW = PRIORITY_NORMAL - 500;

        private final String name;
        private final boolean relative;
        private final int priority;
        private final Node[] nodes;
        private final Consumer<DateTimeParserContext> afterApply;

        GroupNode(String name, Node[] nodes, boolean relative, int priority, Consumer<DateTimeParserContext> afterApply) {
            this.name = name;
            this.nodes = nodes;
            this.relative = relative;
            this.priority = priority;
            this.afterApply = afterApply;
        }

        static GroupNode of(String name, Node... nodes) {
            return new GroupNode(name, nodes, false, PRIORITY_LOW, empty());
        }

        static GroupNode of(String name, Consumer<DateTimeParserContext> afterApply, Node... nodes) {
            return new GroupNode(name, nodes, false, PRIORITY_LOW, afterApply);
        }

        static GroupNode of(Node... nodes) {
            return new GroupNode("noname", nodes, false, PRIORITY_LOW, empty());
        }

        static GroupNodeBuilder builder() {
            return new GroupNodeBuilder();
        }

        @Override
        boolean matches(DateTimeParserContext ctx) {
            Iterator<Node> nodeIt = iterator();
            DateTimeParserContext.Cursor cursor = ctx.cursor();
            int mark = cursor.value();

            while (ctx.hasMoreTokens() && nodeIt.hasNext()) {
                Node node = nodeIt.next();

                if (!node.matches(ctx)) {
                    cursor.setValue(mark);
                    return false;
                }
            }

            if (nodeIt.hasNext()) {
                cursor.setValue(mark);
                return false;
            }

            return true;
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            if (nodes.length == 1) {
                nodes[0].apply(ctx);
            } else {
                for (Node node : nodes) {
                    node.apply(ctx);
                }
            }

            afterApply.accept(ctx);
        }

        boolean isRelative() {
            return relative;
        }

        int priority() {
            return priority;
        }

        @Override
        public Iterator<Node> iterator() {
            if (nodes.length == 1)
                return Collections.singletonList(nodes[0]).iterator();

            return Arrays.asList(nodes).iterator();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GroupNode)) return false;
            GroupNode nodes1 = (GroupNode) o;
            return relative == nodes1.relative &&
                    priority == nodes1.priority &&
                    name.equals(nodes1.name) &&
                    Arrays.equals(nodes, nodes1.nodes) &&
                    afterApply.equals(nodes1.afterApply);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name, relative, priority, afterApply);
            result = 31 * result + Arrays.hashCode(nodes);
            return result;
        }

        Node[] nodes() {
            return nodes;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", GroupNode.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("nodes=" + Arrays.toString(nodes))
                    .add("relative=" + relative)
                    .toString();
        }

        static final class GroupNodeBuilder {
            private String name = "noname";
            private boolean relative;
            private int priority = PRIORITY_LOW;
            private Node[] nodes;
            private Consumer<DateTimeParserContext> afterApply = empty();

            private GroupNodeBuilder() {
            }

            public GroupNodeBuilder name(String name) {
                this.name = name;
                return this;
            }

            GroupNodeBuilder relative(boolean relative) {
                this.relative = relative;
                return this;
            }

            GroupNodeBuilder priority(int priority) {
                this.priority = priority;
                return this;
            }

            GroupNodeBuilder priorityHigh() {
                return priority(PRIORITY_HIGH);
            }

            public GroupNodeBuilder priorityNormal() {
                return priority(PRIORITY_NORMAL);
            }

            public GroupNodeBuilder priorityLow() {
                return priority(PRIORITY_LOW);
            }

            GroupNodeBuilder nodes(Node... nodes) {
                this.nodes = nodes;
                return this;
            }

            public GroupNodeBuilder afterApply(Consumer<DateTimeParserContext> afterApply) {
                this.afterApply = afterApply;
                return this;
            }

            public GroupNodeBuilder afterApplyResetTime() {
                return afterApply(DateTimeParserContext::atStartOfDay);
            }

            public GroupNodeBuilder resetTimeIfNotModified() {
                return afterApply(ctx -> {
                    if (!ctx.isTimeModified())
                        ctx.atStartOfDay();
                });
            }

            GroupNode build() {
                return new GroupNode(name, nodes, relative, priority, afterApply);
            }
        }
    }

    /**
     * Matches tokens with specified length.
     */
    private abstract static class FixedLengthSymbol extends SymbolNode {
        private final int length;

        FixedLengthSymbol(DateTimeTokenizer.Symbol symbol, int length) {
            super(symbol);
            this.length = length;
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            return ctx.tokenAtCursor().length() == length;
        }

        @Override
        public String toString() {
            return symbol() + "{" + length + "}";
        }
    }

    private static class Day2 extends FixedLengthSymbol {
        private Day2() {
            super(DateTimeTokenizer.Symbol.DIGITS, 2);
        }

        public static Day2 of() {
            return new Day2();
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            return DAY_DD.matcher(ctx.tokenizer().readCharBuffer(ctx.tokenAtCursor())).matches();
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            ctx.setDayOfMonth(ctx.readIntAtCursor());
            ctx.cursor().inc();
        }
    }

    private static class AndNode extends Node {
        private final Node l;
        private final Node r;

        AndNode(Node l, Node r) {
            this.l = l;
            this.r = r;
        }

        static AndNode of(Node l, Node r) {
            return new AndNode(l, r);
        }

        @Override
        public boolean matches(DateTimeParserContext ctx) {
            if (l.matches(ctx)) {
                if (r.matches(ctx)) {
                    return true;
                } else {
                    ctx.cursor().dec();
                    return false;
                }
            }
            return false;
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            l.apply(ctx);
            r.apply(ctx);
        }

        @Override
        public String toString() {
            return "(" + l + " AND " + r + ")";
        }
    }

    private static class CharacterNode extends SymbolNode {
        private final char c;
        private final boolean caseSensitive;
        private final Consumer<DateTimeParserContext> apply;

        private CharacterNode(DateTimeTokenizer.Symbol symbol, char c, Consumer<DateTimeParserContext> apply, boolean caseSensitive) {
            super(symbol);
            this.c = c;
            this.caseSensitive = caseSensitive;
            this.apply = apply;
        }

        static CharacterNode of(char c) {
            return new CharacterNode(DateTimeTokenizer.Symbol.STRING, c, empty(), true);
        }

        static CharacterNode of(char c, DateTimeTokenizer.Symbol symbol) {
            return new CharacterNode(symbol, c, empty(), true);
        }

        static CharacterNode of(char c, Consumer<DateTimeParserContext> apply) {
            return new CharacterNode(DateTimeTokenizer.Symbol.STRING, c, apply, true);
        }

        static CharacterNode ofCaseInsensitive(char c) {
            return new CharacterNode(DateTimeTokenizer.Symbol.STRING, c, empty(), false);
        }

        static CharacterNode ofCaseInsensitive(char c, Consumer<DateTimeParserContext> apply) {
            return new CharacterNode(DateTimeTokenizer.Symbol.STRING, c, apply, false);
        }

        @Override
        public boolean matchesInternal(DateTimeParserContext ctx) {
            if (!super.matchesInternal(ctx)) {
                return false;
            }

            DateTimeTokenizer tokenizer = ctx.tokenizer();
            DateTimeTokenizer.Token current = ctx.tokenAtCursor();
            return caseSensitive ? tokenizer.readChar(current) == c :
                    Character.toLowerCase(tokenizer.readChar(current)) == Character.toLowerCase(c);
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            if (apply != empty())
                apply.accept(ctx);
            ctx.cursor().inc();
        }

        public CharacterNode with(Consumer<DateTimeParserContext> consumer) {
            return new CharacterNode(symbol(), c, consumer, caseSensitive);
        }

        @Override
        public String toString() {
            if (caseSensitive)
                return "(" + c + ")";

            return "(" + Character.toLowerCase(c) + " OR " + Character.toUpperCase(c) + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CharacterNode)) return false;
            if (!super.equals(o)) return false;
            CharacterNode that = (CharacterNode) o;
            return c == that.c &&
                    caseSensitive == that.caseSensitive &&
                    apply.equals(that.apply);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), c, caseSensitive, apply);
        }
    }

    private static class NotNode extends Node {
        private final Node node;

        private NotNode(Node node) {
            this.node = node;
        }

        static NotNode of(Node node) {
            return new NotNode(node);
        }

        @Override
        boolean matches(DateTimeParserContext ctx) {
            return !node.matches(ctx);
        }

        @Override
        void apply(DateTimeParserContext ctx) {
        }
    }

    private static class CursorAwareNode extends Node implements Comparable<CursorAwareNode> {
        private final int cursor;
        private final GroupNode node;

        CursorAwareNode(int cursor, GroupNode node) {
            this.cursor = cursor;
            this.node = node;
        }

        @Override
        boolean matches(DateTimeParserContext ctx) {
            return node.matches(ctx.withCursorValue(cursor));
        }

        @Override
        void apply(DateTimeParserContext ctx) {
            node.apply(ctx.withCursorValue(cursor));
        }

        @Override
        public int compareTo(CursorAwareNode o) {
            return Integer.compare(node.priority(), o.node.priority());
        }
    }
}
