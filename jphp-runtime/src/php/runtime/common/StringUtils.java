package php.runtime.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

final public class StringUtils {

    public static final int INDEX_NOT_FOUND = -1;
    private final static String[] EMPTY_STRING_ARRAY = new String[]{};

    public static String repeat(char ch, int repeat) {
        char[] buf = new char[repeat];
        for (int i = repeat - 1; i >= 0; i--) {
            buf[i] = ch;
        }
        return new String(buf);
    }

    public static String join(Iterator<?> iterator, char separator) {
        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? "" : first.toString();
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            buf.append(separator);
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }

        return buf.toString();
    }

    public static String join(Iterator<?> iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null ? "" : first.toString();
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String join(Iterable<?> iterable, char separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String join(Iterable<?> iterable, String separator) {
        if (iterable == null) {
            return null;
        }
        return join(iterable.iterator(), separator);
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        // endIndex - startIndex > 0:   Len = NofStrings *(len(firstString) + len(separator))
        //           (Assuming that all Strings are roughly equally long)
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    public static String join(Object[] array, char separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        int noOfItems = endIndex - startIndex;
        if (noOfItems <= 0) {
            return "";
        }

        StringBuilder buf = new StringBuilder(noOfItems * 16);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    private static String[] splitWorker(String str, char separatorChar, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<String>();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    private static String[] splitWorker(String str, String separatorChars, int max, boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = new ArrayList<String>();
        int sizePlus1 = 1;
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
                list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    public static String[] split(String str, char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    public static String[] split(String str, String separatorChars, int max) {
        return str.split(Pattern.quote(separatorChars), max);
    }

    public static String[] split(String toSplit, String delimiter) {
        return toSplit.split(Pattern.quote(delimiter));
    }

    public static String reverse(String str) {
        if (str == null) {
            return null;
        }
        return new StringBuilder(str).reverse().toString();
    }

    static boolean regionMatches(CharSequence cs, boolean ignoreCase, int thisStart,
                                 CharSequence substring, int start, int length)    {
        if (cs instanceof String && substring instanceof String) {
            return ((String) cs).regionMatches(ignoreCase, thisStart, (String) substring, start, length);
        } else {
            // TODO: Implement rather than convert to String
            return cs.toString().regionMatches(ignoreCase, thisStart, substring.toString(), start, length);
        }
    }

    public static int indexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (startPos < 0) {
            startPos = 0;
        }
        int endLimit = str.length() - searchStr.length() + 1;
        if (startPos > endLimit) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }
        for (int i = startPos; i < endLimit; i++) {
            if (regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static int lastIndexOfIgnoreCase(CharSequence str, CharSequence searchStr, int startPos) {
        if (str == null || searchStr == null) {
            return INDEX_NOT_FOUND;
        }
        if (startPos > str.length() - searchStr.length()) {
            startPos = str.length() - searchStr.length();
        }
        if (startPos < 0) {
            return INDEX_NOT_FOUND;
        }
        if (searchStr.length() == 0) {
            return startPos;
        }

        for (int i = startPos; i >= 0; i--) {
            if (regionMatches(str, true, i, searchStr, 0, searchStr.length())) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static boolean isValidClassName(String name){
        int length = name.length();
        if (length == 0)
            return false;

        for(int i = 0; i < length; i++){
            char ch = name.charAt(i);

            if (Character.isLetter(ch) || (i != 0 && Character.isDigit(ch)) || ch == '\\' || ch == '_')
                continue;

            return false;
        }
        return true;
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
    }

    public static String replace(String inString, String oldPattern, String newPattern) {
        return replace(inString, oldPattern, newPattern, false, null);
    }

    public static String replaceIgnoreCase(String inString, String oldPattern, String newPattern) {
        return replace(inString, oldPattern, newPattern, true, null);
    }

    public static String replace(String inString, String oldPattern, String newPattern, boolean ignoreCase, AtomicLong count) {
        if (!hasLength(inString) || !hasLength(oldPattern) || newPattern == null) {
            return inString;
        }

        if (count != null) count.set(0);

        StringBuilder sb = new StringBuilder();
        int pos = 0; // our position in the old string
        int index = ignoreCase
                ? inString.toLowerCase().indexOf(oldPattern.toLowerCase())
                : inString.indexOf(oldPattern);

        // the index of an occurrence we've found, or -1
        int patLen = oldPattern.length();

        while (index >= 0) {
            sb.append(inString.substring(pos, index));
            sb.append(newPattern);
            pos = index + patLen;
            index = ignoreCase
                ? inString.toLowerCase().indexOf(oldPattern.toLowerCase(), pos)
                : inString.indexOf(oldPattern, pos);

            if (count != null) count.incrementAndGet();
        }

        sb.append(inString.substring(pos));
        // remember to append any characters to the right of a match
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isBlank(String str) {
        return str == null || str.isEmpty() || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
