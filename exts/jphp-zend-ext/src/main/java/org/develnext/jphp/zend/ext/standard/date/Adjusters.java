package org.develnext.jphp.zend.ext.standard.date;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MICRO_OF_SECOND;
import static java.time.temporal.ChronoField.MILLI_OF_SECOND;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MICROS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.previousOrSame;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.Year;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;

import php.runtime.common.Pair;

class Adjusters {
    private Adjusters() {
    }

    public static TemporalAdjuster year(long year) {
        if (year >= 0 && year <= 69) {
            year += 2000;
        } else if (year >= 70 && year <= 100) {
            year += 1900;
        } else if (year < 0) {
            year = 1970 + year;
        }

        final long y = year;

        return temporal -> temporal.with(YEAR, y);
    }

    public static TemporalAdjuster nextOrSameDayOfWeek(int dow) {
        return TemporalAdjusters.nextOrSame(DayOfWeek.of(dow));
    }

    public static TemporalAdjuster nextDayOfWeek(int dow) {
        return TemporalAdjusters.nextOrSame(DayOfWeek.of(dow));
    }

    public static TemporalAdjuster nthDayOfWeek(String dow, String ordinal) {
        return nthDayOfWeek(dayOfWeek(dow), ordinalToNumber(ordinal));
    }

    public static TemporalAdjuster nthDayOfWeek(DayOfWeek dow, long n) {
        if (n < 1)
            throw new IllegalArgumentException("The n should be positive number.");

        int dowValue = dow.getValue();
        return (temporal) -> {
            int calDow = temporal.get(DAY_OF_WEEK);
            int daysDiff = calDow - dowValue;
            return temporal.plus((daysDiff >= 0 ? 7 - daysDiff : -daysDiff) + (n - 1) * 7, DAYS);
        };
    }

    public static TemporalAdjuster relativeDayOfWeek(String relText, String dow) {
        TemporalAdjuster adjuster;

        switch (relText.toLowerCase()) {
            case "this":
                adjuster = previousOrSame(DayOfWeek.MONDAY);
                break;
            case "last":
            case "previous":
                adjuster = compose(previous(DayOfWeek.MONDAY), previous(DayOfWeek.MONDAY));
                break;
            case "next":
                adjuster = next(DayOfWeek.MONDAY);
                break;
            default:
                throw new IllegalArgumentException("Unknown relative text: " + relText);
        }

        return temporal -> temporal.with(adjuster);
    }

    public static TemporalAdjuster relativeDayOfWeek(String relText, DayOfWeek dow) {
        TemporalAdjuster adjuster;

        switch (relText.toLowerCase()) {
            case "this":
                adjuster = previousOrSame(dow);
                break;
            case "last":
            case "previous":
                adjuster = compose(previous(dow), previous(dow));
                break;
            case "next":
                adjuster = next(dow);
                break;
            default:
                throw new IllegalArgumentException("Unknown relative text: " + relText);
        }

        return temporal -> temporal.with(adjuster);
    }

    public static TemporalAdjuster nextOrSameDayOfWeek(String dow) {
        return TemporalAdjusters.nextOrSame(dayOfWeek(dow));
    }

    public static TemporalAdjuster previousDayOfWeek(String dow) {
        return previous(dayOfWeek(dow));
    }

    public static TemporalAdjuster dayOfWeekInMonth(String ordinal, String dow) {
        return TemporalAdjusters.dayOfWeekInMonth(ordinalToNumber(ordinal), dayOfWeek(dow));
    }

    public static TemporalAdjuster month(String month) {
        return temporal -> temporal.with(ChronoField.MONTH_OF_YEAR, monthNameToNumber(month));
    }

    public static Pair<TemporalAdjuster, TemporalField> relativeUnit(DateTimeParserContext ctx, String unit,
                                                                     String ordinal) {
        return relativeUnit(ctx, unit, ordinalToNumber(ordinal));
    }

    public static Pair<TemporalAdjuster, TemporalField> relativeUnit(DateTimeParserContext ctx, String unit,
                                                                     final long value) {
        ChronoUnit chronoUnit;
        TemporalField field;

        switch (unit.toLowerCase()) {
            case "year":
            case "years":
                return Pair.of(temporal -> {
                    try {
                        Temporal ret = temporal.plus(value, YEARS);
                        ctx.addRelativeContributor(unit, value);
                        return ret;
                    } catch (DateTimeException e) {
                        ValueRange range = YEAR.range();
                        long validYear = value < range.getMinimum() ? range.getMinimum() : range.getMaximum();
                        return temporal.with(YEAR, validYear);
                    }
                }, YEAR);
            case "month":
            case "months":
                chronoUnit = MONTHS;
                field = MONTH_OF_YEAR;
                break;
            case "day":
            case "days":
                chronoUnit = DAYS;
                field = DAY_OF_MONTH;
                break;
            case "hour":
            case "hours":
                chronoUnit = HOURS;
                field = HOUR_OF_DAY;
                return Pair.of(temporal -> {
                    ctx.addRelativeContributor("hour", value);
                    if (!ctx.isModifiedTimezone() && temporal instanceof ZonedDateTime) {
                        ZonedDateTime dateTime = (ZonedDateTime) temporal;

                        return dateTime.toLocalDateTime().plusHours(value).atZone(dateTime.getZone());
                    }

                    return temporal.plus(value, chronoUnit);
                }, field);
            case "minute":
            case "minutes":
            case "min":
            case "mins":
                chronoUnit = MINUTES;
                field = MINUTE_OF_HOUR;
                break;
            case "second":
            case "seconds":
            case "sec":
            case "secs":
                chronoUnit = SECONDS;
                field = SECOND_OF_MINUTE;
                break;
            case "millisecond":
            case "milliseconds":
            case "msecs":
            case "msec":
            case "ms":
                chronoUnit = MILLIS;
                field = MILLI_OF_SECOND;
                break;
            case "microsecond":
            case "microseconds":
            case "usec":
            case "usecs":
            case "µs":
            case "µsec":
            case "µsecs":
                chronoUnit = MICROS;
                field = MICRO_OF_SECOND;
                break;
            case "week":
            case "weeks":
                chronoUnit = WEEKS;
                field = DAY_OF_MONTH;
                break;
            case "fortnight":
            case "forthnight":
            case "fortnights":
            case "forthnights":
                return Pair.of(temporal -> {
                    long amountToAdd = value * 14L;
                    ctx.addRelativeContributor("day", amountToAdd);
                    return temporal.plus(amountToAdd, DAYS);
                }, DAY_OF_MONTH);
            case "weekday":
            case "weekdays":
                return Pair.of(plusBusinessDays(value), DAY_OF_MONTH);
            default:
                throw new IllegalArgumentException("Unknown unit: " + unit);
        }

        return Pair.of(temporal -> {
            String chronoUnitAsString = chronoUnit.toString();
            String u = chronoUnitAsString.toLowerCase().substring(0, chronoUnitAsString.length() - 1);

            ctx.addRelativeContributor(u, value);
            return temporal.plus(value, chronoUnit);
        }, field);
    }

    private static TemporalAdjuster plusBusinessDays(long days) {
        return temporal -> {

            temporal = temporal.plus(getAllDays(temporal.get(DAY_OF_WEEK), days) * Long.signum(days), DAYS);
            DayOfWeek dayOfWeek = DayOfWeek.of(temporal.get(DAY_OF_WEEK));

            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                temporal = temporal.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }

            return temporal;
        };
    }

    static TemporalAdjuster meridian(boolean am) {
        return temporal -> am ?
                temporal.with(HOUR_OF_DAY, temporal.get(HOUR_OF_DAY) % 12) :
                temporal.with(HOUR_OF_DAY, temporal.get(HOUR_OF_DAY) + 12);
    }

    static TemporalAdjuster compose(TemporalAdjuster first, TemporalAdjuster second) {
        return temporal -> temporal.with(first).with(second);
    }

    /**
     * https://stackoverflow.com/questions/33942544/how-to-skip-weekends-while-adding-days-to-localdate-in-java-8/33943576
     */
    private static long getAllDays(int dayOfWeek, long businessDays) {
        long result = 0;
        if (businessDays != 0) {
            boolean isStartOnWorkday = dayOfWeek < 6;
            long absBusinessDays = Math.abs(businessDays);

            if (isStartOnWorkday) {
                // if negative businessDays: count backwards by shifting weekday
                int shiftedWorkday = businessDays > 0 ? dayOfWeek : 6 - dayOfWeek;
                result = absBusinessDays + (absBusinessDays + shiftedWorkday - 1) / 5 * 2;
            } else { // start on weekend
                // if negative businessDays: count backwards by shifting weekday
                int shiftedWeekend = businessDays > 0 ? dayOfWeek : 13 - dayOfWeek;
                result = absBusinessDays + (absBusinessDays - 1) / 5 * 2 + (7 - shiftedWeekend);
            }
        }
        return result;
    }

    static DayOfWeek dayOfWeek(String dow) {
        return DayOfWeek.of(weekDayValue(dow));
    }

    static int lastDayOfMonth(ZonedDateTime dateTime) {
        return dateTime.getMonth().length(Year.isLeap(dateTime.getYear()));
    }

    private static int weekDayValue(String weekDay) {
        weekDay = weekDay.toLowerCase();
        switch (weekDay) {
            case "mon":
            case "monday":
                return 1;
            case "tue":
            case "tuesday":
                return 2;
            case "wed":
            case "wednesday":
                return 3;
            case "thu":
            case "thursday":
                return 4;
            case "fri":
            case "friday":
                return 5;
            case "sat":
            case "saturday":
                return 6;
            case "sun":
            case "sunday":
                return 7;
            default:
                throw new IllegalArgumentException("Unknown weekday:" + weekDay);
        }
    }

    private static int monthNameToNumber(String month) {
        month = month.toLowerCase();

        switch (month) {
            case "january":
            case "jan":
            case "i":
                return 1;
            case "february":
            case "feb":
            case "ii":
                return 2;
            case "march":
            case "mar":
            case "iii":
                return 3;
            case "april":
            case "apr":
            case "iv":
                return 4;
            case "may":
            case "v":
                return 5;
            case "june":
            case "jun":
            case "vi":
                return 6;
            case "july":
            case "jul":
            case "vii":
                return 7;
            case "august":
            case "aug":
            case "viii":
                return 8;
            case "september":
            case "sep":
            case "ix":
                return 9;
            case "october":
            case "oct":
            case "x":
                return 10;
            case "november":
            case "nov":
            case "xi":
                return 11;
            case "december":
            case "dec":
            case "xii":
                return 12;
            default:
                throw new IllegalArgumentException("Not a month: " + month);
        }
    }

    private static int ordinalToNumber(String ordinal) {
        ordinal = ordinal.toLowerCase();

        switch (ordinal) {
            case "first":
            case "next":
                return 1;
            case "second":
                return 2;
            case "third":
                return 3;
            case "fourth":
                return 4;
            case "fifth":
                return 5;
            case "sixth":
                return 6;
            case "seventh":
                return 7;
            case "eighth":
                return 8;
            case "ninth":
                return 9;
            case "tenth":
                return 10;
            case "eleventh":
                return 11;
            case "twelfth":
                return 12;
            case "last":
            case "previous":
                return -1;
            case "this":
                return 0;
            default:
                throw new IllegalArgumentException("Unknown ordinal: " + ordinal);
        }
    }
}
