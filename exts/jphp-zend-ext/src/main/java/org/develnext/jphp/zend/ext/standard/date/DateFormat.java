package org.develnext.jphp.zend.ext.standard.date;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Formatter;

import org.develnext.jphp.zend.ext.standard.DateConstants;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;

@Reflection.Ignore
public class DateFormat {
    private DateFormat() {
    }

    private static Formatter formatForDateFunction(Environment env, ZonedDateTime date, String format, Formatter fmt)
            throws IOException {

        Appendable out = fmt.out();
        for (int i = 0, n = format.length(); i < n; i++) {
            char c = format.charAt(i);

            switch (c) {
                case 'Y':
                    fmt.format("%04d", date.getYear());
                    break;
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
                case 'e':
                    out.append(date.getZone().getId());
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
                    int beats = (int) ((atUTC.get(ChronoField.SECOND_OF_MINUTE) +
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
                case 'U': {
                    out.append(Long.toString(date.toEpochSecond()));
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
                case 'r': {
                    formatForDateFunction(env, date, DateConstants.DATE_RFC2822.toString(), fmt);
                    break;
                }
                case 'T': {
                    if (date.getZone() instanceof ZoneOffset) {
                        out.append("GMT");
                        formatForDateFunction(env, date, "O", fmt);
                    } else {
                        String str = ZoneIdFactory.aliasFor(date);
                        if (str != null) {
                            out.append(str);
                        } else {
                            Duration duration = Duration.ofSeconds(date.getOffset().getTotalSeconds());
                            long hours = duration.toHours();
                            out.append(hours < 0 ? '-' : '+');
                            fmt.format("%02d", Math.abs(hours));

                            long minutes =  Math.abs(duration.toMinutes() % 60);
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

    public static String formatForDateFunction(Environment env, ZonedDateTime date, String format) {
        try {
            Formatter formatter = new Formatter(new StringBuilder(), env.getLocale());
            return formatForDateFunction(env, date, format, formatter).toString();
        } catch (IOException e) {
            // never happen, we use StringBuilder
            throw new UncheckedIOException(e);
        }
    }
}
