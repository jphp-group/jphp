package org.develnext.jphp.zend.ext.standard.date;

import static java.util.Collections.singletonList;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeParser.tokenize;
import static org.junit.Assert.assertEquals;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class DateTimeParserTest {

    private static ZonedDateTime parse(String input) {
        return parseWithNano(input).withNano(0);
    }

    private static ZonedDateTime parseWithNano(String input) {
        return new DateTimeParser(input).parse();
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
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 4)), tokenize("A.m."));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 2)), tokenize("am"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 2)), tokenize("Am"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 3)), tokenize("A.m"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 3)), tokenize("A.M"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 4)), tokenize("A.M."));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 2)), tokenize("AM"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 2)), tokenize("pm"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 2)), tokenize("Pm"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 3)), tokenize("P.m"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 4)), tokenize("P.m."));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 4)), tokenize("p.M."));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 4)), tokenize("p.m."));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 3)), tokenize("p.m"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 3)), tokenize("P.M"));
        assertEquals(singletonList(Token.of(Symbol.MERIDIAN, 0, 4)), tokenize("P.M."));
    }

    @Test
    public void meridianWithHour() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.MERIDIAN, 1, 2)
        ), tokenize("4am"));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.MERIDIAN, 1, 2)
        ), tokenize("4pm"));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.MERIDIAN, 1, 4)
        ), tokenize("4A.M."));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.SPACE, 1, 1),
                Token.of(Symbol.MERIDIAN, 2, 4)
        ), tokenize("4 A.M."));
    }

    @Test
    public void test12HourFormat() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.COLON, 1, 1),
                Token.of(Symbol.DIGITS, 2, 2),
                Token.of(Symbol.SPACE, 4, 1),
                Token.of(Symbol.MERIDIAN, 5, 2)
        ), tokenize("4:08 am"));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.COLON, 1, 1),
                Token.of(Symbol.DIGITS, 2, 2),
                Token.of(Symbol.MERIDIAN, 4, 4)
        ), tokenize("7:19P.M."));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.COLON, 1, 1),
                Token.of(Symbol.DIGITS, 2, 2),
                Token.of(Symbol.COLON, 4, 1),
                Token.of(Symbol.DIGITS, 5, 2),
                Token.of(Symbol.SPACE, 7, 1),
                Token.of(Symbol.MERIDIAN, 8, 2)
        ), tokenize("4:08:37 am"));

        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 1),
                Token.of(Symbol.COLON, 1, 1),
                Token.of(Symbol.DIGITS, 2, 2),
                Token.of(Symbol.COLON, 4, 1),
                Token.of(Symbol.DIGITS, 5, 2),
                Token.of(Symbol.SPACE, 7, 1),
                Token.of(Symbol.MERIDIAN, 8, 2)
        ), tokenize("4:08:37 am"));
    }

    @Test
    public void test24HourFormat() {
        List<Token> expected = Arrays.asList(
                Token.of(Symbol.CHARACTER, 0, 1),
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
                Token.of(Symbol.DIGITS, 1, 2)
        ), tokenize("@-1"));

        assertEquals(-15L, new DateTimeTokenizer("@-15").readLong(Token.of(Symbol.DIGITS, 1, 3)));
    }

    @Test
    public void xmlrpc() {
        assertEquals(Arrays.asList(
                Token.of(Symbol.DIGITS, 0, 8),
                Token.of(Symbol.CHARACTER, 8, 1),
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
                        Token.of(Symbol.CHARACTER, 4, 1),
                        Token.of(Symbol.DIGITS, 5, 2)
                ),
                tokenize("2008W27"));

        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.DIGITS, 0, 4),
                        Token.of(Symbol.MINUS, 4, 1),
                        Token.of(Symbol.CHARACTER, 5, 1),
                        Token.of(Symbol.DIGITS, 6, 2)
                ),
                tokenize("2008-W27"));

    }

    @Test
    public void withTimeZone() {
        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.STRING, 0, 3),
                        Token.of(Symbol.DIGITS, 3, 5)
                ),
                tokenize("GMT+0700"));

        assertEquals(
                Arrays.asList(
                        Token.of(Symbol.STRING, 0, 6),
                        Token.of(Symbol.SLASH, 6, 1),
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
        assertEquals(ZonedDateTime.parse("2008-07-01T21:38:07+04:00[Asia/Yerevan]"), parse("20080701T9:38:07"));
        assertEquals(ZonedDateTime.parse("2008-01-02T00:00:00+04:00[Asia/Yerevan]"), parse("2008.002"));
        assertEquals(ZonedDateTime.parse("2008-01-02T00:00:00+04:00[Asia/Yerevan]"), parse("2008.002"));
    }

    @Test
    public void soap() {
        // microseconds
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.300+08:00"), parseWithNano("2008-07-01T22:35:17.3+08:00"));
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.030+08:00"), parseWithNano("2008-07-01T22:35:17.03+08:00"));
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.0030+08:00"), parseWithNano("2008-07-01T22:35:17.003+08:00"));

        // without timezone
        assertEquals(ZonedDateTime.parse("2008-07-01T22:35:17.300+04:00[Asia/Yerevan]"), parseWithNano("2008-07-01T22:35:17.3"));
    }

    @Test
    public void unixtimestamp() {
        assertEquals(ZonedDateTime.parse("1969-12-31T23:59:59Z[UTC]"), parse("@-1"));
        assertEquals(ZonedDateTime.parse("-34521-12-09T08:08:05Z[UTC]"), parse("@-1151515151515"));
    }
}