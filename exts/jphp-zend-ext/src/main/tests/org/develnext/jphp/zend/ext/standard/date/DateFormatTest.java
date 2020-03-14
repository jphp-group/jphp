package org.develnext.jphp.zend.ext.standard.date;

import static java.time.LocalDateTime.parse;
import static java.time.ZonedDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.develnext.jphp.zend.ext.standard.date.DateFormat.createFromFormat;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import php.runtime.common.Pair;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DateFormatTest {
    private static ZonedDateTime create(String format, String date) {
        return createFromFormat(format, date);
    }

    private static ZonedDateTime create(String format, String date, ZonedDateTime dateTime) {
        return createFromFormat(format, date, dateTime);
    }

    private static LocalDateTime local(String format, String date) {
        return createFromFormat(format, date).toLocalDateTime();
    }

    @Test
    public void dayOfMonth() {
        ZonedDateTime now = now();
        assertThat(create("d", "28", now)).isEqualToIgnoringNanos(now.withDayOfMonth(28));

        Stream.of(
                Pair.of("d", "5"),
                Pair.of("d", "05"),
                Pair.of("j", "05"),
                Pair.of("j", "5")
        )
                .map(pair -> create(pair.getA(), pair.getB(), now))
                .forEach(dateTime -> assertThat(dateTime).isEqualToIgnoringNanos(now.withDayOfMonth(5)));
    }

    @Test
    public void monthAndDay() {
        ZonedDateTime now = now();
        Stream.of(
                Pair.of("F d", "Dec 30"),
                Pair.of("F d", "December 30"),
                Pair.of("F d", "dec 30"),
                Pair.of("M d", "Dec 30"),
                Pair.of("M d", "December 30"),
                Pair.of("M d", "dec 30")
        )
                .map(pair -> create(pair.getA(), pair.getB(), now))
                .forEach(dateTime -> assertThat(dateTime)
                        .isEqualToIgnoringNanos(now.withMonth(12).withDayOfMonth(30)));

        assertThat(create("M d", "April 31", now))
                .isEqualToIgnoringNanos(now.withMonth(5).withDayOfMonth(1));
    }

    @Test
    public void complex() {
        assertSoftly(sa -> {
            sa.assertThat(local("U.u", "1475500799.176312"))
                    .isEqualTo("2016-10-03T13:19:59.176312");

            sa.assertThat(local("Y-m-d H:i:s.u", "2016-10-03 12:47:18.819313"))
                    .isEqualTo("2016-10-03T12:47:18.819313");

            sa.assertThat(create("m-d-Y H:i:s.u T", "03-15-2005 12:22:29.001001 PST"))
                    .isEqualTo("2005-03-15T12:22:29.001001-08:00");

            sa.assertThat(local("D., M# j, Y g:iA", "Thu., Nov. 29, 2012 5:00PM")).isEqualToIgnoringNanos(parse("2012-11-29T17:00:00"));
            sa.assertThat(local("Ymd\\THis\\Z", "20170920T091600Z")).isEqualTo("2017-09-20T09:16:00.000000");
            sa.assertAll();
        });
    }

    @Test
    public void dayOfWeekFormat() {
        LocalDateTime dateTime = local("D H i s", "Tue 0 00 00");
    }

    @Test
    public void timezones() {
        String[] timezoneFormats = {"e", "O", "P", "T"};
        List<Pair<String, String>> timezones = Arrays.asList(
                Pair.of("Atlantic/Azores", "Z[Atlantic/Azores]"),
                Pair.of("+02:00", "+02:00"),
                Pair.of("+0200", "+02:00"),
                Pair.of("+02", "+02:00"),
                Pair.of("+2", "+02:00"),
                Pair.of("EDT", "-04:00")

        );
        assertSoftly(sa -> {
            Stream.of(timezoneFormats).map(s -> "Y-m-d H:i:s " + s)
                    .forEach(format -> timezones.forEach(pair ->
                            sa.assertThat(create(format, "2016-10-03 12:47:18 " + pair.getA()))
                                    .isEqualTo("2016-10-03T12:47:18" + pair.getB()))
                    );
            sa.assertAll();
        });
    }

    @Test
    public void singleRandomSymbol() {
        assertSoftly(sa -> {
            StringCharacterIterator it = new StringCharacterIterator("abcdef?^ .:!2~@#$%^&*()_+<>?:|\"[]\n\t\f{}");
            while (it.current() != CharacterIterator.DONE) {
                assertThat(local("H:s:i?", "00:00:00" + it.next()).toLocalTime()).isEqualTo("00:00:00");
            }

            sa.assertAll();
        });
    }

    @Test
    public void oneOrMoreSymbols() {
        LocalDate now = LocalDate.now();
        assertSoftly(sa -> {
            String[] str = {"a", "ab", "abc", "?", "\n", "a%b@c$"};
            for (String s : str) {
                assertThat(local("Y-*-d", String.format("2008-%s-05", s)).toLocalDate())
                        .isEqualTo(now.withYear(2008).withDayOfMonth(5));

            }

            sa.assertAll();
        });
    }

    @Test
    public void dayNameAlwaysShouldTakePrecedence() {
        assertThat(local("!D d M Y", "Fri 17 may 2011"))
                .isEqualTo(local("!d M Y D", "17 may 2011 Fri"))
                .isEqualTo("2011-05-20T00:00:00.000000");
    }

    @Test
    public void negativeTimestamp() {
        assertThat(local("U", "-1"))
                .isEqualTo("1969-12-31T23:59:59");
    }
}