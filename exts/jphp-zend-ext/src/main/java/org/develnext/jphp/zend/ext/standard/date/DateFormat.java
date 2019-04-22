package org.develnext.jphp.zend.ext.standard.date;

import java.time.Duration;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.util.Locale;

import org.develnext.jphp.zend.ext.standard.DateConstants;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;

@Reflection.Ignore
public class DateFormat {
    private DateFormat() {
    }

    public static StringBuilder formatForDateFunction(Environment env, ZonedDateTime date, String format, StringBuilder sb) {
        Locale locale = env.getLocale();

        for (int i = 0, n = format.length(); i < n; i++) {
            char c = format.charAt(i);
            switch (c) {
                case 'Y':
                    sb.append(String.format(locale, "%04d", date.getYear()));
                    break;
                case 'y':
                    sb.append(String.format(locale, "%02d", date.getYear() % 100));
                    break;
                case 'm':
                    sb.append(String.format(locale, "%02d", date.getMonthValue()));
                    break;
                case 'M':
                    sb.append(date.getMonth().getDisplayName(TextStyle.SHORT, locale));
                    break;
                case 'd':
                    sb.append(String.format(locale, "%02d", date.getDayOfMonth()));
                    break;
                case 'j':
                    sb.append(date.getDayOfMonth());
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
                    sb.append(suffix);
                    break;
                }
                case 'w':
                    sb.append(date.getDayOfWeek().getValue());
                    break;
                case 'D':
                    sb.append(date.getDayOfWeek().getDisplayName(TextStyle.SHORT, locale));
                    break;
                case 'F':
                    sb.append(date.getMonth().getDisplayName(TextStyle.FULL, locale));
                    break;
                case 'e':
                    sb.append(date.getZone().getId());
                    break;
                case 'H':
                    sb.append(String.format(locale, "%02d", date.getHour()));
                    break;
                case 'i':
                    sb.append(String.format(locale, "%02d", date.getMinute()));
                    break;
                case 's':
                    sb.append(String.format(locale, "%02d", date.getSecond()));
                    break;
                case 'v':
                    sb.append(String.format(locale, "%03d", date.get(ChronoField.MILLI_OF_SECOND)));
                    break;
                case 'z':
                    sb.append(date.getDayOfYear() - 1);
                    break;
                case 'n':
                    sb.append(date.getMonthValue());
                    break;
                case 't':
                    sb.append(date.getMonth().length(Year.isLeap(date.getYear())));
                    break;
                case 'L':
                    sb.append(Year.isLeap(date.getYear()) ? 1 : 0);
                    break;
                case 'l':
                    sb.append(date.getDayOfWeek().getDisplayName(TextStyle.FULL, locale));
                    break;
                case 'a':
                    sb.append(date.getHour() >= 12 ? "pm" : "am");
                    break;
                case 'A':
                    sb.append(date.getHour() >= 12 ? "PM" : "AM");
                    break;
                case 'B': {
                    ZonedDateTime atUTC = date.withZoneSameInstant(ZoneId.of("UTC+01:00"));
                    int beats = (int) ((atUTC.get(ChronoField.SECOND_OF_MINUTE) +
                            (atUTC.get(ChronoField.MINUTE_OF_HOUR) * 60) + (atUTC.get(ChronoField.HOUR_OF_DAY) * 3600)) / 86.4);
                    sb.append(String.format(locale, "%03d", beats));
                    break;
                }
                case 'g': {
                    int hRem = date.getHour() % 12;
                    sb.append(hRem > 0 ? hRem : 12);
                    break;
                }
                case 'G': {
                    sb.append(date.getHour());
                    break;
                }
                case 'Z': {
                    sb.append(date.getOffset().getTotalSeconds());
                    break;
                }
                case 'U': {
                    sb.append(date.toEpochSecond());
                    break;
                }
                case 'O': {
                    Duration duration = Duration.ofSeconds(date.getOffset().getTotalSeconds());
                    long hours = duration.toHours();
                    long minutes = Math.abs(duration.toMinutes() % 60);
                    String offset = ((hours < 0) ? "-" : "+") + String.format(locale, "%02d", Math.abs(hours));

                    sb.append(offset).append(String.format(locale, "%02d", minutes));
                    break;
                }
                case 'P': {
                    sb.append(date.getOffset().toString());
                    break;
                }
                case 'r': {
                    formatForDateFunction(env, date, DateConstants.DATE_RFC2822.toString(), sb);
                    break;
                }
                case 'T': {
                    ZoneId zone = date.getZone();
                    if (zone instanceof ZoneOffset) {
                        sb.append("GMT");
                    }

                    formatForDateFunction(env, date, "O", sb);
                    break;
                }
                case '\\': {
                    if (i + 1 < n)
                        sb.append(format.charAt(++i));

                    break;
                }
                default: {
                    sb.append(c);
                }
                break;
            }
        }

        return sb;
    }

    public static String formatForDateFunction(Environment env, ZonedDateTime date, String format) {
        return formatForDateFunction(env, date, format, new StringBuilder()).toString();
    }
}
