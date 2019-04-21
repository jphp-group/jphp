package org.develnext.jphp.zend.ext.standard.date;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.SECOND_OF_MINUTE;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.DayOfWeek;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;

import php.runtime.common.Pair;

class Adjusters {
    private Adjusters() {
    }

    public static TemporalAdjuster nthDayOfWeek(String dow, String ordinal) {
        int n = ordinalToNumber(ordinal);

        if (n < 1)
            throw new IllegalArgumentException("The n should be positive number.");

        int dowValue = weekDayValue(dow);
        return (temporal) -> {
            int calDow = temporal.get(DAY_OF_WEEK);
            int daysDiff = calDow - dowValue;
            return temporal.plus((daysDiff >= 0 ? 7 - daysDiff : -daysDiff) + (n - 1) * 7, DAYS);
        };
    }

    public static TemporalAdjuster nextOrSameDayOfWeek(String dow) {
        return TemporalAdjusters.nextOrSame(dayOfWeek(dow));
    }

    public static TemporalAdjuster previousDayOfWeek(String dow) {
        return TemporalAdjusters.previous(dayOfWeek(dow));
    }

    public static TemporalAdjuster dayOfWeekInMonth(String ordinal, String dow) {
        return TemporalAdjusters.dayOfWeekInMonth(ordinalToNumber(ordinal), dayOfWeek(dow));
    }

    public static TemporalAdjuster month(String month) {
        return temporal -> temporal.with(ChronoField.MONTH_OF_YEAR, monthNameToNumber(month));
    }

    public static Pair<TemporalAdjuster, TemporalField> relativeUnit(String unit, String ordinal) {
        return relativeUnit(unit, ordinalToNumber(ordinal));
    }

    public static Pair<TemporalAdjuster, TemporalField> relativeUnit(String unit, long value) {
        ChronoUnit chronoUnit;
        TemporalField field;

        switch (unit.toLowerCase()) {
            case "year":
            case "years":
                chronoUnit = YEARS;
                field = YEAR;
                break;
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
                break;
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
            case "week":
            case "weeks":
                chronoUnit = WEEKS;
                field = DAY_OF_MONTH;
                break;
            case "fortnight":
            case "forthnight":
            case "fortnights":
            case "forthnights":
                value *= 14L;
                chronoUnit = DAYS;
                field = DAY_OF_MONTH;
                break;
            case "weekday":
            case "weekdays":
                return Pair.of(plusBusinessDays(value), DAY_OF_MONTH);
            default:
                throw new IllegalArgumentException("Unknown unit: " + unit);
        }

        final long val = value;

        return Pair.of(temporal -> temporal.plus(val, chronoUnit), field);
    }

    public static TemporalAdjuster plusBusinessDays(long days) {
        return temporal -> temporal.plus(getAllDays(temporal.get(DAY_OF_WEEK), days) * Long.signum(days), DAYS);
    }

    public static TemporalAdjuster meridian(boolean am) {
        return temporal -> am ?
                temporal.with(HOUR_OF_DAY, temporal.get(HOUR_OF_DAY) % 12) :
                temporal.with(HOUR_OF_DAY, temporal.get(HOUR_OF_DAY) + 12);
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

    private static DayOfWeek dayOfWeek(String dow) {
        return DayOfWeek.of(weekDayValue(dow));
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
