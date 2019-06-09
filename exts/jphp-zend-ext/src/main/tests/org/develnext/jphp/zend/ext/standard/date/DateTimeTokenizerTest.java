package org.develnext.jphp.zend.ext.standard.date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.Token.EOF;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.Symbol;
import org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.Token;
import org.junit.Test;

public class DateTimeTokenizerTest {
    private static List<Token> tokenize(final String time) {
        List<Token> tokens = new ArrayList<>();
        DateTimeTokenizer tokenizer = new DateTimeTokenizer(time);

        Token t;
        while ((t = tokenizer.next()) != EOF) tokens.add(t);

        return tokens;
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
    public void characterT() {
        assertThat(tokenize("15T15"))
                .containsExactly(
                        Token.of(Symbol.DIGITS, 0, 2),
                        Token.of(Symbol.STRING, 2, 1),
                        Token.of(Symbol.DIGITS, 3, 2)
                );
    }

    @Test
    public void comma() {
        assertThat(tokenize(",")).containsOnly(Token.of(Symbol.COMMA, 0, 1));
    }

    @Test
    public void textualMonthNameWithDayWithoutSpace() {
        assertThat(tokenize("Oct11"))
                .containsExactly(
                        Token.of(Symbol.STRING, 0, 3),
                        Token.of(Symbol.DIGITS, 3, 2)
                );
    }

    @Test
    public void relativeTimeWithWrongCharacter() {
        assertThat(tokenize("£61538461538 day"))
                .containsExactly(
                        Token.of(Symbol.STRING, 0, 1),
                        Token.of(Symbol.DIGITS, 1, 11),
                        Token.of(Symbol.SPACE, 12, 1),
                        Token.of(Symbol.STRING, 13, 3)
                );
    }
}