package org.develnext.jphp.zend.ext.standard.date;

import static java.time.ZonedDateTime.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previous;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.api.SoftAssertions;
import org.junit.Ignore;
import org.junit.Test;

import php.runtime.common.Pair;

public class DateTimeParserTest {
    private static final Map<DayOfWeek, List<String>> DAY_OF_WEEKS = Arrays.stream(DayOfWeek.values())
            .collect(Collectors.toMap(o -> o, dow -> Arrays.asList(
                    dow.getDisplayName(TextStyle.FULL, Locale.US),
                    dow.getDisplayName(TextStyle.FULL, Locale.US).toLowerCase(),
                    dow.getDisplayName(TextStyle.FULL, Locale.US).toUpperCase(),
                    dow.getDisplayName(TextStyle.SHORT, Locale.US),
                    dow.getDisplayName(TextStyle.SHORT, Locale.US).toLowerCase(),
                    dow.getDisplayName(TextStyle.SHORT, Locale.US).toUpperCase()
            )));

    private static ZonedDateTime parse(String input, ZonedDateTime dateTime) {
        return parseWithMicro(input, dateTime).withNano(0);
    }

    private static LocalDateTime parseLocal(String input) {
        return parseWithMicro(input).toLocalDateTime();
    }

    private static LocalTime parseTime(String input) {
        return parse(input, ZonedDateTime.now()).toLocalTime();
    }

    private static ZonedDateTime parse(String input, String zoneId) {
        return parseWithMicro(input, zoneId).withNano(0);
    }

    private static ZonedDateTime parseWithMicro(String input) {
        try {
            return new DateTimeParser(input).parse();
        } catch (DateTimeParserException e) {
            throw new RuntimeException(e);
        }
    }

    private static ZonedDateTime parseWithMicro(String input, String zoneId) {
        try {
            return new DateTimeParser(input, ZoneId.of(zoneId)).parse();
        } catch (DateTimeParserException e) {
            throw new RuntimeException(e);
        }
    }

    private static ZonedDateTime parseWithMicro(String input, ZonedDateTime base) {
        try {
            return new DateTimeParser(input, base).parse();
        } catch (DateTimeParserException e) {
            throw new RuntimeException(e);
        }
    }

    private static ZonedDateTime withTime(int hour, int minute, int second) {
        return now().withHour(hour).withMinute(minute).withSecond(second).withNano(0);
    }

    private static ZonedDateTime withTime(int hour, int minute, int second, String zone) {
        return now().withHour(hour).withMinute(minute).withSecond(second).withNano(0)
                .withZoneSameLocal(ZoneId.of(zone));
    }

    @Test
    public void soap() {
        // microseconds
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.300+08:00"), parseWithMicro("2008-07-01T22:35:17.3+08:00"));
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.030+08:00"), parseWithMicro("2008-07-01T22:35:17.03+08:00"));
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.0030+08:00"), parseWithMicro("2008-07-01T22:35:17.003+08:00"));

        // without timezone
        assertThat(parseLocal("2008-07-01T22:35:17.3")).isEqualTo("2008-07-01T22:35:17.300");
    }

    @Test
    public void unixtimestamp() {
        ZonedDateTime now = now();
        assertThat(parse("@1", now))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("1970-01-01T00:00:01Z[UTC]"));

        assertThat(parse("@-1", now))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("1969-12-31T23:59:59Z[UTC]"));

        assertThat(parse("@-1151515151515", now))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("-34521-12-09T08:08:05Z[UTC]"));
    }

    @Test
    //@Ignore // TODO, test is unstable https://travis-ci.org/jphp-group/jphp/jobs/654386727?utm_medium=notification&utm_source=github_status
    public void onlyTimeZone() {
        ZonedDateTime now = now();
        assertThat(parse("+1:2", now))
                .isEqualTo(parse("+1:02", now))
                .isEqualTo(parse("+01:02", now))
                .isEqualTo(parse("+01:2", now))
                .isEqualToIgnoringNanos(now.withZoneSameLocal(ZoneId.of("+01:02")));

        assertThat(parse("+0000 GMT", now))
                .isEqualToIgnoringNanos(now.withZoneSameLocal(ZoneId.of("Z")));

        for (String tz : Arrays.asList("+0", "+00", "+0000", "GMT")) {
            assertThat(parse(tz, now))
                    .isEqualToIgnoringNanos(now.withZoneSameLocal(ZoneId.of(tz)));
        }
    }

    @Test
    public void hourAndMinuteWithMeridian() {
        ZonedDateTime now = now();
        assertEquals(now.withHour(4).withMinute(8).withSecond(0).withNano(0), parse("4:08 am", now));
        assertEquals(now.withHour(19).withMinute(19).withSecond(0).withNano(0), parse("7:19P.M.", now));
    }

    @Test
    public void hourAndMinute() {
        ZonedDateTime now = now();
        assertThat(parse("t0222", now)).isEqualTo(now.withHour(2).withMinute(22).truncatedTo(MINUTES));
        assertThat(parse("t0222 t0222", now))
                .isEqualTo(now.withYear(222).withHour(2).withMinute(22).truncatedTo(MINUTES));
    }

    @Test
    public void postgresDayOfYear() {
        ZonedDateTime now = now();
        assertThat(parse("2006167", now))
                .isEqualTo(parse("2006.167", now))
                .isEqualTo(LocalDate.of(2006, 6, 16).atStartOfDay(ZoneId.systemDefault()));
    }

    @Test
    public void hourMinuteSecondWithMeridian() {
        ZonedDateTime now = now();
        assertEquals(now.withHour(4).withMinute(8).withSecond(37).withNano(0), parse("4:08:37 am", now));
        assertEquals(now.withHour(19).withMinute(19).withSecond(19).withNano(0), parse("7:19:19P.M.", now));
    }

    @Test
    public void mssqlTime() {
        ZonedDateTime now = now();
        assertEquals(now
                        .withHour(4)
                        .withMinute(8)
                        .withSecond(39)
                        .with(ChronoField.MICRO_OF_SECOND, 123130),
                parseWithMicro("4:08:39:12313am"));

        assertEquals(now
                        .withHour(4)
                        .withMinute(8)
                        .withSecond(39)
                        .with(ChronoField.MICRO_OF_SECOND, 123130),
                parseWithMicro("4:08:39.12313am"));

        assertEquals(now
                        .withHour(16)
                        .withMinute(8)
                        .withSecond(39)
                        .with(ChronoField.MICRO_OF_SECOND, 123130),
                parseWithMicro("4:08:39.12313pm"));
    }

    @Test
    public void hourMinuteNoColon() {
        ZonedDateTime now = now();
        assertEquals(withTime(4, 8, 0), parse("0408", now));
        assertEquals(withTime(4, 8, 0), parse("T0408", now));
        assertEquals(withTime(4, 8, 0), parse("t0408", now));
    }

    @Test
    public void hourMinuteSecondNoColon() {
        ZonedDateTime now = now();
        assertEquals(withTime(19, 19, 19), parse("T191919", now));
        assertEquals(withTime(19, 19, 19), parse("191919", now));
        assertEquals(withTime(4, 8, 37), parse("040837", now));
        assertEquals(withTime(4, 8, 37), parse("t040837", now));
        assertEquals(withTime(4, 8, 37), parse("T040837", now));
    }

    @Test
    public void hour24Notation() {
        ZonedDateTime now = now();
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT+0700")), parse("T19:19:19GMT+0700", now));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-0700")), parse("T19:19:19GMT-0700", now));

        assertEquals(withTime(4, 8, 0), parse("04:08", now));
        assertEquals(withTime(4, 8, 0), parse("t04:08", now));
        assertEquals(withTime(4, 8, 0), parse("T04:08", now));
        assertEquals(withTime(19, 19, 0), parse("19.19", now));
        assertEquals(withTime(23, 43, 0), parse("T23:43", now));
        assertEquals(withTime(23, 43, 0), parse("t23:43", now));

        assertEquals(withTime(4, 8, 59), parse("04:08:59", now));
        assertEquals(withTime(4, 8, 59), parse("t04:08:59", now));
        assertEquals(withTime(4, 8, 59), parse("T04:08:59", now));
        assertEquals(withTime(19, 19, 19), parse("T19.19.19", now));
        assertEquals(withTime(19, 19, 19), parse("t19.19.19", now));

        assertEquals(withTime(4, 8, 37).with(ChronoField.MICRO_OF_SECOND, 814120), parseWithMicro("04.08.37.81412"));
        assertEquals(withTime(19, 19, 19).with(ChronoField.MICRO_OF_SECOND, 532453), parseWithMicro("19:19:19.532453"));
    }

    @Test
    public void tz() {
        ZonedDateTime expected = withTime(19, 19, 19);

        ZonedDateTime now = now();
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19GMT-6", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT+06:00")), parse("T19:19:19GMT+6", now));
        assertEquals(withTime(4, 8, 37).withZoneSameLocal(ZoneId.of("+02:00")), parse("T040837CEST", now));
        assertEquals(withTime(4, 8, 37).withZoneSameLocal(ZoneId.of("+02:00")), parse("T040837 CEST", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("+02:00")), parse("T19:19:19CEST", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("+10:30")), parse("T19:19:19ACDT", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT+06:00")), parse("T19:19:19GMT+6", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19GMT-06", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:00")), parse("T19:19:19-6", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-0630", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-630", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-630", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-6:30", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:00")), parse("T19:19:19-6", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19 GMT-630", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19GMT-630", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19 -630", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19 -6:30", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19 GMT-6:30", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19 GMT-6", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19 GMT-06", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-07:00")), parse("T19:19:19 -07", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-07:00")), parse("T19:19:19 -07:00", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-0700")), parse("T19:19:19 -0700", now));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-0700")), parse("T19:19:19 GMT-0700", now));
    }

    @Test
    public void timezoneLong() {
        ZonedDateTime now = now();
        assertEquals(withTime(19, 19, 19, "Europe/Amsterdam"), parse("T19:19:19 Europe/Amsterdam", now));
        assertEquals(withTime(19, 19, 19, "America/Indiana/Knox"), parse("T19:19:19 America/Indiana/Knox", now));
        assertEquals(withTime(19, 19, 19, "Europe/Amsterdam"), parse("T19:19:19Europe/Amsterdam", now));
        assertEquals(withTime(19, 19, 19, "Europe/Amsterdam"), parse("191919Europe/Amsterdam", now));
    }

    @Test
    public void timezoneAlias() {
        ZonedDateTime now = now();
        Stream.of(new Pair<>("CEST", "+02:00"), new Pair<>("ACDT", "+10:30"))
                .forEach(pair -> {
                    ZoneId zoneId = ZoneId.of(pair.getB());
                    ZonedDateTime actual = parse("T19:19:19" + pair.getA(), now);
                    ZonedDateTime expected = withTime(19, 19, 19, pair.getB());

                    assertEquals(expected, actual);
                });
    }

    @Test
    public void americalDate() {
        ZonedDateTime now = now();
        assertThat(parse("5/12", now)).isEqualToIgnoringNanos(now.withMonth(5).withDayOfMonth(12));
        assertThat(parse("10/27", now)).isEqualToIgnoringNanos(now.withMonth(10).withDayOfMonth(27));

        assertThat(parse("12/22/78", now)).isEqualToIgnoringNanos(now.withMonth(12).withDayOfMonth(22).withYear(1978));
        assertThat(parse("1/17/2006", now)).isEqualToIgnoringNanos(now.withMonth(1).withDayOfMonth(17).withYear(2006));
        assertThat(parse("1/17/6", now)).isEqualToIgnoringNanos(now.withMonth(1).withDayOfMonth(17).withYear(2006));
    }

    @Test
    public void GNUDate() {
        ZonedDateTime now = now();
        assertThat(parse("2008-6", now)).isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(1).truncatedTo(DAYS));
        assertThat(parse("2008-06", now)).isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(1).truncatedTo(DAYS));
        assertThat(parse("1978-12", now)).isEqualToIgnoringNanos(now.withYear(1978).withMonth(12).withDayOfMonth(1).truncatedTo(DAYS));
    }

    @Test
    public void sandbox() {

    }

    @Test
    public void dateNotResetingTimeWhenTimeIsPresent() {
        ZonedDateTime now = now();
        assertThat(parse("17:00 2004-01-01", now))
                .isEqualTo(now.withYear(2004).withMonth(01).withDayOfMonth(01).withHour(17).truncatedTo(HOURS));
    }

    @Test
    public void ordinalAndNumberDifference() {
        ZonedDateTime now = now();
        assertThat(parse("1 Monday December 2008", now))
                .isEqualTo(now.withYear(2008).withMonth(12).withDayOfMonth(1).truncatedTo(DAYS));

        assertThat(parse("2 Monday December 2008", now))
                .isEqualTo(now.withYear(2008).withMonth(12).withDayOfMonth(8).truncatedTo(DAYS));
    }

    @Test
    public void timeShouldNotReset() {
        ZonedDateTime now = now();
        assertThat(parse("19:30 Dec 17 2005", now))
                .isEqualTo(parse("Dec 17 19:30 2005", now))
                .isEqualTo(now.withYear(2005).withMonth(12).withDayOfMonth(17).withHour(19).withMinute(30).truncatedTo(MINUTES));
    }

    @Test
    public void isoYearWithWeekAndWeekDay() {
        ZonedDateTime parse = parse("1997W011", now());
        assertThat(parse)
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(1996).withMonth(12).withDayOfMonth(30));
    }

    @Test
    @Ignore
    public void isoYearWithWeek() {
        assertThat(parse("2004W30", now()))
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(2004).withMonth(7).withDayOfMonth(19));
    }

    @Test
    public void isoYearWeekWeekDayAndTime() {
        ZonedDateTime parse = parse("2004W101T05:00+0", now());
        assertThat(parse)
                .isEqualToIgnoringNanos(withTime(5, 0, 0).withYear(2004).withMonth(3).withDayOfMonth(1)
                        .withZoneSameLocal(ZoneId.of("+0")));
    }

    @Test
    public void ordinalWithSuffix() {
        assertThat(parseLocal("26th Nov 2005 18:18"))
                .isEqualTo(parseLocal("Sat 26th Nov 2005 18:18"))
                .isEqualTo("2005-11-26T18:18:00");

        ZonedDateTime now = now();
        assertThat(parse("26th Nov", now)).isEqualTo(parse("26 Nov", now));
    }

    @Test
    public void timestampWithTimezone() {
        ZonedDateTime expected = ZonedDateTime.parse("2005-07-14T20:30:41Z").withZoneSameInstant(ZoneIdFactory.of("CEST"));
        ZonedDateTime actual = parse("@1121373041 CEST", now());

        assertThat(actual).isEqualToIgnoringNanos(expected);
    }

    @Test
    public void justYear() {
        ZonedDateTime now = now();
        Stream.of("1978", "1979", "1980")
                .forEach(yearStr -> {
                    assertThat(parse(yearStr, now)).isEqualToIgnoringNanos(now.withYear(Integer.parseInt(yearStr)));
                });
    }

    @Test
    public void justMonth() {
        Locale l = Locale.US;
        for (Month month : Month.values()) {
            for (TextStyle value : Arrays.asList(TextStyle.FULL, TextStyle.SHORT)) {
                String monthName = month.getDisplayName(value, l);
                ZonedDateTime now = now();
                ZonedDateTime expected = now.withMonth(month.getValue()).truncatedTo(DAYS);

                assertThat(parse(monthName, now)).isEqualToIgnoringSeconds(expected);
                assertThat(parse(monthName.toLowerCase(), now)).isEqualToIgnoringSeconds(expected);
                assertThat(parse(monthName.toUpperCase(), now)).isEqualToIgnoringSeconds(expected);
            }
        }
    }

    @Test
    public void hourWithMeridian() {
        ZonedDateTime now = now();
        Stream.of("5P.M.", "5 pm", "5 p.m", "5 p.m.", "5 P.m.", "5 P.M.", "5 p.M.", "5 p.M")
                .map(s -> {
                    return parse(s, now);
                })
                .forEach(dateTime -> {
                    assertThat(dateTime).isEqualToIgnoringNanos(withTime(17, 0, 0));
                });
    }

    @Test
    public void yearAndTextualMonth() {
        ZonedDateTime now = now();
        Stream.of("2008 June", "2008-VI", "2008.VI", "2008 VI", "2008 june", "2008 jUnE", "2008.jUnE")
                .map(s -> parse(s, now))
                .forEach(parsed -> {
                    assertThat(parsed)
                            .isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(1).truncatedTo(DAYS));
                });
    }

    @Test
    public void customDates() {
        ZonedDateTime now = now();
        assertThat(parse("March 1879", now))
                .isEqualToIgnoringNanos(now.withYear(1879).withMonth(3).withDayOfMonth(1).truncatedTo(DAYS));

        Stream.of("78-Dec-22", "1978-Dec-22", "1978-DEC-22", "78-DEC-22", "78-DEc-22")
                .map(s -> parse(s, now))
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(1978, 12, 22), LocalTime.MIDNIGHT, ZoneId.systemDefault());
                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        Stream.of("DEC-1978", "DEC1978", "DEC 1978", "DEC- 1978", "DEC - 1978", "Dec ----  --- -- 1978",
                "dec\t- \t\t-- --\t 1978", "dec.-\t1978", "dec.......-\t1978", "dec . . .....--- . \t1978")
                .map(s -> parse(s, now))
                .forEach(parsed -> {
                    assertThat(parsed)
                            .isEqualToIgnoringNanos(now.withYear(1978).withMonth(12).withDayOfMonth(1).truncatedTo(DAYS));
                });

        Stream.of("July 1st, 2008", "July 1st , 2008", "July 1, 2008", "July 1,2008", "July 1,08",
                "July.1,2008", "July.1rd ,2008")
                .map(s -> parse(s, now))
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(2008, 7, 1), LocalTime.MIDNIGHT, ZoneId.systemDefault());
                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        Stream.of("July 1st,", "July 1st ,", "July 1", "July.1", "July....1", "July  ...1", "July... .. 1,,,.., ", "1 July", "1.July", "1.JulY")
                .map(s -> parse(s, now))
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(Year.now().getValue(), 7, 1), LocalTime.MIDNIGHT, ZoneId.systemDefault());

                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        Stream.of("May-09-78", "May-09-1978", "may-09-1978", "mAY-09-78")
                .map(s -> parse(s, now))
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(1978, 5, 9), LocalTime.MIDNIGHT, ZoneId.systemDefault());

                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        assertThat(parse("DEC1978", now))
                .isEqualToIgnoringNanos(now.withYear(1978).withMonth(12).withDayOfMonth(1).truncatedTo(DAYS));

        assertThat(parse("June 2008", now))
                .isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(1).truncatedTo(DAYS));

        assertThat(parse("14 III 1879", now))
                .isEqualToIgnoringNanos(now.withYear(1879).withMonth(3).withDayOfMonth(14).truncatedTo(DAYS));

        assertThat(parse("22DEC78", now))
                .isEqualToIgnoringNanos(now.withYear(1978).withMonth(12).withDayOfMonth(22).truncatedTo(DAYS));

        assertThat(parse("28-June 2008", now))
                .isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(28).truncatedTo(DAYS));

        assertThat(parse("22\t12.78", now))
                .isEqualToIgnoringNanos(now.withYear(1978).withMonth(12).withDayOfMonth(22).truncatedTo(DAYS));

        assertThat(parse("28.6.08", now))
                .isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(28).truncatedTo(DAYS));

        assertThat(parse("22.12.1978", now))
                .isEqualToIgnoringNanos(now.withYear(1978).withMonth(12).withDayOfMonth(22).truncatedTo(DAYS));

        assertThat(parse("28-6-2008", now))
                .isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(28).truncatedTo(DAYS));

        assertThat(parse("8-6-21", now))
                .isEqualToIgnoringNanos(now.withYear(2008).withMonth(6).withDayOfMonth(21).truncatedTo(DAYS));

        assertThat(parse("2005-07-18 22:10:00 +0400", now))
                .isEqualToIgnoringNanos(now.withYear(2005).withMonth(7).withDayOfMonth(18)
                        .withHour(22).withMinute(10).withSecond(00)
                        .withZoneSameLocal(ZoneId.of("+0400")));

        assertThat(parse("2005-07-14 22:30:41 GMT", "Europe/London"))
                .isEqualToIgnoringNanos(now.withYear(2005).withMonth(7).withDayOfMonth(14)
                        .withHour(22).withMinute(30).withSecond(41)
                        .withZoneSameLocal(ZoneId.of("GMT"))
                );

        assertThat(parse("2005-07-14 22:30:41", now))
                .isEqualToIgnoringNanos(now.withYear(2005).withMonth(7).withDayOfMonth(14)
                        .withHour(22).withMinute(30).withSecond(41));

        assertThat(parse("1999-10-13", now))
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(1999).withMonth(10).withDayOfMonth(13));

        assertThat(parse("Oct 13  1999", now))
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(1999).withMonth(10).withDayOfMonth(13));
    }

    @Test
    public void iso8DigitYearDayMonth() {
        ZonedDateTime dateTime = withTime(0, 0, 0);
        ZonedDateTime now = now();
        assertThat(parse("15810726", now))
                .isEqualToIgnoringNanos(dateTime.withYear(1581).withMonth(07).withDayOfMonth(26));

        assertThat(parse("19780417", now))
                .isEqualToIgnoringNanos(dateTime.withYear(1978).withMonth(04).withDayOfMonth(17));

        assertThat(parse("18140517", now))
                .isEqualToIgnoringNanos(dateTime.withYear(1814).withMonth(05).withDayOfMonth(17));
    }

    @Test
    public void isoYearDayMonthWithSlashes() {
        ZonedDateTime dateTime = withTime(0, 0, 0);
        ZonedDateTime now = now();
        assertThat(parse("2008/06/30", now))
                .isEqualToIgnoringNanos(dateTime.withYear(2008).withMonth(06).withDayOfMonth(30));

        assertThat(parse("1978/12/22", now))
                .isEqualToIgnoringNanos(dateTime.withYear(1978).withMonth(12).withDayOfMonth(22));
    }

    @Test
    public void isoYearDayMonthWithDashes() {
        ZonedDateTime dateTime = withTime(0, 0, 0);
        ZonedDateTime now = now();
        assertThat(parse("08-06-30", now))
                .isEqualToIgnoringNanos(dateTime.withYear(2008).withMonth(06).withDayOfMonth(30));

        assertThat(parse("78-12-22", now))
                .isEqualToIgnoringNanos(dateTime.withYear(1978).withMonth(12).withDayOfMonth(22));
    }

    @Test
    public void relativeOffsets() {
        ZonedDateTime now = now();
        Stream.of(
                Pair.of("150year 150years", 300),
                Pair.of("+1 year", 1),
                Pair.of("+ 1 year", 1),
                Pair.of("+5 year", 5),
                Pair.of("5 year", 5),
                Pair.of("-5 year", -5),
                Pair.of("-300 years", -300),
                Pair.of("300 years", 300),
                Pair.of("+300 years", 300),
                Pair.of("+300years", 300),
                Pair.of("+300year", 300),
                Pair.of("300year", 300),
                Pair.of("300year", 300),
                Pair.of("150year 150years", 300),
                Pair.of("150year -150 years +1 year", 1)
        )
                .forEach(pair -> {
                    assertThat(parse(pair.getA(), now))
                            .isEqualToIgnoringNanos(now.plusYears(pair.getB()));
                });

        assertThat(parse("+10 year +15 day -5 secs", now))
                .isEqualToIgnoringNanos(now.plusYears(10).plusDays(15).minusSeconds(5));
    }

    @Test
    public void relativeOffsetAgo() {
        ZonedDateTime now = now();
        assertThat(parse("2 years ago", now)).isEqualToIgnoringNanos(now.minusYears(2));
        assertThat(parse("-2 years ago", now)).isEqualToIgnoringNanos(now.plusYears(2));
        assertThat(parse("8 days ago 14:00", now)).isEqualToIgnoringNanos(now.minusDays(8).withHour(14).truncatedTo(HOURS));
    }

    @Test
    public void relativeWeek() {
        ZonedDateTime now = now();
        assertThat(parse("this week", now))
                .isEqualToIgnoringNanos(now.with(previousOrSame(DayOfWeek.MONDAY)));

        assertThat(parse("last week", now))
                .isEqualTo(parse("previous week", now))
                .isEqualToIgnoringNanos(now.with(previous(DayOfWeek.MONDAY)).with(previous(DayOfWeek.MONDAY)));
        assertThat(parse("next week", now)).isEqualToIgnoringNanos(now.with(next(DayOfWeek.MONDAY)));
    }

    @Test
    public void relativeWeekWithDayName() {
        assertThat(parseWithMicro("Saturday next week", ZonedDateTime.parse("2019-04-21T00:00+04:00")))
                .isEqualTo("2019-04-27T00:00+04:00");
    }

    @Test
    public void yesterdayAndFriends() {
        ZonedDateTime base = ZonedDateTime.parse("2019-04-19T15:15:15Z");

        assertThat(parseWithMicro("now", base)).isEqualTo(base);
        assertThat(parseWithMicro("yesterday", base)).isEqualTo(parseWithMicro("11:00 yesterday", base))
                .isEqualTo(base.minusDays(1).truncatedTo(DAYS));
        assertThat(parseWithMicro("midnight", base)).isEqualTo(parseWithMicro("today", base))
                .isEqualTo(base.truncatedTo(DAYS));

        assertThat(parseWithMicro("noon", base)).isEqualTo(base.withHour(12).truncatedTo(ChronoUnit.HOURS));
        assertThat(parseWithMicro("tomorrow", base)).isEqualTo(base.plusDays(1).truncatedTo(DAYS));

        assertThat(parseWithMicro("yesterday 14:01", base))
                .isEqualTo(base.minusDays(1).withHour(14).withMinute(1).withSecond(0));

        assertThat(parseWithMicro("yesterday noon", base))
                .isEqualTo(base.minusDays(1).withHour(12).withMinute(0).withSecond(0));
    }

    @Test
    public void backOfHour() {
        ZonedDateTime now = now();
        assertThat(parse("back of 9am", now))
                .isEqualTo(parse("back of 09", now)).isEqualTo(parse("BACK OF 09", now)).isEqualTo(parse("BaCK OF 09", now))
                .isEqualToIgnoringNanos(now.withHour(9).withMinute(15).withSecond(0));

        assertThat(parse("back of 9pm", now)).isEqualToIgnoringNanos(now.withHour(21).withMinute(15).withSecond(0));
        assertThat(parse("back of 23", now)).isEqualToIgnoringNanos(now.withHour(23).withMinute(15).withSecond(0));
        assertThat(parse("back of 5", now)).isEqualToIgnoringNanos(now.withHour(5).withMinute(15).withSecond(0));
    }

    @Test
    public void frontOfHour() {
        ZonedDateTime now = now();
        assertThat(parse("front of 5am", now))
                .isEqualToIgnoringNanos(now.withHour(4).withMinute(45).withSecond(0));
        assertThat(parse("front of 23", now))
                .isEqualToIgnoringNanos(now.withHour(22).withMinute(45).withSecond(0));
    }

    @Test
    public void firstDayOf() {
        assertThat(parseLocal("first day of next month"))
                .isEqualToIgnoringNanos(LocalDateTime.now().plusMonths(1).withDayOfMonth(1));

        ZonedDateTime now = now();
        assertThat(parse("first day of March 2005", now)).isEqualToIgnoringNanos(now.withYear(2005).withMonth(3).withDayOfMonth(1).truncatedTo(DAYS));
        assertThat(parse("first day of", now)).isEqualToIgnoringNanos(now.withDayOfMonth(1));
    }

    @Test
    public void relativeTimeText() {
        String[] ordinals = {"this", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"};
        ZonedDateTime now = now();
        for (int i = 0; i < ordinals.length; i++) {
            String ordinal = ordinals[i];
            assertThat(parse(ordinal + " sec", now))
                    .isEqualToIgnoringNanos(parse(ordinal + " secs", now))
                    .isEqualToIgnoringNanos(parse(ordinal + " second", now))
                    .isEqualToIgnoringNanos(parse(ordinal + " seconds", now))
                    .isEqualToIgnoringNanos(now.plusSeconds(i));

            assertThat(parse(ordinal + " min", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " mins", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " minute", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " minutes", now))
                    .isEqualToIgnoringSeconds(now.plusMinutes(i));

            assertThat(parse(ordinal + " hour", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " hours", now))
                    .isEqualToIgnoringSeconds(now.plusHours(i));

            assertThat(parse(ordinal + " day", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " days", now))
                    .isEqualToIgnoringSeconds(now.plusDays(i));

            assertThat(parse(ordinal + " fortnight", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " fortnights", now))
                    .isEqualToIgnoringSeconds(now.plusDays(i * 14));

            assertThat(parse(ordinal + " forthnight", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " forthnights", now))
                    .isEqualToIgnoringSeconds(now.plusDays(i * 14));

            assertThat(parse(ordinal + " month", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " months", now))
                    .isEqualToIgnoringSeconds(now.plusMonths(i));

            assertThat(parse(ordinal + " year", now))
                    .isEqualToIgnoringSeconds(parse(ordinal + " years", now))
                    .isEqualToIgnoringSeconds(now.plusYears(i));
        }

        assertThat(parse("this month", now)).isEqualToIgnoringSeconds(now);
        assertThat(parse("next month", now)).isEqualToIgnoringSeconds(now.plusMonths(1));
        assertThat(parse("previous month", now))
                .isEqualToIgnoringSeconds(parse("last month", now))
                .isEqualToIgnoringSeconds(now.minusMonths(1));
    }

    @Test
    public void lastDayOf() {
        ZonedDateTime now = now();
        assertThat(parse("last day of next month", now))
                .isEqualToIgnoringNanos(now.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));

        assertThat(parse("last day of this month", now))
                .isEqualTo(parse("last day of", now))
                .isEqualToIgnoringNanos(now.with(TemporalAdjusters.lastDayOfMonth()));

        assertThat(parse("last day of previous month", now))
                .isEqualToIgnoringNanos(now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
    }

    @Test
    public void nthDayOfMonth() {
        ZonedDateTime now = now();
        ZonedDateTime expected = now.withYear(2008).withMonth(6).withDayOfMonth(30).truncatedTo(DAYS);
        String[] ordinals = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"};

        for (int i = 0, days = 7; i < ordinals.length; i++, days += 7) {
            assertThat(parse(ordinals[i] + " monday of July 2008", now))
                    .isEqualTo(parse(ordinals[i] + " mon of July 2008", now))
                    .isEqualTo(expected.plusDays(days));
        }

        Stream.of(
                Pair.of("last monday of July 2008", "2008-07-28"),
                Pair.of("previous monday of July 2008", "2008-07-28"),
                Pair.of("next sunday of July 2008", "2008-07-06"),
                Pair.of("previous sunday of July 2008", "2008-07-27")
        ).forEach(pair -> {
            assertThat(parse(pair.getA(), now))
                    .isEqualTo(LocalDate.parse(pair.getB()).atStartOfDay(ZoneId.systemDefault()));
        });
    }

    @Test
    public void relativeDayOfWeek() {

        DAY_OF_WEEKS.forEach((dayOfWeek, strings) -> {
            for (String transformedDayOfWeek : strings) {
                ZonedDateTime now = now();
                assertThat(parse("this " + transformedDayOfWeek, now))
                        .isEqualTo(now.truncatedTo(DAYS).with(nextOrSame(dayOfWeek)));
                TemporalAdjuster nextDayOfWeek = TemporalAdjusters.next(dayOfWeek);

                assertThat(parse("next " + transformedDayOfWeek, now))
                        .isEqualTo(now.truncatedTo(DAYS).with(nextDayOfWeek));

                assertThat(parse("last " + transformedDayOfWeek, now))
                        .isEqualTo(parse("previous " + transformedDayOfWeek, now))
                        .isEqualTo(now.truncatedTo(DAYS).with(previous(dayOfWeek)));

                assertThat(parse("first " + transformedDayOfWeek, now))
                        .isEqualTo(now.truncatedTo(DAYS).with(nextDayOfWeek));

                assertThat(parse("second " + transformedDayOfWeek, now))
                        .isEqualTo(now.truncatedTo(DAYS).with(nextDayOfWeek).with(nextDayOfWeek));

                assertThat(parse("third " + transformedDayOfWeek, now))
                        .isEqualTo(now.truncatedTo(DAYS).with(nextDayOfWeek).with(nextDayOfWeek).with(nextDayOfWeek));
            }
        });
    }

    @Test
    public void dayofWeek() {
        DAY_OF_WEEKS.forEach((dayOfWeek, strings) -> {
            for (String dayOfWeekStr : strings) {
                ZonedDateTime now = now();
                assertThat(parse(dayOfWeekStr, now)).isEqualTo(now.with(nextOrSame(dayOfWeek)).truncatedTo(DAYS));
            }
        });
    }

    @Test
    public void relativeWeekdaysAndFortnight() {
        ZonedDateTime base = ZonedDateTime.parse("2019-04-19T00:00:00+04:00[Asia/Yerevan]");

        Stream.of(Pair.of("+1 weekdays", 3), Pair.of("+5 weekdays", 7), Pair.of("+7 weekdays", 11),
                Pair.of("-6 weekdays", -8), Pair.of("+1 fortnight", 14), Pair.of("+2 fortnight", 28),
                Pair.of("-2 fortnight", -28))
                .forEach(pair -> {
                    assertThat(parseWithMicro(pair.getA(), base))
                            .isEqualToIgnoringNanos(base.plusDays(pair.getB()));
                });
    }

    @Test
    public void cookieDate() {
        assertThat(parse("Sunday, 21-Apr-2019 12:14:15 PDT", now())).isEqualTo("2019-04-21T12:14:15-07:00");
    }

    @Test
    public void rfc850() {
        assertThat(parseLocal("21-Apr-19"))
                .isEqualTo("2019-04-21T00:00:00");

        assertThat(parse("Sunday, 21-Apr-19 22:17:16 +0200", now()))
                .isEqualTo("2019-04-21T22:17:16+02:00");
    }

    @Test
    public void mysqlTimestamp() {
        assertThat(parseLocal("20800410101010")).isEqualTo("2080-04-10T10:10:10");
        assertThat(parseLocal("20800410101010")).isEqualTo("2080-04-10T10:10:10");
    }

    @Test
    public void relativeOffsetNotParserAsTimezone() {
        ZonedDateTime now = now();
        assertThat(parse("28 Feb 2008 12:00:00 +400 years", now))
                .isEqualTo(now.withYear(2408).withMonth(2).withDayOfMonth(28).withHour(12).truncatedTo(HOURS));
    }

    @Test
    public void wddxDateTime() {
        assertThat(parse("2006-1-6T0:0:0-8:0", now()))
                .isEqualTo("2006-01-06T00:00:00-08:00");
    }

    @Test
    public void monthYearAndYearMonth() {
        ZonedDateTime now = now();
        assertThat(parse("2001 Oct", now)).isEqualTo(parse("Oct 2001", now));
    }

    @Test
    public void yearMonthDay() {
        ZonedDateTime now = now();
        assertThat(parse("2005/8/12", now).toLocalDateTime())
                .isEqualTo(parse("2005-8-12", now).toLocalDateTime())
                .isEqualTo("2005-08-12T00:00:00");

        Stream.of("2005/1/2", "2005/01/02", "2005/01/2", "2005/1/02")
                .map(s -> parse(s, now))
                .map(ZonedDateTime::toLocalDateTime)
                .forEach(actual -> {
                    assertThat(actual).isEqualTo("2005-01-02T00:00:00");
                });
    }

    @Test
    public void textualMonthAndDayWithoutSuffix() {
        assertThat(parseLocal("Oct11"))
                .isEqualTo(parseLocal("11Oct"))
                .isEqualTo(LocalDate.now().atStartOfDay().withMonth(10).withDayOfMonth(11));

        assertThat(parseLocal("11Oct 2005"))
                .isEqualTo(parseLocal("11Oct2005"))
                .isEqualTo(LocalDate.of(2005, 10, 11).atStartOfDay());
    }

    @Test
    public void only12HourTime() {
        Stream.of("5:05", "05:05", "05:5", "05.05", "5.05", "5.5", "05.5")
                .map(DateTimeParserTest::parseTime)
                .forEach(actual -> assertThat(actual).isEqualTo("05:05:00"));
    }

    @Test
    public void support24AsHour() {
        assertThat(parse("2007-11-01T24:34:00+00:00", now())).isEqualTo("2007-11-02T00:34:00+00:00");
    }

    @Test
    public void decimalInRelativeTime() {
        assertThat(parseLocal("+1.61538461538 day"))
                .isEqualTo(parseLocal("+1 61538461538 day"))
                .isEqualToIgnoringNanos(LocalDateTime.now().plusDays(61538461538L));
    }

    @Test
    public void relativeTimeSecondFractions() {
        // millis
        Stream.of("ms", "msec", "msecs", "millisecond", "milliseconds")
                .map(unit -> "2016-10-07 13:25:50 +1 " + unit)
                .map(DateTimeParserTest::parseLocal)
                .forEach(actual -> assertThat(actual).isEqualTo("2016-10-07T13:25:50.001"));


        // micros
        Stream.of("usec", "usecs", "microsecond", "microseconds", "µs", "µsec", "µsecs")
                .map(unit -> "2016-10-07 13:25:50 -6 " + unit)
                .map(DateTimeParserTest::parseLocal)
                .forEach(actual -> assertThat(actual).isEqualTo("2016-10-07T13:25:49.999994"));
    }

    @Test
    public void timezoneWithComment() {
        assertThat(parse("Sun, 13 Nov 2005 22:56:10 -0800 (PST)", now()))
                .isEqualTo("2005-11-13T22:56:10-08:00");
    }

    @Test
    public void longTimezones() {
        SoftAssertions.assertSoftly(sa -> {
            Stream.of("Africa/Dar_es_Salaam", "Africa/Porto-Novo", "Chile/EasterIsland")
                    .forEach(s -> {
                        ZonedDateTime now = now();
                        sa.assertThat(parse(s, now)).isEqualToIgnoringSeconds(now.withZoneSameLocal(ZoneId.of(s)));
                    });
            sa.assertAll();
        });
    }

    @Test
    public void fourDigitYear() {
        ZonedDateTime now = now();
        assertThat(parse("Jan 0099", now).getYear())
                .isEqualTo(parse("0099-01", now).getYear())
                .isEqualTo(parse("Jan 1, 0099", now).getYear())
                .isEqualTo(99);
    }

    @Test
    public void incompleteDates() {
        assertThat(parseLocal("2017-03"))
                .isEqualTo("2017-03-01T00:00:00");
    }

    @Test
    public void shouldThrowExceptionWhenDateInvalid() {
        ZonedDateTime now = now();
        assertThatThrownBy(() -> parse("£61538461538 day", now))
                .hasCause(new DateTimeParserException("£61538461538 day", 0));

        assertThatThrownBy(() -> parse("2009---01", now))
                .hasCauseExactlyInstanceOf(DateTimeParserException.class);
    }
}