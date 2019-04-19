package org.develnext.jphp.zend.ext.standard.date;

import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.tokenize;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import org.junit.Test;

import php.runtime.common.Pair;

public class DateTimeParserTest {

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
        return new DateTimeParser(input, base, ZoneId.systemDefault(), Locale.getDefault()).parse();
    }

    private static ZonedDateTime withTime(int hour, int minute, int second) {
        return now().withHour(hour).withMinute(minute).withSecond(second).withNano(0);
    }

    private static ZonedDateTime withTime(int hour, int minute, int second, String zone) {
        return now().withHour(hour).withMinute(minute).withSecond(second).withNano(0)
                .withZoneSameLocal(ZoneId.of(zone));
    }

    @Test
    public void hourAndMinute() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 2),
                Token.of(Symbol.COLON, 2, 1),
                Token.of(Symbol.DIGITS, 3, 2)
        ), tokenize("00:15"));
    }

    @Test
    public void hourMinuteSecond() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 2),
                Token.of(Symbol.COLON, 2, 1),
                Token.of(Symbol.DIGITS, 3, 2),
                Token.of(Symbol.COLON, 5, 1),
                Token.of(Symbol.DIGITS, 6, 2)
        ), tokenize("00:15:00"));
    }

    @Test
    public void hourMinuteSecondsWithMicros() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 2),
                Token.of(Symbol.COLON, 2, 1),
                Token.of(Symbol.DIGITS, 3, 2),
                Token.of(Symbol.COLON, 5, 1),
                Token.of(Symbol.DIGITS, 6, 2),
                Token.of(Symbol.DOT, 8, 1),
                Token.of(Symbol.DIGITS, 9, 4)
        ), tokenize("00:15:00.0000"));
    }

    @Test
    public void meridianOnly() {
        assertThat(tokenize("A.m."))
                .containsExactly(
                        Token.of(Symbol.STRING, 0, 1),
                        Token.of(Symbol.DOT, 1, 1),
                        Token.of(Symbol.STRING, 2, 1),
                        Token.of(Symbol.DOT, 3, 1)
                );

        assertThat(tokenize("am")).containsOnly(Token.of(Symbol.STRING, 0, 2));
        assertThat(tokenize("Am")).containsOnly(Token.of(Symbol.STRING, 0, 2));
        assertThat(tokenize("A.m")).containsExactly(
                Token.of(Symbol.STRING, 0, 1),
                Token.of(Symbol.DOT, 1, 1),
                Token.of(Symbol.STRING, 2, 1)
        );
    }

    @Test
    public void meridianWithHour() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.STRING, 1, 2)
        ), tokenize("4am"));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.STRING, 1, 2)
        ), tokenize("4pm"));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.STRING, 1, 1),
                Token.of(Symbol.DOT, 2, 1),
                Token.of(Symbol.STRING, 3, 1),
                Token.of(Symbol.DOT, 4, 1)
        ), tokenize("4A.M."));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.SPACE, 1, 1),
                Token.of(Symbol.STRING, 2, 1),
                Token.of(Symbol.DOT, 3, 1),
                Token.of(Symbol.STRING, 4, 1),
                Token.of(Symbol.DOT, 5, 1)
        ), tokenize("4 A.M."));
    }

    @Test
    public void test12HourFormat() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.COLON, 1, 1),
                Token.of(Symbol.DIGITS, 2, 2),
                Token.of(Symbol.SPACE, 4, 1),
                Token.of(Symbol.STRING, 5, 2)
        ), tokenize("4:08 am"));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.COLON, 1, 1),
                Token.of(Symbol.DIGITS, 2, 2),
                Token.of(Symbol.STRING, 4, 1),
                Token.of(Symbol.DOT, 5, 1),
                Token.of(Symbol.STRING, 6, 1),
                Token.of(Symbol.DOT, 7, 1)
        ), tokenize("7:19P.M."));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.COLON, 1, 1),
                Token.of(Symbol.DIGITS, 2, 2),
                Token.of(Symbol.COLON, 4, 1),
                Token.of(Symbol.DIGITS, 5, 2),
                Token.of(Symbol.SPACE, 7, 1),
                Token.of(Symbol.STRING, 8, 2)
        ), tokenize("4:08:37 am"));
    }

    @Test
    public void test24HourFormat() {
        List<Token> expected = Arrays.asList(
                Token.of(Symbol.STRING, 0, 1),
                Token.of(Symbol.DIGITS, 1, 2),
                Token.of(Symbol.COLON, 3, 1),
                Token.of(Symbol.DIGITS, 4, 2)
        );
        assertEquals(expected, tokenize("T23:43"));
        assertEquals(expected, tokenize("t23:43"));
    }

    @Test
    public void negativeTimestamp() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.AT, 0, 1),
                Token.of(Symbol.MINUS, 1, 1),
                Token.of(Symbol.DIGITS, 2, 1)
        ), tokenize("@-1"));

        assertEquals(-15L, new DateTimeTokenizer("@-15").readLong(Token.of(Symbol.DIGITS, 1, 3)));
    }

    @Test
    public void testDateFormat() {
        assertThat(tokenize("8-6-21"))
                .containsExactly(
                        Token.of(Symbol.DIGITS, 0, 1),
                        Token.of(Symbol.MINUS, 1, 1),
                        Token.of(Symbol.DIGITS, 2, 1),
                        Token.of(Symbol.MINUS, 3, 1),
                        Token.of(Symbol.DIGITS, 4, 2)
                );
    }

    @Test
    public void xmlrpc() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 8),
                Token.of(Symbol.STRING, 8, 1),
                Token.of(Symbol.DIGITS, 9, 2),
                Token.of(Symbol.COLON, 11, 1),
                Token.of(Symbol.DIGITS, 12, 2),
                Token.of(Symbol.COLON, 14, 1),
                Token.of(Symbol.DIGITS, 15, 2)
        ), tokenize("20080701T22:38:07"));
    }

    @Test
    public void isoYearWeek() {
        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.DIGITS, 0, 4),
                        Token.of(Symbol.STRING, 4, 1),
                        Token.of(Symbol.DIGITS, 5, 2)
                ),
                tokenize("2008W27"));

        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.DIGITS, 0, 4),
                        Token.of(Symbol.MINUS, 4, 1),
                        Token.of(Symbol.STRING, 5, 1),
                        Token.of(Symbol.DIGITS, 6, 2)
                ),
                tokenize("2008-W27"));

    }

    @Test
    public void withTimeZone() {
        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.STRING, 0, 3),
                        Token.of(Symbol.PLUS, 3, 1),
                        Token.of(Symbol.DIGITS, 4, 4)
                ),
                tokenize("GMT+0700"));

        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.STRING, 0, 6),
                        Token.of(Symbol.STRING, 6, 1),
                        Token.of(Symbol.STRING, 7, 4)
                ),
                tokenize("Europe/Oslo"));
    }

    @Test
    public void fraction() {
        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.DIGITS, 0, 2),
                        Token.of(Symbol.DOT, 2, 1),
                        Token.of(Symbol.DIGITS, 3, 2)
                ),
                tokenize("17.03"));
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
        for (String tz : Arrays.asList("+0", "+00", "GMT")) {
            assertThat(parse(tz)).isEqualToIgnoringNanos(now().withZoneSameLocal(ZoneId.of(tz)));
        }
    }

    @Test
    public void hourAndMinuteWithMeridian() {
        assertEquals(now().withHour(4).withMinute(8).withSecond(0).withNano(0), parse("4:08 am"));
        assertEquals(now().withHour(19).withMinute(19).withSecond(0).withNano(0), parse("7:19P.M."));
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
    public void tokenizeTimezoneWithCorrection() {
        assertThat(tokenize("GMT+0700"))
                .containsExactly(
                        Token.of(Symbol.STRING, 0, 3),
                        Token.of(Symbol.PLUS, 3, 1),
                        Token.of(Symbol.DIGITS, 4, 4)
                );

        assertThat(tokenize("GMT-0700"))
                .containsExactly(
                        Token.of(Symbol.STRING, 0, 3),
                        Token.of(Symbol.MINUS, 3, 1),
                        Token.of(Symbol.DIGITS, 4, 4)
                );
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
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19GMT-6"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT+06:00")), parse("T19:19:19GMT+6"));
        assertEquals(withTime(4, 8, 37).withZoneSameLocal(ZoneId.of("UTC+02:00")), parse("T040837CEST"));
        assertEquals(withTime(4, 8, 37).withZoneSameLocal(ZoneId.of("UTC+02:00")), parse("T040837 CEST"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("UTC+02:00")), parse("T19:19:19CEST"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("UTC+10:30")), parse("T19:19:19ACDT"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT+06:00")), parse("T19:19:19GMT+6"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19GMT-06"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:00")), parse("T19:19:19-6"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-0630"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-630"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-630"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19-6:30"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:00")), parse("T19:19:19-6"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19 GMT-630"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19GMT-630"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19 -630"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-06:30")), parse("T19:19:19 -6:30"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-06:30")), parse("T19:19:19 GMT-6:30"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19 GMT-6"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-06:00")), parse("T19:19:19 GMT-06"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-07:00")), parse("T19:19:19 -07"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-07:00")), parse("T19:19:19 -07:00"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("-0700")), parse("T19:19:19 -0700"));
        assertEquals(withTime(19, 19, 19).withZoneSameLocal(ZoneId.of("GMT-0700")), parse("T19:19:19 GMT-0700"));
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
        Stream.of(new Pair<>("CEST", "UTC+02:00"), new Pair<>("ACDT", "UTC+10:30"))
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
    public void textualMonth() {
        assertThat(tokenize("30-June 2008"))
                .containsExactly(
                        Token.of(Symbol.DIGITS, 0, 2),
                        Token.of(Symbol.MINUS, 2, 1),
                        Token.of(Symbol.STRING, 3, 4),
                        Token.of(Symbol.SPACE, 7, 1),
                        Token.of(Symbol.DIGITS, 8, 4)
                );
    }

    @Test
    public void sandbox() {
        ZonedDateTime parse = parse("Dec 21  12:16");
    }

    @Test
    public void isoYearWithWeekAndWeekDay() {
        assertThat(parse("1997W011"))
                .isEqualToIgnoringNanos(withTime(0, 0, 0).withYear(1996).withMonth(12).withDayOfMonth(30));
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
                ZonedDateTime expected = now().withMonth(month.getValue());

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
                            .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(1));
                });
    }

    @Test
    public void customDates() {
        assertThat(parse("March 1879"))
                .isEqualToIgnoringNanos(now().withYear(1879).withMonth(3).withDayOfMonth(1));

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
                            .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(1));
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
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(1));

        assertThat(parse("June 2008"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(1));

        assertThat(parse("14 III 1879"))
                .isEqualToIgnoringNanos(now().withYear(1879).withMonth(3).withDayOfMonth(14));

        assertThat(parse("22DEC78"))
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(22));

        assertThat(parse("30-June 2008"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(30));

        assertThat(parse("22\t12.78"))
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(22));

        assertThat(parse("30.6.08"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(30));

        assertThat(parse("22.12.1978"))
                .isEqualToIgnoringNanos(now().withYear(1978).withMonth(12).withDayOfMonth(22));

        assertThat(parse("30-6-2008"))
                .isEqualToIgnoringNanos(now().withYear(2008).withMonth(6).withDayOfMonth(30));

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

        Stream.of(Pair.of("+1 week", 7), Pair.of("1 week", 7), Pair.of("2 weeks", 14),
                Pair.of("-2 weeks", -14))
                .forEach(pair -> {
                    assertThat(parse(pair.getA())).isEqualToIgnoringNanos(now().plusDays(pair.getB()));
                });

        assertThat(parse("+10 year +15 day -5 secs"))
                .isEqualToIgnoringNanos(now().plusYears(10).plusDays(15).minusSeconds(5));

    }

    @Test
    public void relativeWeekDays() {
        assertThat(parseWithMicro("-6 weekdays", ZonedDateTime.parse("2019-04-19T14:03:23+04:00[Asia/Yerevan]")))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("2019-04-11T00:00:00+04:00[Asia/Yerevan]"));

        assertThat(parseWithMicro("+1 weekdays", ZonedDateTime.parse("2019-04-19T14:03:23+04:00[Asia/Yerevan]")))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("2019-04-22T00:00:00+04:00[Asia/Yerevan]"));

        assertThat(parseWithMicro("+5 weekdays", ZonedDateTime.parse("2019-04-19T14:03:23+04:00[Asia/Yerevan]")))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("2019-04-26T00:00:00+04:00[Asia/Yerevan]"));

        assertThat(parseWithMicro("+7 weekdays", ZonedDateTime.parse("2019-04-19T14:03:23+04:00[Asia/Yerevan]")))
                .isEqualToIgnoringNanos(ZonedDateTime.parse("2019-04-30T00:00:00+04:00[Asia/Yerevan]"));
    }

    @Test
    public void shouldThrowExceptionWhenDateInvalid() {
        assertThatThrownBy(() -> parse("2009---01"));
    }
}