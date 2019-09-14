package org.develnext.jphp.zend.ext.standard.date;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.develnext.jphp.zend.ext.standard.date.Adjusters.nextOrSameDayOfWeek;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.DAY_NAME;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MERIDIAN;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MICROSECONDS;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MINUTE_II;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.MONTH_mm;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.ORDINAL_SUFFIX;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.SECOND_SS;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YEAR_Y;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.YEAR_yy;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayDeque;
import java.util.Formatter;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.develnext.jphp.zend.ext.standard.DateConstants;

import php.runtime.annotation.Reflection.Ignore;
import php.runtime.env.Environment;

@Ignore
public class DateFormat {

    private DateFormat() {
    }

    private static Formatter formatForDateFunction(Environment env, DateTimeParseResult parseResult, String format, Formatter fmt)
            throws IOException {

        ZonedDateTime date = parseResult.dateTime();
        Appendable out = fmt.out();
        for (int i = 0, n = format.length(); i < n; i++) {
            char c = format.charAt(i);

            switch (c) {
                case 'Y': {
                    int year = date.getYear();
                    if (year < 0) {
                        year = -year;
                        out.append('-');
                    }

                    fmt.format("%04d", year);
                    break;
                }
                case 'y':
                    fmt.format("%02d", date.getYear() % 100);
                    break;
                case 'm':
                    fmt.format("%02d", date.getMonthValue());
                    break;
                case 'M':
                    out.append(date.getMonth().getDisplayName(TextStyle.SHORT, env.getLocale()));
                    break;
                case 'd':
                    fmt.format("%02d", date.getDayOfMonth());
                    break;
                case 'j':
                    out.append(Integer.toString(date.getDayOfMonth()));
                    break;
                case 'S': {
                    int dayOfMonth = date.getDayOfMonth();
                    String suffix;
                    switch (dayOfMonth) {
                        case 1:
                        case 21:
                        case 31:
                            suffix = "st";
                            break;
                        case 2:
                        case 22:
                            suffix = "nd";
                            break;
                        case 3:
                        case 23:
                            suffix = "rd";
                            break;
                        default:
                            suffix = "th";
                            break;
                    }
                    out.append(suffix);
                    break;
                }
                case 'w':
                case 'N':
                    out.append(Integer.toString(date.getDayOfWeek().getValue()));
                    break;
                case 'D':
                    out.append(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, env.getLocale()));
                    break;
                case 'W':
                    TemporalField woy = WeekFields.of(DayOfWeek.MONDAY, 4).weekOfWeekBasedYear();
                    fmt.format("%02d", date.get(woy));
                    break;
                case 'o':
                    fmt.format("%04d", date.get(IsoFields.WEEK_BASED_YEAR));
                    break;
                case 'F':
                    out.append(date.getMonth().getDisplayName(TextStyle.FULL, env.getLocale()));
                    break;
                case 'H':
                    fmt.format("%02d", date.getHour());
                    break;
                case 'i':
                    fmt.format("%02d", date.getMinute());
                    break;
                case 's':
                    fmt.format("%02d", date.getSecond());
                    break;
                case 'u':
                    fmt.format("%06d", date.get(ChronoField.MICRO_OF_SECOND));
                    break;
                case 'v':
                    fmt.format("%03d", date.get(ChronoField.MILLI_OF_SECOND));
                    break;
                case 'z':
                    out.append(Integer.toString(date.getDayOfYear() - 1));
                    break;
                case 'n':
                    out.append(Integer.toString(date.getMonthValue()));
                    break;
                case 't':
                    out.append(Integer.toString(date.getMonth().length(Year.isLeap(date.getYear()))));
                    break;
                case 'L':
                    out.append(Year.isLeap(date.getYear()) ? '1' : '0');
                    break;
                case 'l':
                    out.append(date.getDayOfWeek().getDisplayName(TextStyle.FULL, env.getLocale()));
                    break;
                case 'a':
                    out.append(date.getHour() >= 12 ? "pm" : "am");
                    break;
                case 'A':
                    out.append(date.getHour() >= 12 ? "PM" : "AM");
                    break;
                case 'B': {
                    ZonedDateTime atUTC = date.withZoneSameInstant(ZoneId.of("UTC+01:00"));
                    int beats = (int) ((atUTC.get(SECOND_OF_MINUTE) +
                            (atUTC.get(ChronoField.MINUTE_OF_HOUR) * 60) + (atUTC.get(ChronoField.HOUR_OF_DAY) * 3600)) / 86.4);
                    fmt.format("%03d", beats);
                    break;
                }
                case 'g': {
                    int hRem = date.getHour() % 12;
                    out.append(Integer.toString(hRem > 0 ? hRem : 12));
                    break;
                }
                case 'G': {
                    out.append(Integer.toString(date.getHour()));
                    break;
                }
                case 'Z': {
                    out.append(Integer.toString(date.getOffset().getTotalSeconds()));
                    break;
                }
                case 'e': {
                    if (parseResult.hasParsedZone()) {
                        out.append(parseResult.parsedZone());
                    } else if ("Z".equals(date.getZone().getId())) {
                        out.append("+00:00");
                    } else {
                        out.append(date.getZone().getId());
                    }
                    break;
                }
                case 'O': {
                    Duration duration = Duration.ofSeconds(date.getOffset().getTotalSeconds());
                    long hours = duration.toHours();
                    long minutes = Math.abs(duration.toMinutes() % 60);
                    out.append((hours < 0) ? "-" : "+");
                    fmt.format("%02d", Math.abs(hours)).format("%02d", minutes);
                    break;
                }
                case 'P': {
                    if ("Z".equals(date.getOffset().getId())) {
                        out.append("+00:00");
                    } else {
                        out.append(date.getOffset().toString());
                    }
                    break;
                }
                case 'T': {
                    if (parseResult.hasParsedZone()) {
                        out.append(parseResult.parsedZone());
                    } else if (date.getZone() instanceof ZoneOffset) {
                        out.append("GMT");
                        formatForDateFunction(env, parseResult, "O", fmt);
                    } else {
                        String str = ZoneIdFactory.aliasFor(date);
                        if (str != null) {
                            out.append(str);
                        } else {
                            Duration duration = Duration.ofSeconds(date.getOffset().getTotalSeconds());
                            long hours = duration.toHours();
                            out.append(hours < 0 ? '-' : '+');
                            fmt.format("%02d", Math.abs(hours));

                            long minutes = Math.abs(duration.toMinutes() % 60);
                            if (minutes > 0) {
                                fmt.format("%02d", minutes);
                            }
                        }
                    }

                    break;
                }
                case 'I': {
                    out.append(date.getZone().getRules().isDaylightSavings(date.toInstant()) ? '1' : '0');
                    break;
                }
                // Full Date/Time
                case 'c': {
                    formatForDateFunction(env, parseResult, DateConstants.DATE_ISO8601.toString(), fmt);
                    break;
                }
                case 'r': {
                    formatForDateFunction(env, parseResult, DateConstants.DATE_RFC2822.toString(), fmt);
                    break;
                }
                case 'U': {
                    out.append(Long.toString(date.toEpochSecond()));
                    break;
                }
                case '\\': {
                    if (i + 1 < n)
                        out.append(format.charAt(++i));

                    break;
                }
                default: {
                    out.append(c);
                }
                break;
            }
        }

        return fmt;
    }

    public static String formatForDateFunction(Environment env, DateTimeParseResult parseResult, String format) {
        try {
            Formatter formatter = new Formatter(new StringBuilder(), env.getLocale());
            return formatForDateFunction(env, parseResult, format, formatter).toString();
        } catch (IOException e) {
            // never happen, we use StringBuilder
            throw new UncheckedIOException(e);
        }
    }

    public static ZonedDateTime createFromFormat(String format, String date) {
        return createFromFormat(format, date, ZonedDateTime.now());
    }

    public static ZonedDateTime createFromFormat(String format, String date, ZonedDateTime dateTime) {
        return DateFormatParser.fromFormat(format, date, dateTime);
    }

    public static DateTimeParseResult createParseResultFromFormat(String format, String date, ZonedDateTime dateTime) {
        return DateFormatParser.fromFormatToResult(format, date, dateTime);
    }

    private static class DateFormatParser {
        static final Pattern DAY_d = Pattern.compile("3[01]|[0-2]?[0-9]");
        static final Pattern DAY_OF_YEAR = Pattern.compile("[0-9]{1,3}");
        static final ZoneId UTC_OFFSET = ZoneId.of("+00:00");
        private static final Pattern DIGITS = Pattern.compile("[+-]?\\d+");
        private static final Pattern HOUR_H = Pattern.compile("2[0-4]|[01]?[0-9]");
        private final Set<TemporalField> mods = new HashSet<>();
        private final String format;
        private final String date;
        private final ZonedDateTime base;
        ZonedDateTime dateTime;

        public DateFormatParser(String format, String date, ZonedDateTime base) {
            this.format = format;
            this.date = date;
            this.base = base;
            dateTime = base;
        }

        private static ZonedDateTime fromFormat(String format, String date, ZonedDateTime base) {
            return new DateFormatParser(format, date, base).parse();
        }

        private static DateTimeParseResult fromFormatToResult(String format, String date, ZonedDateTime base) {
            return new DateFormatParser(format, date, base).parseResult();
        }

        private static String exhaust(Scanner scanner) {
            // return fast
            if (!scanner.hasNextLine()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }

            return sb.toString();
        }

        private static int position(String str, Scanner scanner) {
            // return fast
            if (!scanner.hasNextLine()) {
                return str.length();
            }

            int res = 0;
            while (scanner.hasNextLine()) {
                res += scanner.nextLine().length();
            }

            return str.length() - res;
        }

        private static long findLong(Scanner scanner, Pattern pattern) {
            return Long.parseLong(scanner.findWithinHorizon(pattern, 0));
        }

        private DateTimeParseResult parseResult() {
            return new DateTimeParseResult(parse(), mods, null, null);
        }

        private ZonedDateTime parse() {
            DateTimeMessageContainer.clear();

            Scanner scanner = new Scanner(date).useDelimiter("");
            // TODO: use more lightweight structure
            Queue<TemporalAdjuster> delayed = new ArrayDeque<>(8);

            String f = this.format;
            loop:
            for (int i = 0, n = f.length(); i < n; i++) {
                char c = f.charAt(i);

                switch (c) {
                    // Day
                    case 'd':
                    case 'j': {
                        String withinHorizon = scanner.findWithinHorizon(DAY_d, 0);
                        int day = Integer.parseInt(withinHorizon);
                        int lastDay = Adjusters.lastDayOfMonth(dateTime);

                        dateTime = day > lastDay ?
                                dateTime.withDayOfMonth(lastDay).plusDays(day - lastDay) :
                                dateTime.withDayOfMonth(day);

                        mods.add(DAY_OF_MONTH);
                        break;
                    }
                    case 'D':
                    case 'l': {
                        String dow = scanner.findWithinHorizon(DAY_NAME, 0);

                        TemporalAdjuster adjuster = Adjusters.compose(nextOrSameDayOfWeek(dow), temporal -> {
                            mods.add(DAY_OF_WEEK);
                            return temporal;
                        });
                        delayed.offer(adjuster);
                        break;
                    }
                    case 'S': {
                        scanner.skip(ORDINAL_SUFFIX);
                        break;
                    }
                    case 'z': {
                        int doy = Integer.parseInt(scanner.findWithinHorizon(DAY_OF_YEAR, 0));

                        if (doy < 0 || doy > 365) {
                            throw new DateTimeParseException("Invalid value for z modifier day of year", date, i);
                        }

                        ++doy;
                        ZonedDateTime result = dateTime.withDayOfYear(doy).with(truncateIfNotModified(HOUR_OF_DAY, DAYS));
                        mods.add(ChronoField.DAY_OF_YEAR);
                        mods.add(ChronoField.MONTH_OF_YEAR);

                        dateTime = result;
                        break;
                    }
                    // Month
                    case 'F':
                    case 'M': {
                        String month = scanner.findWithinHorizon(MONTH, 0);
                        dateTime = dateTime.with(Adjusters.month(month));
                        mods.add(MONTH_OF_YEAR);
                        break;
                    }
                    case 'm':
                    case 'n': {
                        int month = findInt(scanner, MONTH_mm);
                        dateTime = dateTime.withMonth(month);
                        mods.add(MONTH_OF_YEAR);
                        break;
                    }
                    // Year
                    case 'Y': {
                        int year = findInt(scanner, YEAR_Y);
                        dateTime = dateTime.with(Adjusters.year(year));
                        mods.add(YEAR);
                        break;
                    }
                    case 'y': {
                        int year = findInt(scanner, YEAR_yy);
                        dateTime = dateTime.with(Adjusters.year(year));
                        mods.add(YEAR);
                        break;
                    }
                    // Time
                    case 'a':
                    case 'A': {
                        if (!mods.contains(HOUR_OF_DAY)) {
                            DateTimeMessageContainer.addError("Meridian can only come after an hour has been found", 0);
                        }

                        String next = scanner.findWithinHorizon(MERIDIAN, 0);
                        if (next == null) {
                            scanner.skip(".");
                            DateTimeMessageContainer.addError("A meridian could not be found", position(date, scanner));
                            break;
                        }

                        dateTime = dateTime.with(Adjusters.meridian(next.equals("am")));
                        break;
                    }
                    case 'g':
                    case 'h': {
                        int hour = findInt(scanner, DateTimeTokenizer.HOUR_hh);
                        dateTime = dateTime.withHour(hour).with(truncateIfNotModified(SECOND_OF_MINUTE, HOURS));

                        mods.add(HOUR_OF_DAY);
                        break;
                    }
                    case 'G':
                    case 'H': {
                        int hour = findInt(scanner, HOUR_H);
                        dateTime = dateTime.withHour(hour).with(truncateIfNotModified(MINUTE_OF_HOUR, HOURS));

                        mods.add(HOUR_OF_DAY);
                        break;
                    }
                    case 'i': {
                        dateTime = dateTime.withMinute(findInt(scanner, MINUTE_II))
                                .with(truncateIfNotModified(SECOND_OF_MINUTE, MINUTES));
                        mods.add(MINUTE_OF_HOUR);
                        break;
                    }
                    case 's': {
                        dateTime = dateTime.withSecond(findInt(scanner, SECOND_SS, "A two digit second could not be found"));
                        mods.add(SECOND_OF_MINUTE);
                        break;
                    }
                    case 'U': {
                        dateTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(findLong(scanner, DIGITS)), UTC_OFFSET);
                        break;
                    }
                    case 'u': {
                        String micros = scanner.findWithinHorizon(MICROSECONDS, 0);
                        if (micros == null) {
                            DateTimeMessageContainer.addError("Data missing", position(date, scanner));
                            throw new IllegalArgumentException("Data missing");
                        }
                        int length = micros.length();

                        if (length < 6) {
                            //padding from right
                            micros = String.format("%-6s", micros).replace(' ', '0');
                        }

                        if (length > 6) {
                            micros = micros.substring(0, 6);
                            DateTimeMessageContainer.addError("Trailing data", position(date, scanner));
                        }

                        int microInt = Integer.parseInt(micros);
                        dateTime = dateTime.with(ChronoField.MICRO_OF_SECOND, microInt);

                        mods.add(ChronoField.MICRO_OF_SECOND);
                        break;
                    }
                    // Timezone
                    case 'e':
                    case 'O':
                    case 'P':
                    case 'T': {
                        String timezone = scanner.useDelimiter(" ").next();
                        scanner.useDelimiter("");
                        ZoneId zoneId = ZoneIdFactory.of(timezone);
                        dateTime = dateTime.withZoneSameLocal(zoneId);
                        break;
                    }
                    //Escape
                    case '\\': {
                        if (i + 1 < n) {
                            skip(scanner, String.valueOf(f.charAt(++i)));
                        }
                        break;
                    }
                    // Whitespace and Separators
                    case '#':
                        scanner.skip("[ ,:;/.\\-]");
                        break;
                    case '[': { // expecting timezone modifier
                        scanner.skip("\\[");
                        switch (f.charAt(++i)) {
                            case 'e':
                            case 'O':
                            case 'P':
                            case 'T': {
                                scanner.useDelimiter("]");
                                String timezone = scanner.next();
                                ZoneId zoneId = ZoneIdFactory.of(timezone);
                                dateTime = dateTime.withZoneSameLocal(zoneId);
                                skip(scanner, "]");

                                ++i; // skipping the ']' character.
                                break;
                            }
                            default:
                                throw new IllegalArgumentException("After character '[' should be a e, O, P or T modifier, but got: " + c);
                        }
                        break;
                    }
                    case ';':
                        skip(scanner, ";");
                        break;
                    case ':':
                        skip(scanner, ":");
                        break;
                    case '/':
                        skip(scanner, "/");
                        break;
                    case '-':
                        skip(scanner, "-");
                        break;
                    case ',':
                        skip(scanner, ",");
                        break;
                    case '.':
                        skip(scanner, "\\.");
                        break;
                    case '\t':
                        skip(scanner, "\\t");
                        break;
                    case ' ':
                        skip(scanner, " ");
                        break;
                    case '?':
                        skip(scanner, ".|\\s");
                        break;
                    case '*':
                        skip(scanner, "[^ ,:;/.\\-]+");
                        break;
                    case '+': {
                        String trailingData = exhaust(scanner);
                        if (!trailingData.isEmpty()) {
                            DateTimeMessageContainer.addWarning("Trailing data", date.length() - trailingData.length());
                        }
                        break;
                    }
                    case '|': {
                        if (!mods.contains(YEAR)) {
                            dateTime = dateTime.withYear(1970);
                        }
                        if (!mods.contains(MONTH_OF_YEAR)) {
                            dateTime = dateTime.withMonth(1);
                        }
                        if (!mods.contains(DAY_OF_MONTH) && !mods.contains(ChronoField.DAY_OF_YEAR)) {
                            dateTime = dateTime.withDayOfMonth(1);
                        }
                        if (!mods.contains(HOUR_OF_DAY)) {
                            dateTime = dateTime.withHour(0);
                        }
                        if (!mods.contains(MINUTE_OF_HOUR)) {
                            dateTime = dateTime.withMinute(0);
                        }
                        if (!mods.contains(SECOND_OF_MINUTE)) {
                            dateTime = dateTime.withSecond(0);
                        }
                        if (!mods.contains(MICRO_OF_SECOND)) {
                            dateTime = dateTime.with(MICRO_OF_SECOND, 0);
                        }
                        break;
                    }
                    case '!': {
                        dateTime = ZonedDateTime.of(LocalDate.of(1970, 1, 1), LocalTime.MIDNIGHT, ZoneId.of("UTC"));
                        break;
                    }
                    default: {
                        break loop;
                    }
                }
            }

            String trailingData = exhaust(scanner);
            if (!trailingData.isEmpty()) {
                DateTimeMessageContainer.addError("Trailing data", date.length() - trailingData.length());
                throw new IllegalArgumentException("Trailing data");
            }

            while (!delayed.isEmpty()) {
                dateTime = dateTime.with(delayed.poll());
            }

            return dateTime;
        }

        private void skip(Scanner scanner, String pattern) {
            try {
                scanner.skip(pattern);
            } catch (NoSuchElementException e) {
                DateTimeMessageContainer.addError("Data missing", position(date, scanner));
                throw e;
            }
        }

        private int findInt(Scanner scanner, Pattern pattern, String message) {
            try {
                return Integer.parseInt(scanner.findWithinHorizon(pattern, 0));
            } catch (NumberFormatException e) {
                DateTimeMessageContainer.addError(message, position(date, scanner));
                throw e;
            }
        }

        private int findInt(Scanner scanner, Pattern pattern) {
            if (!scanner.hasNext()) {
                DateTimeMessageContainer.addError("Data missing", position(date, scanner));
                throw new IllegalArgumentException("Data missing");
            }

            return Integer.parseInt(scanner.findWithinHorizon(pattern, 0));
        }

        private boolean isModified(ChronoField field) {
            return mods.contains(field);
        }

        private TemporalAdjuster truncateIfNotModified(ChronoField field, TemporalUnit to) {
            return temporal -> {
                if (isModified(field)) {
                    return temporal;
                }

                return ((ZonedDateTime) temporal).truncatedTo(to);
            };
        }
    }
}
