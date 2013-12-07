package ru.regenix.jphp.runtime.ext.core;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.annotation.Runtime;
import ru.regenix.jphp.compiler.common.compile.FunctionsContainer;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.LongMemory;
import ru.regenix.jphp.runtime.memory.StringMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.util.Printf;
import ru.regenix.jphp.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * TODO:
 *    - addcslashes
 *    - bin2hex ?
 *    - chunk_split
 */
public class StringFunctions extends FunctionsContainer {

    protected static char toUUChar(int d) {
        if (d == 0)
            return (char) 0x60;
        else
            return (char) (0x20 + (d & 0x3f));
    }

    protected static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    protected static char toUpperCase(char ch) {
        if (ch >= 'a' && ch <= 'z')
            return (char) ('A' + (ch - 'a'));
        else
            return ch;
    }

    protected static char toHexChar(int d) {
        d &= 0xf;

        if (d < 10)
            return (char) (d + '0');
        else
            return (char) (d - 10 + 'a');
    }

    protected static char toUpperHexChar(int d) {
        d &= 0xf;

        if (d < 10)
            return (char) (d + '0');
        else
            return (char) (d - 10 + 'A');
    }

    protected static int hexToDigit(char ch) {
        if ('0' <= ch && ch <= '9')
            return ch - '0';
        else if ('a' <= ch && ch <= 'f')
            return ch - 'a' + 10;
        else if ('A' <= ch && ch <= 'F')
            return ch - 'A' + 10;
        else
            return -1;
    }

    protected static int octToDigit(char ch) {
        if ('0' <= ch && ch <= '7')
            return ch - '0';
        else
            return -1;
    }

    public static Memory sprintf(Environment env, TraceInfo trace, String format, Memory... args){
        Printf printf = new Printf(env.getLocale(), format, args);
        String result = printf.toString();
        if (result == null){
            env.warning(trace, "Too few arguments");
            return Memory.NULL;
        } else
            return new StringMemory(result);
    }

    public static Memory vsprintf(Environment env, TraceInfo trace, String format, Memory array){
        if (array.isArray()){
            return sprintf(env, trace, format, array.toValue(ArrayMemory.class).values());
        } else
            return sprintf(env, trace, format, array);
    }

    public static int printf(Environment env, TraceInfo trace, String format, Memory... args){
        Memory str = sprintf(env, trace, format, args);
        if (str.isNull())
            return 0;
        else {
            String value = str.toString();
            env.echo(value);
            return value.length();
        }
    }

    @Runtime.Immutable
    public static String addslashes(String source) {
        StringBuilder sb = new StringBuilder();

        int length = source.length();
        for (int i = 0; i < length; i++) {
            char ch = source.charAt(i);

            switch (ch) {
                case 0x0:
                    sb.append("\\0");
                    break;
                case '\'':
                    sb.append("\\'");
                    break;
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                default:
                    sb.append(ch);
                    break;
            }
        }
        return sb.toString();
    }

    @Runtime.Immutable
    public static String trim(String s){
        return s.trim();
    }

    @Runtime.Immutable
    public static String ltrim(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return s.substring(i);
    }

    @Runtime.Immutable
    public static String rtrim(String s) {
        int i = s.length() - 1;
        while (i > 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0, i + 1);
    }

    @Runtime.Immutable
    public static String chop(String s){
        return rtrim(s);
    }

    @Runtime.Immutable
    public static String chr(int codePoint){
        return String.valueOf((char) codePoint);
    }

    @Runtime.Immutable
    public static int ord(String value){
        if (value.isEmpty())
            return 0;
        else
            return value.charAt(0);
    }

    @Runtime.Immutable
    public static String quotemeta(String string) {
        int len = string.length();
        StringBuilder sb = new StringBuilder(len * 5 / 4);

        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);

            switch (ch) {
                case '.': case '\\': case '+': case '*': case '?':
                case '[': case '^': case ']': case '(': case ')': case '$':
                    sb.append("\\");
                    sb.append(ch);
                    break;
                default:
                    sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static final char[] SOUNDEX_VALUES = "01230120022455012623010202".toCharArray();

    @Runtime.Immutable
    public static Memory soundex(String string) {
        int length = string.length();

        if (length == 0)
            return Memory.FALSE;

        StringBuilder sb = new StringBuilder();

        int count = 0;
        char lastCode = 0;

        for (int i = 0; i < length && count < 4; i++) {
            char ch = toUpperCase(string.charAt(i));

            if ('A' <= ch && ch <= 'Z') {
                char code = SOUNDEX_VALUES[ch - 'A'];

                if (count == 0) {
                    sb.append(ch);
                    count++;
                } else if (code != '0' && code != lastCode) {
                    sb.append(code);
                    count++;
                }

                lastCode = code;
            }
        }

        for (; count < 4; count++) {
            sb.append('0');
        }

        return new StringMemory(sb.toString());
    }

    @Runtime.Immutable
    public static String str_rot13(String string){
        int len = string.length();
        StringBuilder sb = new StringBuilder(len);

        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);

            if ('a' <= ch && ch <= 'z') {
                int off = ch - 'a';

                sb.append((char) ('a' + (off + 13) % 26));
            } else if ('A' <= ch && ch <= 'Z') {
                int off = ch - 'A';

                sb.append((char) ('A' + (off + 13) % 26));
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    public static String str_shuffle(String string) {
        char[] chars = string.toCharArray();

        int length = chars.length;
        for (int i = 0; i < length; i++) {
            int rand = MathFunctions.RANDOM.nextInt(length);

            char temp = chars[rand];
            chars[rand] = chars[i];
            chars[i] = temp;
        }
        return new String(chars);
    }

    public static Memory str_split(String string, int chunk) {
        ArrayMemory array = new ArrayMemory();

        if (string.isEmpty()) {
            array.add(new StringMemory(string));
            return array;
        }

        int strLen = string.length();
        for (int i = 0; i < strLen; i += chunk) {
            String value;

            if (i + chunk <= strLen) {
                value = string.substring(i, i + chunk);
            } else {
                value = string.substring(i);
            }

            array.add(new StringMemory(value));
        }

        return array;
    }

    public static Memory str_split(String string) {
        return str_split(string, 1);
    }

    @Runtime.Immutable
    public static int strcoll(String value1, String value2){
        int cmp = value1.compareTo(value2);

        if (cmp == 0)
            return 0;
        else if (cmp < 0)
            return -1;
        else
            return 1;
    }

    @Runtime.Immutable
    public static int strcmp(String value1, String value2){
        int aLen = value1.length();
        int bLen = value2.length();

        for (int i = 0; i < aLen && i < bLen; i++) {
            char chA = value1.charAt(i);
            char chB = value2.charAt(i);

            if (chA == chB)
                continue;

            if (chA < chB)
                return -1;
            else
                return 1;
        }

        if (aLen == bLen)
            return 0;
        else if (aLen < bLen)
            return -1;
        else
            return 1;
    }

    @Runtime.Immutable
    public static int strcasecmp(String value1, String value2){
        int aLen = value1.length();
        int bLen = value1.length();

        for (int i = 0; i < aLen && i < bLen; i++) {
            char chA = value1.charAt(i);
            char chB = value1.charAt(i);

            if (chA == chB)
                continue;

            if (Character.isUpperCase(chA))
                chA = Character.toLowerCase(chA);

            if (Character.isUpperCase(chB))
                chB = Character.toLowerCase(chB);

            if (chA < chB)
                return -1;
            else
                return 1;
        }

        if (aLen == bLen)
            return 0;
        else if (aLen < bLen)
            return -1;
        else
            return 1;
    }

    @Runtime.Immutable
    public static int strncasecmp(String value1, String value2, int len){
        int len1 = value1.length();
        int len2 = value2.length();
        String _value1 = len1 <= len ? value1 : value1.substring(0, len);
        String _value2 = len2 <= len ? value2 : value2.substring(0, len);

        return _value1.compareToIgnoreCase(_value2);
    }

    @Runtime.Immutable
    public static String nl2br(String value, boolean isXhtml){
        StringBuilder sb = new StringBuilder();
        String br = isXhtml ? "<br />" : "<br>";

        int length = value.length();
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            char next_ch = i >= length - 1 ? '\0' : value.charAt(i + 1);
            if ((ch == '\r' && next_ch == '\n') || (ch == '\n' && next_ch == '\r')){
                sb.append(br);
                sb.append(ch);
                sb.append(next_ch);
                i += 1;
                continue;
            }

            if (ch == '\r' || ch == '\n'){
                sb.append(br);
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    @Runtime.Immutable
    public static String nl2br(String value){
        return nl2br(value, true);
    }

    @Runtime.Immutable
    public static Memory implode(Environment env, Memory glue, Memory pieces){
        ArrayMemory array;
        String delimiter;
        if (glue.isArray()) {
            array = (ArrayMemory)glue;
            delimiter = pieces.toString();
        } else if (pieces.isArray()) {
            array = (ArrayMemory)pieces;
            delimiter = glue.toString();
        } else {
            env.warning("Argument must be an array");
            return Memory.NULL;
        }

        StringBuilder builder = new StringBuilder();
        int i = 0, size = array.size();
        for(Memory el : array){
            builder.append(el.toString());
            if (i != size - 1)
                builder.append(delimiter);
            i++;
        }

        return new StringMemory(builder.toString());
    }

    @Runtime.Immutable
    public static Memory implode(Environment env, Memory pieces){
        return implode(env, Memory.NULL, pieces);
    }

    @Runtime.Immutable
    public static Memory join(Environment env, Memory glue, Memory pieces){
        return implode(env, glue, pieces);
    }

    @Runtime.Immutable
    public static Memory join(Environment env, Memory pieces){
        return implode(env, Memory.NULL, pieces);
    }

    public static Memory explode(String delimiter, String string, int limit){
        if (limit == 0)
            limit = 1;

        String[] result;
        if (limit < 0){
            result = StringUtils.split(string, delimiter);
            result = Arrays.copyOfRange(result, 0, result.length + limit);
        } else
            result = StringUtils.split(string, delimiter, limit);
        return new ArrayMemory(result);
    }

    public static Memory explode(String delimiter, String string){
        return explode(delimiter, string, Integer.MAX_VALUE);
    }

    @Runtime.Immutable
    public static String lcfirst(String value){
        if (value.isEmpty())
            return "";

        return String.valueOf(Character.toLowerCase( value.charAt(0) )) + value.substring(1);
    }

    @Runtime.Immutable
    public static String ucfirst(String value){
        if (value.isEmpty())
            return "";
        return String.valueOf(Character.toUpperCase( value.charAt(0) )) + value.substring(1);
    }

    @Runtime.Immutable
    public static String ucwords(String value){
        char[] buffer = value.toCharArray();

        boolean prevSpace = true; // first char to Upper
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (Character.isSpaceChar(ch)){
                prevSpace = true;
                continue;
            }
            if (prevSpace){
                buffer[i] = Character.toUpperCase(ch);
                prevSpace = false;
            }
        }
        return new String(buffer);
    }

    private static final ThreadLocal<MessageDigest> md5Digest = new ThreadLocal<MessageDigest>(){
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private static final ThreadLocal<MessageDigest> sha1Digest = new ThreadLocal<MessageDigest>(){
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    @Runtime.Immutable
    public static String md5(Memory value)  {
        MessageDigest md = md5Digest.get();
        md.reset();
        md.update(value.getBinaryBytes());
        return DigestUtils.bytesToHex(md.digest());
    }

    @Runtime.Immutable
    public static String sha1(Memory value)  {
        MessageDigest md = sha1Digest.get();
        md.reset();
        md.update(value.getBinaryBytes());
        return DigestUtils.bytesToHex(md.digest());
    }

    @Runtime.Immutable
    public static Memory substr(String value, int start, int length){
        int strLen = value.length();
        if (start < 0)
            start = strLen + start;

        if (start < 0 || start >= strLen)
            return Memory.FALSE;

        if (length == 0)
            return Memory.CONST_EMPTY_STRING;

        int end;

        if (length < 0)
            end = strLen + length;
        else
            end = (strLen < length) ? strLen : start + length;

        if (end <= start)
            return Memory.FALSE;
        else if (strLen <= end)
            return new StringMemory(value.substring(start));
        else
            return new StringMemory(value.substring(start, end));
    }

    @Runtime.Immutable
    public static Memory substr(String value, int start){
        int length = value.length();
        if (start < 0)
            start = length + start;

        if (start < 0 || start > length)
            return Memory.FALSE;

        return new StringMemory(value.substring(start));
    }

    @Runtime.Immutable
    public static Memory substr_count(Environment env, TraceInfo trace,
                                      String haystack, String needle, int offset, Memory _length) {
        if (needle.isEmpty()) {
            env.warning(trace, "Empty substring");
            return Memory.FALSE;
        }

        int haystackLength = haystack.length();

        if (offset < 0) {
            env.warning(trace, "Offset should be greater than or equal to 0");
            return Memory.FALSE;
        }

        if (offset > haystackLength) {
            env.warning(trace, "Offset value %s exceeds string length", offset);
            return Memory.FALSE;
        }

        int length;
        int end;
        int needleLength = needle.length();

        if (_length != null){
            length = _length.toInteger();
            end = offset + length - 1;

            if (length <= 0) {
                env.warning(trace, "Length should be greater than 0");
                return Memory.FALSE;
            }

            if (length > (haystackLength - offset)) {
                env.warning(trace, "Length value %s exceeds string length", length);
                return Memory.FALSE;
            }
        } else {
            end = haystackLength - needleLength + 1;
        }

        int count = 0;

        if (needleLength == 1){
            char ch = needle.charAt(0);
            for(int i = offset; i < end; i++){
                if (ch == haystack.charAt(i))
                    count++;
            }
        } else {
            for (int i = offset; i < end; i++) {
                if (haystack.startsWith(needle, i)) {
                    count++;
                    i += needleLength;
                }
            }
        }

        return LongMemory.valueOf(count);
    }

    @Runtime.Immutable
    public static Memory substr_count(Environment env, TraceInfo trace, String haystack, String needle, int offset) {
        return substr_count(env, trace, haystack, needle, offset, null);
    }

    @Runtime.Immutable
    public static Memory substr_count(Environment env, TraceInfo trace, String haystack, String needle) {
        return substr_count(env, trace, haystack, needle, 0, null);
    }

    @Runtime.Immutable
    public static Memory substr_compare(Environment env, TraceInfo trace, String mainStr, String str, int offset,
                                       Memory lenV, boolean isCaseInsensitive) {
        int strLen = mainStr.length();

        if (lenV != null && lenV.toInteger() == 0)
            return Memory.FALSE;

        int len = lenV == null ? 0 : lenV.toInteger();

        if (strLen < offset) {
            env.warning(trace, "offset can not be greater than length of string");
            return Memory.FALSE;
        }

        if (len > strLen || len + offset > strLen) {
            return Memory.FALSE;
        }

        if (lenV == null)
            mainStr = substr(mainStr, offset).toString();
        else
            mainStr = substr(mainStr, offset, len).toString();

        str = lenV == null ? str : substr(str, 0, len).toString();

        if (isCaseInsensitive)
            return LongMemory.valueOf(strcasecmp(mainStr, str));
        else
            return LongMemory.valueOf(strcmp(mainStr, str));
    }

    @Runtime.Immutable
    public static Memory substr_compare(Environment env, TraceInfo trace, String mainStr, String str, int offset,
                                        Memory lenV) {
        return substr_compare(env, trace, mainStr, str, offset, lenV, false);
    }

    @Runtime.Immutable
    public static Memory substr_compare(Environment env, TraceInfo trace, String mainStr, String str, int offset) {
        return substr_compare(env, trace, mainStr, str, offset, null, false);
    }

    @Runtime.Immutable
    public static String strtolower(String string) {
        return string.toLowerCase();
    }

    @Runtime.Immutable
    public static String strtoupper(String string) {
        return string.toUpperCase();
    }

    @Runtime.Immutable
    public static String strrev(String string) {
        return StringUtils.reverse(string);
    }

    @Runtime.Immutable
    public static Memory strrchr(String haystack, char needle) {
        int i = haystack.lastIndexOf(needle);
        if (i > 0)
            return new StringMemory(haystack.substring(i));
        else
            return Memory.FALSE;
    }

    @Runtime.Immutable
    public static Memory strchr(String haystack, char needle, boolean beforeNeedle) {
        int i = haystack.indexOf(needle);
        if (i >= 0) {
            return new StringMemory(beforeNeedle ? haystack.substring(0, i) : haystack.substring(i));
        } else
            return Memory.FALSE;
    }

    @Runtime.Immutable
    public static Memory strchr(String haystack, char needle) {
        return strchr(haystack, needle, false);
    }

    @Runtime.Immutable
    public static Memory strstr(String haystack, char needle, boolean beforeNeedle) {
        return strchr(haystack, needle, beforeNeedle);
    }

    @Runtime.Immutable
    public static Memory strstr(String haystack, char needle) {
        return strchr(haystack, needle, false);
    }

    @Runtime.Immutable
    public static Memory strpos(Environment env, TraceInfo trace, String haystack, Memory needle, int offset){
        int haystackLen = haystack.length();
        if (offset < 0 || offset > haystackLen) {
            env.warning(trace, "Offset not contained in string");
            return Memory.FALSE;
        }

        if (haystackLen == 0)
            return Memory.FALSE;

        char ch = '\0';
        String search = null;
        if (needle.isString()){
            search = needle.toString();
            if (search.length() == 1) {
                ch = search.charAt(0);
                search = null;
            }
        } else {
            ch = needle.toChar();
        }

        int p;
        if (search == null){
            p = haystack.indexOf(ch, offset);
        } else {
            if (search.isEmpty()){
                env.warning(trace, "Empty needle");
                return Memory.FALSE;
            }
            p = haystack.indexOf(search, offset);
        }

        if (p < 0)
            return Memory.FALSE;
        else
            return LongMemory.valueOf(p);
    }

    @Runtime.Immutable
    public static Memory strpos(Environment env, TraceInfo trace, String haystack, Memory needle){
        return strpos(env, trace, haystack, needle, 0);
    }


    @Runtime.Immutable
    public static Memory strrpos(Environment env, TraceInfo trace, String haystack, Memory needle, int offset){
        int haystackLen = haystack.length();
        if (offset < 0 || offset > haystackLen) {
            env.warning(trace, "Offset not contained in string");
            return Memory.FALSE;
        }

        if (haystackLen == 0)
            return Memory.FALSE;

        char ch = '\0';
        String search = null;
        if (needle.isString()){
            search = needle.toString();
            if (search.length() == 1) {
                ch = search.charAt(0);
                search = null;
            }
        } else {
            ch = needle.toChar();
        }

        int p;
        if (search == null){
            p = haystack.lastIndexOf(ch, offset);
        } else {
            if (search.isEmpty()){
                env.warning(trace, "Empty needle");
                return Memory.FALSE;
            }
            p = haystack.lastIndexOf(search, offset);
        }

        if (p < 0)
            return Memory.FALSE;
        else
            return LongMemory.valueOf(p);
    }

    @Runtime.Immutable
    public static Memory strrpos(Environment env, TraceInfo trace, String haystack, Memory needle){
        return strrpos(env, trace, haystack, needle, 0);
    }


    @Runtime.Immutable
    public static Memory stripos(Environment env, TraceInfo trace, String haystack, Memory needle, int offset){
        int haystackLen = haystack.length();
        if (offset < 0 || offset > haystackLen) {
            env.warning(trace, "Offset not contained in string");
            return Memory.FALSE;
        }

        if (haystackLen == 0)
            return Memory.FALSE;

        char ch = '\0';
        String search = null;
        if (needle.isString()){
            search = needle.toString();
            if (search.length() == 1) {
                ch = Character.toUpperCase(search.charAt(0));
                search = null;
            }
        } else {
            ch = Character.toUpperCase(needle.toChar());
        }

        int p = -1;
        if (search == null){
            for(int i = offset; i < haystackLen; i++){
                if (Character.toUpperCase(haystack.charAt(i)) == ch){
                    p = i;
                    break;
                }
            }
        } else {
            if (search.isEmpty()){
                env.warning(trace, "Empty needle");
                return Memory.FALSE;
            }

            p = StringUtils.indexOfIgnoreCase(haystack, search, offset);
        }

        if (p < 0)
            return Memory.FALSE;
        else
            return LongMemory.valueOf(p);
    }

    @Runtime.Immutable
    public static Memory stripos(Environment env, TraceInfo trace, String haystack, Memory needle){
        return stripos(env, trace, haystack, needle, 0);
    }



}
