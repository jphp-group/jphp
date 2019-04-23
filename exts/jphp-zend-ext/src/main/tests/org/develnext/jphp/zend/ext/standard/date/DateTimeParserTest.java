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

    private static ZonedDateTime parse(String input) {
        return parseWithMicro(input, ZonedDateTime.now()).withNano(0);
    }

    private static ZonedDateTime parse(String input, String zoneId) {
        return parseWithMicro(input, zoneId).withNano(0);
    }

    private static ZonedDateTime parseWithMicro(String input) {
        return new DateTimeParser(input).parse();
    }

    private static ZonedDateTime parseWithMicro(String input, String zoneId) {
        return new DateTimeParser(input, ZoneId.of(zoneId)).parse();
    }

    private static ZonedDateTime parseWithMicro(String input, ZonedDateTime base) {
        return new DateTimeParser(input, base).parse();
    }

    private static ZonedDateTime withTime(int hour, int minute, int second) {
        return now().withHour(hour).withMinute(minute).withSecond(second).withNano(0);
    }

    private static ZonedDateTime withTime(int hour, int minute, int second, String zone) {
        return now().withHour(hour).withMinute(minute).withSecond(second).withNano(0)
                .withZoneSameLocal(ZoneId.of(zone));
    }

    @Test
    public void dummy() {
        //assertEquals(ZonedDateTime.parse("2008-01-28T00:00:00+04:00[Asia/Yerevan]"), parse("2008-W05"));
        //assertEquals(ZonedDateTime.parse("2007-12-31T00:00:00+04:00[Asia/Yerevan]"), parse("2008W01"));
        //assertEquals(ZonedDateTime.parse("2007-12-31T00:00:00+04:00[Asia/Yerevan]"), parse("2008-W01-3"));

        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36-07:00[GMT-07:00]"), parse("10/Oct/2000:13:55:36 GMT-7"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36+04:30[GMT+04:30]"), parse("10/Oct/2000:13:55:36 GMT+04:30"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36-04:30[GMT-04:30]"), parse("10/Oct/2000:13:55:36 GMT-04:30"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36+07:00[GMT+07:00]"), parse("10/Oct/2000:13:55:36 GMT+7"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36+07:00[GMT+07:00]"), parse("10/Oct/2000:13:55:36 GMT+07"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36+07:00"), parse("10/Oct/2000:13:55:36 +7"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36-07:00"), parse("10/Oct/2000:13:55:36 -7"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36-07:00"), parse("10/Oct/2000:13:55:36 -0700"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36-07:00"), parse("10/Oct/2000:13:55:36 -07:00"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36-07:00"), parse("10/Oct/2000:13:55:36 -07"));
        assertEquals(ZonedDateTime.parse("2000-10-10T13:55:36-07:00"), parse("10/Oct/2000:13:55:36 -07"));
        assertEquals(ZonedDateTime.parse("1991-08-07T18:11:31+04:00[Asia/Yerevan]"), parse("1991-08-07 18:11:31"));
        assertEquals(ZonedDateTime.parse("2008-07-01T09:03:37+04:00[Asia/Yerevan]"), parse("2008-7-1T9:3:37"));
        assertEquals(ZonedDateTime.parse("2018-12-07T23:59:59+04:00[Asia/Yerevan]"), parse("2018:12:07 23:59:59"));
        assertEquals(ZonedDateTime.parse("2008-07-01T22:38:07+04:00[Asia/Yerevan]"), parse("20080701T22:38:07"));
        assertEquals(ZonedDateTime.parse("2038-07-01T05:38:07+04:00[Asia/Yerevan]"), parse("20380701t53807"));
        assertEquals(ZonedDateTime.parse("2038-07-01T05:38:07+04:00[Asia/Yerevan]"), parse("20380701T53807"));
        assertEquals(ZonedDateTime.parse("2008-07-01T09:38:07+04:00[Asia/Yerevan]"), parse("20080701T9:38:07"));
        assertEquals(ZonedDateTime.parse("2008-01-02T00:00:00+04:00[Asia/Yerevan]"), parse("2008.002"));
        assertEquals(ZonedDateTime.parse("2008-01-02T00:00:00+04:00[Asia/Yerevan]"), parse("2008.002"));
    }

    @Test
    public void soap() {
        // microseconds
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.300+08:00"), parseWithMicro("2008-07-01T22:35:17.3+08:00"));
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.030+08:00"), parseWithMicro("2008-07-01T22:35:17.03+08:00"));
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.0030+08:00"), parseWithMicro("2008-07-01T22:35:17.003+08:00"));

        // without timezone
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.300+04:00[Asia/Yerevan]"), parseWithMicro("2008-07-01T22:35:17.3"));
    }

    @Test
    public void unixtimestamp() {
        assertThat(parse("@1"))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("1970-01-01T00:00:01Z[UTC]"));

        assertThat(parse("@-1"))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("1969-12-31T23:59:59Z[UTC]"));

        assertThat(parse("@-1151515151515"))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("-34521-12-09T08:08:05Z[UTC]"));
    }

    @Test
    public void onlyTimeZone() {
        assertThat(parse("+0000 GMT"))
                .isEqualToIgnoringNanos(now().withZoneSameLocal(ZoneId.of("Z")));

        for (String tz : Arrays.asList("+0", "+00", "+0000", "GMT")) {
            assertThat(parse(tz))
                    .isEqualToIgnoringNanos(now().withZoneSameLocal(ZoneId.of(tz)));
        }
    }

    @Test
    public void hourAndMinuteWithMeridian() {
        assertEquals(now().withHour(4).withMinute(8).withSecond(0).withNano(0), parse("4:08 am"));
        assertEquals(now().withHour(19).withMinute(19).withSecond(0).withNano(0), parse("7:19P.M."));
    }

    @Test
    public void hourAndMinute() {
        assertThat(parse("t0222")).isEqualTo(now().withHour(2).withMinute(22).truncatedTo(MINUTES));
        assertThat(parse("t0222 t0222"))
                .isEqualTo(now().withYear(222).withHour(2).withMinute(22).truncatedTo(MINUTES));
    }

    @Test
    public void postgresDayOfYear() {
        assertThat(parse("2006167"))
                .isEqualTo(parse("2006.167"))
                .isEqualTo(LocalDate.of(2006, 6, 16).atStartOfDay(ZoneId.systemDefault()));
    }

    @Test
    public void hourMinuteSecondWithMeridian() {
        assertEquals(now().withHour(4).withMinute(8).withSecond(37).withNano(0), parse("4:08:37 am"));
        assertEquals(now().withHour(19).withMinute(19).withSecond(19).withNano(0), parse("7:19:19P.M."));
    }

    @Test
    public void mssqlTime() {
        assertEquals(now()
                        .withHour(4)
                        .withMinute(8)
                        .withSecond(39)
                        .with(ChronoField.MICRO_OF_SECOND, 123130),
                parseWithMicro("4:08:39:12313am"));

        assertEquals(now()
                        .withHour(4)
                        .withMinute(8)
                        .withSecond(39)
                        .with(ChronoField.MICRO_OF_SECOND, 123130),
                parseWithMicro("4:08:39.12313am"));

        assertEquals(now()
                        .withHour(16)
                        .withMinute(8)
                        .withSecond(39)
                        .with(ChronoField.MICRO_OF_SECOND, 123130),
                parseWithMicro("4:08:39.12313pm"));
    }

    @Test
    public void hourMinuteNoColon() {
        assertEquals(withTime(4, 8, 0), parse("0408"));
        assertEquals(withTime(4, 8, 0), parse("T0408"));
        assertEquals(withTime(4, 8, 0), parse("t0408"));
    }

    @Test
    public void hourMinuteSecondNoColon() {
        assertEquals(withTime(19, 19, 19), parse("T191919"));
        assertEquals(withTime(19, 19, 19), parse("191919"));
        assertEquals(withTime(4, 8, 37), parse("040837"));
        assertEquals(withTime(4, 8, 37), parse("t040837"));
        assertEquals(withTime(4, 8, 37), parse("T040837"));
    }

    @Test
    public void hour24Notation() {
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT+0700")), parse("T19:19:19GMT+0700"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-0700")), parse("T19:19:19GMT-0700"));

        assertEquals(withTime(4, 8, 0), parse("04:08"));
        assertEquals(withTime(4, 8, 0), parse("t04:08"));
        assertEquals(withTime(4, 8, 0), parse("T04:08"));
        assertEquals(withTime(19, 19, 0), parse("19.19"));
        assertEquals(withTime(23, 43, 0), parse("T23:43"));
        assertEquals(withTime(23, 43, 0), parse("t23:43"));

        assertEquals(withTime(4, 8, 59), parse("04:08:59"));
        assertEquals(withTime(4, 8, 59), parse("t04:08:59"));
        assertEquals(withTime(4, 8, 59), parse("T04:08:59"));
        assertEquals(withTime(19, 19, 19), parse("T19.19.19"));
        assertEquals(withTime(19, 19, 19), parse("t19.19.19"));

        assertEquals(withTime(4, 8, 37).with(ChronoField.MICRO_OF_SECOND, 814120), parseWithMicro("04.08.37.81412"));
        assertEquals(withTime(19, 19, 19).with(ChronoField.MICRO_OF_SECOND, 532453), parseWithMicro("19:19:19.532453"));
    }

    @Test
    public void tz() {
        ZonedDateTime expected = withTime(19, 19, 19);

        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19GMT-6"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT+06:00")), parse("T19:19:19GMT+6"));
        assertEquals(withTime(4, 8, 37).withZoneSameLocal(ZoneId.of("+02:00")), parse("T040837CEST"));
        assertEquals(withTime(4, 8, 37).withZoneSameLocal(ZoneId.of("+02:00")), parse("T040837 CEST"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("+02:00")), parse("T19:19:19CEST"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("+10:30")), parse("T19:19:19ACDT"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT+06:00")), parse("T19:19:19GMT+6"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19GMT-06"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:00")), parse("T19:19:19-6"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-0630"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-630"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-630"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-6:30"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:00")), parse("T19:19:19-6"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19 GMT-630"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19GMT-630"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19 -630"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19 -6:30"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19 GMT-6:30"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19 GMT-6"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19 GMT-06"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-07:00")), parse("T19:19:19 -07"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-07:00")), parse("T19:19:19 -07:00"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("-0700")), parse("T19:19:19 -0700"));
        assertEquals(expected.withZoneSameLocal(ZoneId.of("GMT-0700")), parse("T19:19:19 GMT-0700"));
    }

    @Test
    public void timezoneLong() {
        assertEquals(withTime(19, 19, 19, "Europe/Amsterdam"), parse("T19:19:19 Europe/Amsterdam"));
        assertEquals(withTime(19, 19, 19, "America/Indiana/Knox"), parse("T19:19:19 America/Indiana/Knox"));
        assertEquals(withTime(19, 19, 19, "Europe/Amsterdam"), parse("T19:19:19Europe/Amsterdam"));
        assertEquals(withTime(19, 19, 19, "Europe/Amsterdam"), parse("191919Europe/Amsterdam"));
    }

    @Test
    public void timezoneAlias() {
        Stream.of(new Pair<>("CEST", "+02:00"), new Pair<>("ACDT", "+10:30"))
                .forEach(pair -> {
                    ZoneId zoneId = ZoneId.of(pair.getB());
                    ZonedDateTime actual = parse("T19:19:19" + pair.getA());
                    ZonedDateTime expected = withTime(19, 19, 19, pair.getB());

                    assertEquals(expected, actual);
                });
    }

    @Test
    public void americalDate() {
        assertThat(parse("5/12")).isEqualToIgnoringNanos(now().withMonth(5).withDayOfMonth(12));
        assertThat(parse("10/27")).isEqualToIgnoringNanos(now().withMonth(10).withDayOfMonth(27));

        assertThat(parse("12/22/78")).isEqualToIgnoringNanos(now().withMonth(12).withDayOfMonth(22).withYear(1978));
        assertThat(parse("1/17/2006")).isEqualToIgnoringNanos(now().withMonth(1).withDayOfMonth(17).withYear(2006));
        assertThat(parse("1/17/6")).isEqualToIgnoringNanos(now().withMonth(1).withDayOfMonth(17).withYear(2006));
    }

    @Test
    public void GNUDate() {
        assertThat(parse("2008-6")).isEqualToIgnoringNanos(now().withYear(2008).withMonth(6));
        assertThat(parse("2008-06")).isEqualToIgnoringNanos(now().withYear(2008).withMonth(6));
        assertThat(parse("1978-12")).isEqualToIgnoringNanos(now().withYear(1978).withMonth(12));
    }

    @Test
    public void sandbox() {

    }

    @Test
    public void dateNotResetingTimeWhenTimeIsPresent() {
        assertThat(parse("17:00 2004-01-01"))
                .isEqualTo(now().withYear(2004).withMonth(01).withDayOfMonth(01).withHour(17).truncatedTo(HOURS));
    }

    @Test
    public void ordinalAndNumberDifference() {
        assertThat(parse("1 Monday December 2008"))
                .isEqualTo(now().withYear(2008).withMonth(12).withDayOfMonth(1).truncatedTo(DAYS));

        assertThat(parse("2 Monday December 2008"))
                .isEqualTo(now().withYear(2008).withMonth(12).withDayOfMonth(8).truncatedTo(DAYS));
    }

    @Test
    public void timeShouldNotReset() {
        assertThat(parse("19:30 Dec 17 2005"))
                .isEqualTo(parse("Dec 17 19:30 2005"))
                .isEqualTo(now().withYear(2005).withMonth(12).withDayOfMonth(17).withHour(19).withMinute(30).truncatedTo(MINUTES));
    }

    @Test
    public void isoYearWithWeekAndWeekDay() {
        ZonedDateTime parse = parse("1997W011");
        assertThat(parse)
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(1996).withMonth(12).withDayOfMonth(30));
    }

    @Test
    public void isoYearWithWeek() {
        assertThat(parse("2004W30"))
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(2004).withMonth(7).withDayOfMonth(19));
    }

    @Test
    public void isoYearWeekWeekDayAndTime() {
        ZonedDateTime parse = parse("2004W101T05:00+0");
        assertThat(parse)
                .isEqualToIgnoringNanos(withTime(5, 0, 0).withYear(2004).withMonth(3).withDayOfMonth(1)
                        .withZoneSameLocal(ZoneId.of("+0")));
    }

    @Test
    public void timestampWithTimezone() {
        ZonedDateTime expected = ZonedDateTime.parse("2005-07-14T20:30:41Z").withZoneSameInstant(ZoneIdFactory.of("CEST"));
        ZonedDateTime actual = parse("@1121373041 CEST");

        assertThat(actual).isEqualToIgnoringNanos(expected);
    }

    @Test
    public void justYear() {
        Stream.of("1978", "1979", "1980")
                .forEach(yearStr -> {
                    assertThat(parse(yearStr)).isEqualToIgnoringNanos(now().withYear(Integer.parseInt(yearStr)));
                });
    }

    @Test
    public void justMonth() {
        Locale l = Locale.US;
        for (Month month : Month.values()) {
            for (TextStyle value : Arrays.asList(TextStyle.FULL, TextStyle.SHORT)) {
                String monthName = month.getDisplayName(value, l);
                ZonedDateTime expected = now().withMonth(month.getValue()).truncatedTo(DAYS);

                assertThat(parse(monthName)).isEqualToIgnoringSeconds(expected);
                assertThat(parse(monthName.toLowerCase())).isEqualToIgnoringSeconds(expected);
                assertThat(parse(monthName.toUpperCase())).isEqualToIgnoringSeconds(expected);
            }
        }
    }

    @Test
    public void hourWithMeridian() {
        Stream.of("5P.M.", "5 pm", "5 p.m", "5 p.m.", "5 P.m.", "5 P.M.", "5 p.M.", "5 p.M")
                .map(DateTimeParserTest::parse)
                .forEach(dateTime -> {
                    assertThat(dateTime).isEqualToIgnoringNanos(withTime(17, 0, 0));
                });
    }

    @Test
    public void yearAndTextualMonth() {
        Stream.of("2008 June", "2008-VI", "2008.VI", "2008 VI", "2008 june", "2008 jUnE", "2008.jUnE")
                .map(DateTimeParserTest::parse)
                .forEach(parsed -> {
                    assertThat(parsed)
                            .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(1).truncatedTo(DAYS));
                });
    }

    @Test
    public void customDates() {
        assertThat(parse("March 1879"))
                .isEqualToIgnoringNanos(now().withYear(1879).withMonth(3).withDayOfMonth(1).truncatedTo(DAYS));

        Stream.of("78-Dec-22", "1978-Dec-22", "1978-DEC-22", "78-DEC-22", "78-DEc-22")
                .map(DateTimeParserTest::parse)
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(1978, 12, 22), LocalTime.MIDNIGHT, ZoneId.systemDefault());
                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        Stream.of("DEC-1978", "DEC1978", "DEC 1978", "DEC- 1978", "DEC - 1978", "Dec ----  --- -- 1978",
                "dec\t- \t\t-- --\t 1978", "dec.-\t1978", "dec.......-\t1978", "dec . . .....--- . \t1978")
                .map(DateTimeParserTest::parse)
                .forEach(parsed -> {
                    assertThat(parsed)
                            .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(1).truncatedTo(DAYS));
                });

        Stream.of("July 1st, 2008", "July 1st , 2008", "July 1, 2008", "July 1,2008", "July 1,08",
                "July.1,2008", "July.1rd ,2008")
                .map(DateTimeParserTest::parse)
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(2008, 7, 1), LocalTime.MIDNIGHT, ZoneId.systemDefault());
                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        Stream.of("July 1st,", "July 1st ,", "July 1", "July.1", "July....1", "July  ...1", "July... .. 1,,,.., ", "1 July", "1.July", "1.JulY")
                .map(DateTimeParserTest::parse)
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(Year.now().getValue(), 7, 1), LocalTime.MIDNIGHT, ZoneId.systemDefault());

                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        Stream.of("May-09-78", "May-09-1978", "may-09-1978", "mAY-09-78")
                .map(DateTimeParserTest::parse)
                .forEach(parsed -> {
                    ZonedDateTime of = ZonedDateTime.of(LocalDate.of(1978, 5, 9), LocalTime.MIDNIGHT, ZoneId.systemDefault());

                    assertThat(parsed).isEqualToIgnoringNanos(of);
                });

        assertThat(parse("DEC1978"))
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(1).truncatedTo(DAYS));

        assertThat(parse("June 2008"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(1).truncatedTo(DAYS));

        assertThat(parse("14 III 1879"))
                .isEqualToIgnoringNanos(now().withYear(1879).withMonth(3).withDayOfMonth(14).truncatedTo(DAYS));

        assertThat(parse("22DEC78"))
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(22).truncatedTo(DAYS));

        assertThat(parse("30-June 2008"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(30).truncatedTo(DAYS));

        assertThat(parse("22\t12.78"))
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(22).truncatedTo(DAYS));

        assertThat(parse("30.6.08"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(30).truncatedTo(DAYS));

        assertThat(parse("22.12.1978"))
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(22).truncatedTo(DAYS));

        assertThat(parse("30-6-2008"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(30).truncatedTo(DAYS));

        assertThat(parse("8-6-21"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(21));

        assertThat(parse("2005-07-18 22:10:00 +0400"))
                .isEqualToIgnoringNanos(now().withYear(2005).withMonth(7).withDayOfMonth(18)
                        .withHour(22).withMinute(10).withSecond(00)
                        .withZoneSameLocal(ZoneId.of("+0400")));

        assertThat(parse("2005-07-14 22:30:41 GMT", "Europe/London"))
                .isEqualToIgnoringNanos(now().withYear(2005).withMonth(7).withDayOfMonth(14)
                        .withHour(22).withMinute(30).withSecond(41)
                        .withZoneSameLocal(ZoneId.of("GMT"))
                );

        assertThat(parse("2005-07-14 22:30:41"))
                .isEqualToIgnoringNanos(now().withYear(2005).withMonth(7).withDayOfMonth(14)
                        .withHour(22).withMinute(30).withSecond(41));

        assertThat(parse("1999-10-13"))
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(1999).withMonth(10).withDayOfMonth(13));

        assertThat(parse("Oct 13  1999"))
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(1999).withMonth(10).withDayOfMonth(13));
    }

    @Test
    public void iso8DigitYearDayMonth() {
        ZonedDateTime dateTime = withTime(0, 0, 0);
        assertThat(parse("15810726"))
                .isEqualToIgnoringNanos(dateTime.withYear(1581).withMonth(07).withDayOfMonth(26));

        assertThat(parse("19780417"))
                .isEqualToIgnoringNanos(dateTime.withYear(1978).withMonth(04).withDayOfMonth(17));

        assertThat(parse("18140517"))
                .isEqualToIgnoringNanos(dateTime.withYear(1814).withMonth(05).withDayOfMonth(17));
    }

    @Test
    public void isoYearDayMonthWithSlashes() {
        ZonedDateTime dateTime = withTime(0, 0, 0);
        assertThat(parse("2008/06/30"))
                .isEqualToIgnoringNanos(dateTime.withYear(2008).withMonth(06).withDayOfMonth(30));

        assertThat(parse("1978/12/22"))
                .isEqualToIgnoringNanos(dateTime.withYear(1978).withMonth(12).withDayOfMonth(22));
    }

    @Test
    public void isoYearDayMonthWithDashes() {
        ZonedDateTime dateTime = withTime(0, 0, 0);
        assertThat(parse("08-06-30"))
                .isEqualToIgnoringNanos(dateTime.withYear(2008).withMonth(06).withDayOfMonth(30));

        assertThat(parse("78-12-22"))
                .isEqualToIgnoringNanos(dateTime.withYear(1978).withMonth(12).withDayOfMonth(22));
    }

    @Test
    public void relativeOffsets() {
        Stream.of(
                Pair.of("150year 150years", 300),
                Pair.of("+1 year", 1),
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
                    assertThat(parse(pair.getA()))
                            .isEqualToIgnoringNanos(now().plusYears(pair.getB()));
                });

        assertThat(parse("+10 year +15 day -5 secs"))
                .isEqualToIgnoringNanos(now().plusYears(10).plusDays(15).minusSeconds(5));
    }

    @Test
    public void relativeOffsetAgo() {
        assertThat(parse("2 years ago")).isEqualToIgnoringNanos(now().minusYears(2));
        assertThat(parse("-2 years ago")).isEqualToIgnoringNanos(now().plusYears(2));
        assertThat(parse("8 days ago 14:00")).isEqualToIgnoringNanos(now().minusDays(8).withHour(14).truncatedTo(HOURS));
    }

    @Test
    public void relativeWeek() {
        assertThat(parse("this week"))
                .isEqualToIgnoringNanos(now().with(previousOrSame(DayOfWeek.MONDAY)));

        assertThat(parse("last week"))
                .isEqualTo(parse("previous week"))
                .isEqualToIgnoringNanos(now().with(previous(DayOfWeek.MONDAY)).with(previous(DayOfWeek.MONDAY)));
        assertThat(parse("next week")).isEqualToIgnoringNanos(now().with(next(DayOfWeek.MONDAY)));
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
        assertThat(parse("back of 9am"))
                .isEqualTo(parse("back of 09")).isEqualTo(parse("BACK OF 09")).isEqualTo(parse("BaCK OF 09"))
                .isEqualToIgnoringNanos(now().withHour(9).withMinute(15).withSecond(0));

        assertThat(parse("back of 9pm")).isEqualToIgnoringNanos(now().withHour(21).withMinute(15).withSecond(0));
        assertThat(parse("back of 23")).isEqualToIgnoringNanos(now().withHour(23).withMinute(15).withSecond(0));
        assertThat(parse("back of 5")).isEqualToIgnoringNanos(now().withHour(5).withMinute(15).withSecond(0));
    }

    @Test
    public void frontOfHour() {
        assertThat(parse("front of 5am"))
                .isEqualToIgnoringNanos(now().withHour(4).withMinute(45).withSecond(0));
        assertThat(parse("front of 23"))
                .isEqualToIgnoringNanos(now().withHour(22).withMinute(45).withSecond(0));
    }

    @Test
    public void firstDayOf() {
        assertThat(parse("first day of March 2005")).isEqualToIgnoringNanos(now().withYear(2005).withMonth(3).withDayOfMonth(1).truncatedTo(DAYS));
        assertThat(parse("first day of")).isEqualToIgnoringNanos(now().withDayOfMonth(1));
    }

    @Test
    public void relativeTimeText() {
        String[] ordinals = {"this", "first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"};
        for (int i = 0; i < ordinals.length; i++) {
            String ordinal = ordinals[i];
            assertThat(parse(ordinal + " sec"))
                    .isEqualTo(parse(ordinal + " secs"))
                    .isEqualTo(parse(ordinal + " second"))
                    .isEqualTo(parse(ordinal + " seconds"))
                    .isEqualToIgnoringNanos(now().plusSeconds(i));

            assertThat(parse(ordinal + " min"))
                    .isEqualTo(parse(ordinal + " mins"))
                    .isEqualTo(parse(ordinal + " minute"))
                    .isEqualTo(parse(ordinal + " minutes"))
                    .isEqualToIgnoringNanos(now().plusMinutes(i));

            assertThat(parse(ordinal + " hour")).isEqualTo(parse(ordinal + " hours"))
                    .isEqualToIgnoringNanos(now().plusHours(i));

            assertThat(parse(ordinal + " day")).isEqualTo(parse(ordinal + " days"))
                    .isEqualToIgnoringNanos(now().plusDays(i));

            assertThat(parse(ordinal + " fortnight")).isEqualTo(parse(ordinal + " fortnights"))
                    .isEqualToIgnoringNanos(now().plusDays(i * 14));

            assertThat(parse(ordinal + " forthnight")).isEqualTo(parse(ordinal + " forthnights"))
                    .isEqualToIgnoringNanos(now().plusDays(i * 14));

            assertThat(parse(ordinal + " month")).isEqualTo(parse(ordinal + " months"))
                    .isEqualToIgnoringNanos(now().plusMonths(i));

            assertThat(parse(ordinal + " year")).isEqualTo(parse(ordinal + " years"))
                    .isEqualToIgnoringNanos(now().plusYears(i));
        }

        assertThat(parse("this month")).isEqualToIgnoringNanos(now());
        assertThat(parse("next month")).isEqualToIgnoringNanos(now().plusMonths(1));
        assertThat(parse("previous month")).isEqualTo(parse("last month"))
                .isEqualToIgnoringNanos(now().minusMonths(1));
    }

    @Test
    public void lastDayOf() {
        assertThat(parse("last day of next month"))
                .isEqualToIgnoringNanos(now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));

        assertThat(parse("last day of this month"))
                .isEqualTo(parse("last day of"))
                .isEqualToIgnoringNanos(now().with(TemporalAdjusters.lastDayOfMonth()));

        assertThat(parse("last day of previous month"))
                .isEqualToIgnoringNanos(now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
    }

    @Test
    public void nthDayOfMonth() {
        ZonedDateTime expected = now().withYear(2008).withMonth(6).withDayOfMonth(30).truncatedTo(DAYS);
        String[] ordinals = {"first", "second", "third", "fourth", "fifth", "sixth", "seventh", "eighth", "ninth", "tenth", "eleventh", "twelfth"};

        for (int i = 0, days = 7; i < ordinals.length; i++, days += 7) {
            assertThat(parse(ordinals[i] + " monday of July 2008"))
                    .isEqualTo(parse(ordinals[i] + " mon of July 2008"))
                    .isEqualTo(expected.plusDays(days));
        }

        Stream.of(
                Pair.of("last monday of July 2008", "2008-07-28"),
                Pair.of("previous monday of July 2008", "2008-07-28"),
                Pair.of("next sunday of July 2008", "2008-07-06"),
                Pair.of("previous sunday of July 2008", "2008-07-27")
        ).forEach(pair -> {
            assertThat(parse(pair.getA()))
                    .isEqualTo(LocalDate.parse(pair.getB()).atStartOfDay(ZoneId.systemDefault()));
        });
    }

    @Test
    public void relativeDayOfWeek() {

        DAY_OF_WEEKS.forEach((dayOfWeek, strings) -> {
            for (String transformedDayOfWeek : strings) {
                assertThat(parse("this " + transformedDayOfWeek))
                        .isEqualTo(now().truncatedTo(DAYS).with(nextOrSame(dayOfWeek)));
                TemporalAdjuster nextDayOfWeek = TemporalAdjusters.next(dayOfWeek);

                assertThat(parse("next " + transformedDayOfWeek))
                        .isEqualTo(now().truncatedTo(DAYS).with(nextDayOfWeek));

                assertThat(parse("last " + transformedDayOfWeek))
                        .isEqualTo(parse("previous " + transformedDayOfWeek))
                        .isEqualTo(now().truncatedTo(DAYS).with(previous(dayOfWeek)));

                assertThat(parse("first " + transformedDayOfWeek))
                        .isEqualTo(now().truncatedTo(DAYS).with(nextDayOfWeek));

                assertThat(parse("second " + transformedDayOfWeek))
                        .isEqualTo(now().truncatedTo(DAYS).with(nextDayOfWeek).with(nextDayOfWeek));

                assertThat(parse("third " + transformedDayOfWeek))
                        .isEqualTo(now().truncatedTo(DAYS).with(nextDayOfWeek).with(nextDayOfWeek).with(nextDayOfWeek));
            }
        });
    }

    @Test
    public void dayofWeek() {
        DAY_OF_WEEKS.forEach((dayOfWeek, strings) -> {
            for (String dayOfWeekStr : strings) {
                assertThat(parse(dayOfWeekStr)).isEqualTo(now().with(nextOrSame(dayOfWeek)).truncatedTo(DAYS));
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
        assertThat(parse("Sunday, 21-Apr-2019 12:14:15 PDT")).isEqualTo("2019-04-21T12:14:15-07:00");
    }

    @Test
    public void rfc850() {
        assertThat(parse("21-Apr-19"))
                .isEqualTo("2019-04-21T00:00:00+04:00");

        assertThat(parse("Sunday, 21-Apr-19 22:17:16 +0200"))
                .isEqualTo("2019-04-21T22:17:16+02:00");
    }

    @Test
    public void mysqlTimestamp() {
        assertThat(parse("20800410101010")).isEqualTo("2080-04-10T10:10:10+04:00");
        assertThat(parse("20800410101010")).isEqualTo("2080-04-10T10:10:10+04:00");
    }

    @Test
    public void relativeOffsetNotParserAsTimezone() {
        assertThat(parse("28 Feb 2008 12:00:00 +400 years"))
                .isEqualTo(now().withYear(2408).withMonth(2).withDayOfMonth(28).withHour(12).truncatedTo(HOURS));
    }

    @Test
    public void monthYearAndYearMonth() {
        assertThat(parse("2001 Oct")).isEqualTo(parse("Oct 2001"));
    }

    @Test
    public void shouldThrowExceptionWhenDateInvalid() {
        assertThatThrownBy(() -> parse("2009---01"));
    }
}