package org.develnext.jphp.zend.ext.standard;

import org.develnext.jphp.zend.ext.support.NaturalOrderComparator;
import php.runtime.Memory;
import php.runtime.annotation.Runtime.Immutable;
import php.runtime.annotation.Runtime.Reference;
import php.runtime.common.DigestUtils;
import php.runtime.common.Messages;
import php.runtime.common.StringUtils;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.TodoException;
import php.runtime.ext.core.MathFunctions;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.*;
import php.runtime.util.PrintF;
import php.runtime.util.SScanF;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.CRC32;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * TODO:
 * - addcslashes
 * - bin2hex ?
 * - chunk_split
 */
public class StringFunctions extends FunctionsContainer {
    private static final DecimalFormatSymbols DEFAULT_DECIMAL_FORMAT_SYMBOLS;

    private static ArrayMemory HTML_ENTITIES;
    private static ArrayMemory HTML_SPECIALCHARS;

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

    public static Memory sscanf(Environment env, TraceInfo trace, String string, String format,
                                @Reference Memory... args) {
        SScanF.Segment[] formatArray = SScanF.parse(env, trace, format);

        int strlen = string.length();
        int sIndex = 0;

        boolean isReturnArray = args == null || args.length == 0;
        int argIndex = 0;

        if (strlen == 0) {
            return isReturnArray ? Memory.NULL : Memory.CONST_INT_M1;
        }

        ArrayMemory array = new ArrayMemory();

        for (int i = 0; i < formatArray.length; i++) {
            SScanF.Segment segment = formatArray[i];
            Memory var;

            if (!segment.isAssigned()) {
                var = null;
            } else if (isReturnArray) {
                var = array;
            } else {
                if (argIndex < args.length) {
                    var = args[argIndex];

                    if (sIndex < strlen)
                        argIndex++;

                } else {
                    env.warning(trace, "sscanf(): not enough variables passed in");
                    var = new ReferenceMemory();
                }
            }

            if (!(var instanceof ReferenceMemory))
                var = new ReferenceMemory(var);

            sIndex = segment.apply(string, strlen, sIndex, (ReferenceMemory) var, isReturnArray);

            if (sIndex < 0) {
                if (isReturnArray)
                    return sscanfFillNull(array, formatArray, i);
                else
                    return LongMemory.valueOf(argIndex);
            }
        }

        return sscanfReturn(env, trace, array, args, argIndex, isReturnArray, false);
    }

    private static Memory sscanfReturn(Environment env, TraceInfo trace,
                                       ArrayMemory array,
                                       Memory[] args,
                                       int argIndex,
                                       boolean isReturnArray,
                                       boolean isWarn) {
        if (isReturnArray)
            return array;
        else {
            if (isWarn && args != null && argIndex != args.length)
                env.warning(trace, "%s vars passed in but saw only %s '%' args", args.length, argIndex);

            return LongMemory.valueOf(argIndex);
        }
    }

    private static Memory sscanfFillNull(ArrayMemory array, SScanF.Segment[] formatArray, int fIndex) {
        for (; fIndex < formatArray.length; fIndex++) {
            SScanF.Segment segment = formatArray[fIndex];
            if (segment.isAssigned())
                array.add(Memory.NULL);
        }
        return array;
    }


    public static Memory sprintf(Environment env, TraceInfo trace, String format, Memory... args) {
        PrintF printF = new PrintF(env.getLocale(), format, args);
        String result = printF.toString();
        if (result == null) {
            env.warning(trace, "Too few arguments");
            return Memory.NULL;
        } else
            return new StringMemory(result);
    }

    public static Memory vsprintf(Environment env, TraceInfo trace, String format, Memory array) {
        if (array.isArray()) {
            return sprintf(env, trace, format, array.toValue(ArrayMemory.class).values());
        } else
            return sprintf(env, trace, format, array);
    }

    public static int printf(Environment env, TraceInfo trace, String format, Memory... args) {
        Memory str = sprintf(env, trace, format, args);
        if (str.isNull())
            return 0;
        else {
            String value = str.toString();
            env.echo(value);
            return value.length();
        }
    }

    public static int vprintf(Environment env, TraceInfo trace, String format, Memory array) {
        if (array.isArray()) {
            return printf(env, trace, format, array.toValue(ArrayMemory.class).values());
        } else
            return printf(env, trace, format, array);
    }

    /**
     * Parses the cslashes bitmap returning an actual bitmap.
     *
     * @param charset the bitmap string
     * @return the actual bitmap
     */
    private static boolean[] parseCharsetBitmap(Environment env, TraceInfo trace, String charset) {
        boolean[] bitmap = new boolean[256];

        int length = charset.length();
        for (int i = 0; i < length; i++) {
            char ch = charset.charAt(i);

            // XXX: the bitmap eventual might need to deal with unicode
            if (ch >= 256)
                continue;

            bitmap[ch] = true;

            if (length <= i + 3)
                continue;

            if (charset.charAt(i + 1) != '.' || charset.charAt(i + 2) != '.')
                continue;

            char last = charset.charAt(i + 3);

            if (last < ch) {
                env.warning(trace, "character set range is invalid: %s..%s", ch, last);
                continue;
            }

            i += 3;
            for (; ch <= last; ch++) {
                bitmap[ch] = true;
            }

            // TODO: handling of '@'?
        }

        return bitmap;
    }

    @Immutable
    public static String addcslashes(Environment env, TraceInfo trace, String source, String characters) {
        boolean[] bitmap = parseCharsetBitmap(env, trace, characters);

        int length = source.length();
        StringBuilder sb = new StringBuilder(length * 5 / 4);

        for (int i = 0; i < length; i++) {
            char ch = source.charAt(i);

            if (ch >= 256 || !bitmap[ch]) {
                sb.append(ch);
                continue;
            }

            switch (ch) {
                case 0x07:
                    sb.append("\\a");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case 0xb:
                    sb.append("\\v");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                default:
                    if (ch < 0x20 || ch >= 0x7f) {
                        // save as octal
                        sb.append("\\");
                        sb.append((char) ('0' + ((ch >> 6) & 7)));
                        sb.append((char) ('0' + ((ch >> 3) & 7)));
                        sb.append((char) ('0' + ((ch) & 7)));
                        break;
                    } else {
                        sb.append("\\");
                        sb.append(ch);
                        break;
                    }
            }
        }

        return sb.toString();
    }

    @Immutable
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

    @Immutable
    public static String bin2hex(Memory _value) {
        String value = _value.toBinaryString();
        StringBuilder sb = new StringBuilder();

        int ch;
        int length = value.length();
        for (int i = 0; i < length; i++) {
            ch = value.charAt(i);
            int d = (ch >> 4) & 0xf;

            if (d < 10)
                sb.append((char) (d + '0'));
            else
                sb.append((char) (d + 'a' - 10));

            d = (ch) & 0xf;

            if (d < 10)
                sb.append((char) (d + '0'));
            else
                sb.append((char) (d + 'a' - 10));
        }

        return sb.toString();
    }

    @Immutable
    public static String hex2bin(Memory _s) {
        String s = _s.toBinaryString();
        StringBuilder sb = new StringBuilder();

        int len = s.length();

        for (int i = 0; i + 1 < len; i += 2) {
            int d1 = hexDigit(s.charAt(i));
            int d2 = hexDigit(s.charAt(i + 1));

            int d = d1 * 16 + d2;
            sb.append((char) d);
        }

        return sb.toString();
    }

    private static int hexDigit(int c) {
        if ('0' <= c && c <= '9') {
            return c - '0';
        } else if ('a' <= c && c <= 'f') {
            return c - 'a' + 10;
        } else if ('A' <= c && c <= 'F') {
            return c - 'A' + 10;
        } else {
            return 0;
        }
    }

    @Immutable
    public static Memory chunk_split(Environment env, TraceInfo trace, String body, int chunkLen, String end) {
        if (chunkLen < 1) {
            env.warning(trace, "chunk_split(): Chunk length should be greater than zero");
            return Memory.FALSE;
        }

        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (; i + chunkLen <= body.length(); i += chunkLen) {
            sb.append(body.substring(i, i + chunkLen));
            sb.append(end);
        }

        if (i < body.length()) {
            sb.append(body.substring(i));
            sb.append(end);
        }

        return new StringMemory(sb.toString());
    }

    @Immutable
    public static Memory chunk_split(Environment env, TraceInfo trace, String body, int chunkLen) {
        return chunk_split(env, trace, body, chunkLen, "\r\n");
    }

    @Immutable
    public static Memory chunk_split(Environment env, TraceInfo trace, String body) {
        return chunk_split(env, trace, body, 76);
    }

    @Immutable
    public static Memory convert_cyr_string(Environment env, TraceInfo trace, String str, String from, String to) {
        throw new TodoException();
    }

    @Immutable
    public static String trim(String s) {
        return s.trim();
    }

    @Immutable
    public static String trim(String s, String charsetList) {
        return rtrim(ltrim(s, charsetList), charsetList);
    }

    @Immutable
    public static String ltrim(String s) {
        int i = 0;
        while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
            i++;
        }
        return i >= s.length() ? "" : s.substring(i);
    }

    @Immutable
    public static String ltrim(String s, String charsetList) {
        int i = 0;
        while (i < s.length() && charsetList.indexOf(s.charAt(i)) > -1) {
            i++;
        }
        return i >= s.length() ? "" : s.substring(i);
    }

    @Immutable
    public static String rtrim(String s) {
        int i = s.length() - 1;
        while (i > 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return i <= 0 ? "" : s.substring(0, i + 1);
    }

    @Immutable
    public static String rtrim(String s, String charsetList) {
        int i = s.length() - 1;
        while (i > 0 && charsetList.indexOf(s.charAt(i)) > -1) {
            i--;
        }
        return i <= 0 ? "" : s.substring(0, i + 1);
    }

    @Immutable
    public static String chop(String s) {
        return rtrim(s);
    }

    @Immutable
    public static String quotemeta(String string) {
        int len = string.length();
        StringBuilder sb = new StringBuilder(len * 5 / 4);

        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);

            switch (ch) {
                case '.':
                case '\\':
                case '+':
                case '*':
                case '?':
                case '[':
                case '^':
                case ']':
                case '(':
                case ')':
                case '$':
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

    @Immutable
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

    @Immutable
    public static String str_rot13(String string) {
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

    @Immutable
    public static int strcoll(String value1, String value2) {
        int cmp = value1.compareTo(value2);

        if (cmp == 0)
            return 0;
        else if (cmp < 0)
            return -1;
        else
            return 1;
    }

    @Immutable
    public static int strcmp(String value1, String value2) {
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

    @Immutable
    public static int strncmp(String value1, String value2, int len) {
        int len1 = value1.length();
        int len2 = value2.length();
        String _value1 = len1 <= len ? value1 : value1.substring(0, len);
        String _value2 = len2 <= len ? value2 : value2.substring(0, len);

        return _value1.compareTo(_value2);
    }

    @Immutable
    public static int strcasecmp(String value1, String value2) {
        return value1.compareToIgnoreCase(value2);
    }

    @Immutable
    public static int strncasecmp(String value1, String value2, int len) {
        int len1 = value1.length();
        int len2 = value2.length();
        String _value1 = len1 <= len ? value1 : value1.substring(0, len);
        String _value2 = len2 <= len ? value2 : value2.substring(0, len);

        return _value1.compareToIgnoreCase(_value2);
    }

    /*@Runtime.Immutable
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
    }*/

    @Immutable
    public static String nl2br(String value) {
        return nl2br(value, true);
    }

    @Immutable
    public static Memory implode(Environment env, TraceInfo trace, Memory glue, Memory pieces) {
        ArrayMemory array;
        String delimiter;
        if (glue.isArray()) {
            array = (ArrayMemory) glue;
            delimiter = pieces.toString();
        } else if (pieces.isArray()) {
            array = (ArrayMemory) pieces;
            delimiter = glue.toString();
        } else {
            env.warning(trace, "Argument must be an array");
            return Memory.NULL;
        }

        StringBuilder builder = new StringBuilder();
        int i = 0, size = array.size();
        for (Memory el : array) {
            builder.append(el.toString());
            if (i != size - 1)
                builder.append(delimiter);
            i++;
        }

        return new StringMemory(builder.toString());
    }

    @Immutable
    public static Memory implode(Environment env, TraceInfo trace, Memory pieces) {
        return implode(env, trace, Memory.NULL, pieces);
    }

    @Immutable
    public static Memory join(Environment env, TraceInfo trace, Memory glue, Memory pieces) {
        return implode(env, trace, glue, pieces);
    }

    @Immutable
    public static Memory join(Environment env, TraceInfo trace, Memory pieces) {
        return implode(env, trace, Memory.NULL, pieces);
    }

    public static Memory explode(String delimiter, String string, int limit) {
        if (limit == 0)
            limit = 1;

        String[] result;
        if (limit < 0) {
            result = StringUtils.split(string, delimiter);
            result = Arrays.copyOfRange(result, 0, result.length + limit);
        } else
            result = StringUtils.split(string, delimiter, limit);

        return ArrayMemory.ofStrings(result);
    }

    public static Memory explode(String delimiter, String string) {
        return explode(delimiter, string, Integer.MAX_VALUE);
    }

    @Immutable
    public static String lcfirst(String value) {
        if (value.isEmpty())
            return "";

        return String.valueOf(Character.toLowerCase(value.charAt(0))) + value.substring(1);
    }

    @Immutable
    public static String ucfirst(String value) {
        if (value.isEmpty())
            return "";
        return String.valueOf(Character.toUpperCase(value.charAt(0))) + value.substring(1);
    }

    @Immutable
    public static String ucwords(String value) {
        char[] buffer = value.toCharArray();

        boolean prevSpace = true; // first char to Upper
        for (int i = 0; i < buffer.length; i++) {
            char ch = buffer[i];
            if (Character.isSpaceChar(ch)) {
                prevSpace = true;
                continue;
            }
            if (prevSpace) {
                buffer[i] = Character.toUpperCase(ch);
                prevSpace = false;
            }
        }
        return new String(buffer);
    }

    private static final ThreadLocal<MessageDigest> md5Digest = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    private static final ThreadLocal<MessageDigest> sha1Digest = new ThreadLocal<MessageDigest>() {
        @Override
        protected MessageDigest initialValue() {
            try {
                return MessageDigest.getInstance("SHA1");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    };

    public static Memory md5(Environment env, Memory value, boolean rawOutput) {
        MessageDigest md = md5Digest.get();
        md.reset();
        md.update(value.getBinaryBytes(env.getDefaultCharset()));
        if (rawOutput)
            return new BinaryMemory(md.digest());
        else
            return new StringMemory(DigestUtils.bytesToHex(md.digest()));
    }

    public static Memory md5(Environment env, Memory value) {
        return md5(env, value, false);
    }

    public static Memory md5_file(Environment env, TraceInfo trace, String fileName) {
        return md5_file(env, trace, fileName, false);
    }

    public static Memory md5_file(Environment env, TraceInfo trace, String fileName, boolean rawOutput) {
        try {
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(fileName));
            try {
                MessageDigest md = md5Digest.get();
                md.reset();

                int len;
                byte[] buff = new byte[1024];
                while ((len = reader.read(buff)) > 0) {
                    md.update(buff, 0, len);
                }

                if (rawOutput)
                    return new BinaryMemory(md.digest());
                else
                    return new StringMemory(DigestUtils.bytesToHex(md.digest()));
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            env.warning(trace, "md5_file(): " + Messages.ERR_FILE_NOT_FOUND.fetch(fileName));
            return Memory.FALSE;
        } catch (IOException e) {
            env.warning(trace, "md5_file(): " + e.getMessage());
            return Memory.FALSE;
        }
    }

    public static Memory sha1(Environment env, Memory value, boolean rawOutput) {
        MessageDigest md = sha1Digest.get();
        md.reset();
        md.update(value.getBinaryBytes(env.getDefaultCharset()));
        if (rawOutput) {
            return new BinaryMemory(md.digest());
        } else {
            return new StringMemory(DigestUtils.bytesToHex(md.digest()));
        }
    }

    @Immutable
    public static Memory sha1(Environment env, Memory value) {
        return sha1(env, value, false);
    }

    public static Memory sha1_file(Environment env, TraceInfo trace, String fileName) {
        return sha1_file(env, trace, fileName, false);
    }

    public static Memory sha1_file(Environment env, TraceInfo trace, String fileName, boolean rawOutput) {
        try {
            BufferedInputStream reader = new BufferedInputStream(new FileInputStream(fileName));
            try {
                MessageDigest md = sha1Digest.get();
                md.reset();

                int len;
                byte[] buff = new byte[1024];
                while ((len = reader.read(buff)) > 0) {
                    md.update(buff, 0, len);
                }

                if (rawOutput)
                    return new BinaryMemory(md.digest());
                else
                    return new StringMemory(DigestUtils.bytesToHex(md.digest()));
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            env.warning(trace, "sha1_file(): " + Messages.ERR_FILE_NOT_FOUND.fetch(fileName));
            return Memory.FALSE;
        } catch (IOException e) {
            env.warning(trace, "sha1_file(): " + e.getMessage());
            return Memory.FALSE;
        }
    }

    @Immutable
    public static Memory substr(String value, int start, int length) {
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

    @Immutable
    public static Memory substr(String value, int start) {
        int length = value.length();
        if (start < 0)
            start = length + start;

        if (start < 0 || start > length)
            return Memory.FALSE;

        return new StringMemory(value.substring(start));
    }

    @Immutable
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

        if (_length != null) {
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

        if (needleLength == 1) {
            char ch = needle.charAt(0);
            for (int i = offset; i < end; i++) {
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

    @Immutable
    public static Memory substr_count(Environment env, TraceInfo trace, String haystack, String needle, int offset) {
        return substr_count(env, trace, haystack, needle, offset, null);
    }

    @Immutable
    public static Memory substr_count(Environment env, TraceInfo trace, String haystack, String needle) {
        return substr_count(env, trace, haystack, needle, 0, null);
    }

    @Immutable
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

    @Immutable
    public static Memory substr_compare(Environment env, TraceInfo trace, String mainStr, String str, int offset,
                                        Memory lenV) {
        return substr_compare(env, trace, mainStr, str, offset, lenV, false);
    }

    @Immutable
    public static Memory substr_compare(Environment env, TraceInfo trace, String mainStr, String str, int offset) {
        return substr_compare(env, trace, mainStr, str, offset, null, false);
    }

    @Immutable
    public static String strtolower(String string) {
        return string.toLowerCase();
    }

    @Immutable
    public static String strtoupper(String string) {
        return string.toUpperCase();
    }

    @Immutable
    public static String strrev(String string) {
        return StringUtils.reverse(string);
    }

    @Immutable
    public static Memory strrchr(String haystack, char needle) {
        int i = haystack.lastIndexOf(needle);
        if (i > 0)
            return new StringMemory(haystack.substring(i));
        else
            return Memory.FALSE;
    }

    @Immutable
    public static Memory strchr(String haystack, char needle, boolean beforeNeedle) {
        int i = haystack.indexOf(needle);
        if (i >= 0) {
            return new StringMemory(beforeNeedle ? haystack.substring(0, i) : haystack.substring(i));
        } else
            return Memory.FALSE;
    }

    @Immutable
    public static Memory strchr(String haystack, char needle) {
        return strchr(haystack, needle, false);
    }

    @Immutable
    public static Memory strstr(String haystack, char needle, boolean beforeNeedle) {
        return strchr(haystack, needle, beforeNeedle);
    }

    @Immutable
    public static Memory strstr(String haystack, char needle) {
        return strchr(haystack, needle, false);
    }

    @Immutable
    public static Memory strpos(Environment env, TraceInfo trace, String haystack, Memory needle, int offset) {
        int haystackLen = haystack.length();
        if (offset < 0 || offset > haystackLen) {
            env.warning(trace, "strpos(): Offset not contained in string");
            return Memory.FALSE;
        }

        if (haystackLen == 0)
            return Memory.FALSE;

        char ch = '\0';
        String search = null;
        if (needle.isString()) {
            search = needle.toString();
            if (search.length() == 1) {
                ch = search.charAt(0);
                search = null;
            }
        } else {
            ch = needle.toChar();
        }

        int p;
        if (search == null) {
            p = haystack.indexOf(ch, offset);
        } else {
            if (search.isEmpty()) {
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

    @Immutable
    public static Memory strpos(Environment env, TraceInfo trace, String haystack, Memory needle) {
        return strpos(env, trace, haystack, needle, 0);
    }

    @Immutable
    public static Memory strrpos(Environment env, TraceInfo trace, String haystack, Memory needle, Memory offsetV) {
        int haystackLen = haystack.length();

        int offset;

        if (offsetV == null) {
            offset = haystack.length();
        } else {
            offset = offsetV.toInteger();

            if (haystack.length() < offset) {
                env.warning(trace, "strrpos(): offset cannot exceed string length");
                return Memory.FALSE;
            }
        }

        if (haystackLen == 0)
            return Memory.FALSE;

        char ch = '\0';
        String search = null;
        if (needle.isString()) {
            search = needle.toString();
            if (search.length() == 1) {
                ch = search.charAt(0);
                search = null;
            }
        } else {
            ch = needle.toChar();
        }

        int p;
        if (search == null) {
            p = haystack.lastIndexOf(ch, offset);
        } else {
            if (search.isEmpty()) {
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

    @Immutable
    public static Memory strrpos(Environment env, TraceInfo trace, String haystack, Memory needle) {
        return strrpos(env, trace, haystack, needle, null);
    }

    @Immutable
    public static Memory strripos(Environment env, TraceInfo trace, String haystack, Memory needleV) {
        return strripos(env, trace, haystack, needleV, null);
    }

    @Immutable
    public static Memory strripos(Environment env, TraceInfo trace, String haystack, Memory needleV, Memory offsetV) {
        String needle;

        if (needleV.isString()) {
            needle = needleV.toString();
        } else {
            needle = String.valueOf((char) needleV.toInteger());
        }

        int offset;

        if (offsetV == null) {
            offset = haystack.length();
        } else {
            offset = offsetV.toInteger();

            if (haystack.length() < offset) {
                env.warning(trace, "strripos(): offset cannot exceed string length");
                return Memory.FALSE;
            }
        }

        haystack = haystack.toLowerCase();
        needle = needle.toLowerCase();

        int pos = haystack.lastIndexOf(needle, offset);

        if (pos < 0) {
            return Memory.FALSE;
        } else {
            return LongMemory.valueOf(pos);
        }
    }

    @Immutable
    public static Memory stripos(Environment env, TraceInfo trace, String haystack, Memory needle, int offset) {
        int haystackLen = haystack.length();
        if (offset < 0 || offset > haystackLen) {
            env.warning(trace, "stripos(): Offset not contained in string");
            return Memory.FALSE;
        }

        if (haystackLen == 0)
            return Memory.FALSE;

        char ch = '\0';
        String search = null;
        if (needle.isString()) {
            search = needle.toString();
            if (search.length() == 1) {
                ch = Character.toUpperCase(search.charAt(0));
                search = null;
            }
        } else {
            ch = Character.toUpperCase(needle.toChar());
        }

        int p = -1;
        if (search == null) {
            for (int i = offset; i < haystackLen; i++) {
                if (Character.toUpperCase(haystack.charAt(i)) == ch) {
                    p = i;
                    break;
                }
            }
        } else {
            if (search.isEmpty()) {
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

    @Immutable
    public static Memory stripos(Environment env, TraceInfo trace, String haystack, Memory needle) {
        return stripos(env, trace, haystack, needle, 0);
    }

    @Immutable
    public static Memory strlen(Environment env, TraceInfo trace, Memory string) {
        if (string.isArray()) {
            env.warning(trace, "expects parameter 1 to be string, array given");
            return Memory.NULL;
        }
        if (string instanceof BinaryMemory)
            return LongMemory.valueOf(string.getBinaryBytes(env.getDefaultCharset()).length);

        return LongMemory.valueOf(string.toString().length());
    }


    protected static String _substr_replace(String string, String replacement, int start, int length) {
        int strLength = string.length();
        if (start > strLength)
            start = strLength;
        else if (start < 0)
            start = strLength + start;

        if (start < 0)
            start = 0;

        int end;
        if (length < 0)
            end = Math.max(strLength + length, start);
        else
            end = (strLength < length) ? strLength : (start + length);

        StringBuilder result = new StringBuilder();

        result.append(string.substring(0, start));
        result.append(replacement);
        result.append(string.substring(end));

        return result.toString();
    }

    public static Memory str_replace(Environment env, TraceInfo trace, Memory search, Memory replace, Memory string) {
        return str_replace(env, trace, search, replace, string, Memory.UNDEFINED);
    }

    public static Memory str_replace(Environment env, TraceInfo trace, Memory search, Memory replace, Memory string,
                                     @Reference Memory _count) {
        return _str_replace(env, trace, search, replace, string, _count, false);
    }

    public static Memory str_ireplace(Environment env, TraceInfo trace, Memory search, Memory replace, Memory string) {
        return str_ireplace(env, trace, search, replace, string, Memory.UNDEFINED);
    }

    public static Memory str_ireplace(Environment env, TraceInfo trace, Memory search, Memory replace, Memory string,
                                      @Reference Memory _count) {
        return _str_replace(env, trace, search, replace, string, _count, true);
    }

    protected static Memory _str_replace_impl(Environment env, TraceInfo trace,
                                              Memory search, Memory replace, Memory string,
                                              @Reference Memory _count, boolean isInsensitive) {
        String searchText = search.toString();
        String replaceText = replace.toString();
        String text = string.toString();

        AtomicLong count = _count.isUndefined() ? null : new AtomicLong(_count.toLong());
        text = StringUtils.replace(text, searchText, replaceText, isInsensitive, count);

        if (count != null) _count.assign(count.get());

        return StringMemory.valueOf(text);
    }

    protected static Memory _str_replace(Environment env, TraceInfo trace,
                                         Memory search, Memory replace, Memory string,
                                         @Reference Memory count, boolean isInsensitive) {
        if (count.isReference()) {
            count.assign(0);
        }

        if (string.isNull()) {
            return Memory.CONST_EMPTY_STRING;
        }

        if (string.isArray()) {
            ForeachIterator iterator = string.getNewIterator(env);
            ArrayMemory result = new ArrayMemory();

            while (iterator.next()) {
                Memory key = iterator.getMemoryKey();
                Memory value = iterator.getValue();

                if (value.isArray()) {
                    result.refOfIndex(key).assign(value.toImmutable());
                } else {
                    Memory ret = _str_replace(
                            env, trace, search, replace,
                            StringMemory.valueOf(value.toString()), count, isInsensitive
                    );

                    result.refOfIndex(key).assign(ret);
                }
            }

            return result.toConstant();
        } else {
            if (!search.isArray()) {
                String searchStr = search.toString();

                if (searchStr.isEmpty()) {
                    return string;
                }

                if (replace.isArray()) {
                    env.warning(trace, "str_replace(): Array to string conversion");
                }

                string = _str_replace_impl(env, trace,
                        StringMemory.valueOf(searchStr),
                        StringMemory.valueOf(replace.toString()),
                        string,
                        count,
                        isInsensitive);
            } else if (replace.isArray()) {
                ForeachIterator searchIterator = search.getNewIterator(env);
                ForeachIterator replaceIterator = replace.getNewIterator(env);

                while (searchIterator.next()) {
                    Memory searchValue = searchIterator.getValue();
                    Memory replaceValue;

                    if (replaceIterator.next()) {
                        replaceValue = replaceIterator.getValue();
                    } else {
                        replaceValue = Memory.NULL;
                    }

                    string = _str_replace(env, trace,
                            StringMemory.valueOf(searchValue.toString()),
                            StringMemory.valueOf(replaceValue.toString()),
                            string,
                            count,
                            isInsensitive);
                }
            } else {
                ForeachIterator searchIterator = search.getNewIterator(env);

                while (searchIterator.next()) {
                    string = _str_replace(
                            env, trace,
                            StringMemory.valueOf(searchIterator.getValue().toString()),
                            replace,
                            string,
                            count,
                            isInsensitive
                    );
                }
            }
        }

        return string;
    }

    @Immutable
    public static Memory substr_replace(Environment env, TraceInfo trace,
                                        Memory string, Memory replacementM, Memory startM, Memory lengthM) {
        int start = 0;
        int length = Integer.MAX_VALUE / 2;
        String replacement = "";

        ForeachIterator replacementIterator = null;
        if (replacementM.isArray())
            replacementIterator = replacementM.getNewIterator(env, false, false);
        else
            replacement = replacementM.toString();

        ForeachIterator startIterator = null;
        if (startM.isArray())
            startIterator = startM.getNewIterator(env, false, false);
        else
            start = startM.toInteger();

        ForeachIterator lengthIterator = null;
        if (lengthM.isArray())
            lengthIterator = lengthM.getNewIterator(env, false, false);
        else
            length = lengthM.toInteger();

        if (string.isArray()) {
            ArrayMemory resultArray = new ArrayMemory();
            ForeachIterator iterator = string.getNewIterator(env, false, false);

            while (iterator.next()) {
                String value = iterator.getValue().toString();

                if (replacementIterator != null && replacementIterator.next())
                    replacement = replacementIterator.getValue().toString();

                if (lengthIterator != null && lengthIterator.next())
                    length = lengthIterator.getValue().toInteger();

                if (startIterator != null && startIterator.next())
                    start = startIterator.getValue().toInteger();

                String result = _substr_replace(value, replacement, start, length);
                resultArray.add(new StringMemory(result));
            }

            return resultArray.toConstant();
        } else {
            if (replacementIterator != null && replacementIterator.next())
                replacement = replacementIterator.getValue().toString();

            if (lengthIterator != null && lengthIterator.next())
                length = lengthIterator.getValue().toInteger();

            if (startIterator != null && startIterator.next())
                start = startIterator.getValue().toInteger();

            return new StringMemory(_substr_replace(string.toString(), replacement, start, length));
        }
    }

    @Immutable
    public static Memory substr_replace(Environment env, TraceInfo trace,
                                        Memory string, Memory replacementM, Memory startM) {
        return substr_replace(env, trace, string, replacementM, startM, LongMemory.valueOf(Integer.MAX_VALUE / 2));
    }


    @Immutable
    public static Memory wordwrap(String str, int width, String _break, boolean cut) {
        int length = str.length();
        StringBuilderMemory sb = new StringBuilderMemory();

        int done = 0;
        int prevSpacePos = 0;
        int start = 0;
        int wordLength = 0;
        for (int i = 0; i < length + 1; i++) {
            char ch = i == length ? ' ' : str.charAt(i);

            if (Character.isSpaceChar(ch)
                    || (cut && wordLength + 1 >= width)) {
                if (done >= width || i == length) {
                    if (done <= width) {
                        sb.append(str.substring(start, i));
                    } else {
                        sb.append(str.substring(start, prevSpacePos));
                        i = prevSpacePos;
                    }
                    start = i + 1;
                    if (i != length)
                        sb.append(_break);

                    done = 0;
                    continue;
                } else
                    prevSpacePos = i;

                wordLength = 0;
            }

            done++;
            wordLength++;
        }

        return sb;
    }

    @Immutable
    public static Memory wordwrap(String str, int width, String _break) {
        return wordwrap(str, width, _break, false);
    }

    @Immutable
    public static Memory wordwrap(String str, int width) {
        return wordwrap(str, width, "\n", false);
    }

    @Immutable
    public static Memory wordwrap(String str) {
        return wordwrap(str, 75, "\n", false);
    }

    @Immutable
    public static String number_format(double number, int decimals, char decPoint, char thousandsSep) {
        String pattern;
        if (decimals > 0) {
            StringBuilder patternBuilder = new StringBuilder(6 + decimals);

            patternBuilder.append(thousandsSep == 0 ? "###0." : "#,##0.");

            for (int i = 0; i < decimals; i++) {
                patternBuilder.append('0');
            }

            pattern = patternBuilder.toString();
        } else {
            pattern = thousandsSep == 0 ? "###0" : "#,##0";
        }

        DecimalFormatSymbols decimalFormatSymbols;

        if (decPoint == '.' && thousandsSep == ',') {
            decimalFormatSymbols = DEFAULT_DECIMAL_FORMAT_SYMBOLS;
        } else {
            decimalFormatSymbols = new DecimalFormatSymbols();
            decimalFormatSymbols.setDecimalSeparator(decPoint);
            decimalFormatSymbols.setGroupingSeparator(thousandsSep);
            decimalFormatSymbols.setZeroDigit('0');
        }

        DecimalFormat format = new DecimalFormat(pattern, decimalFormatSymbols);

        String result = format.format(number);
        if (decPoint == 0 && decimals > 0) {
            // no way to get DecimalFormat to output nothing for the point,
            // so remove it here
            int i = result.lastIndexOf(decPoint);
            return result.substring(0, i) + result.substring(i + 1, result.length());
        } else {
            return result;
        }
    }

    @Immutable
    public static String number_format(double number, int decimals) {
        return number_format(number, decimals, '.', ',');
    }

    @Immutable
    public static String number_format(double number) {
        return number_format(number, 0, '.', ',');
    }

    @Immutable
    public static String str_repeat(String input, int multiplier) {
        if (multiplier <= 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < multiplier; i++)
            sb.append(input);

        return sb.toString();
    }

    @Immutable
    public static String str_pad(String string, int length, String pad, int type) {
        int strLen = string.length();
        int padLen = length - strLen;

        if (padLen <= 0) {
            return string;
        }

        if (pad == null || pad.length() == 0) {
            pad = " ";
        }

        int leftPad = 0;
        int rightPad = 0;

        switch (type) {
            case 0:
                leftPad = padLen;
                break;
            case 1:
            default:
                rightPad = padLen;
                break;
            case 2:
                leftPad = padLen / 2;
                rightPad = padLen - leftPad;
                break;
        }

        int padStringLen = pad.length();

        StringBuilder sb = new StringBuilder(string.length() + padLen);

        for (int i = 0; i < leftPad; i++) {
            sb.append(pad.charAt(i % padStringLen));
        }
        sb = sb.append(string);
        for (int i = 0; i < rightPad; i++) {
            sb.append(pad.charAt(i % padStringLen));
        }

        return sb.toString();
    }

    @Immutable
    public static String str_pad(String string, int length, String pad) {
        return str_pad(string, length, pad, StringConstants.STR_PAD_RIGHT);
    }

    @Immutable
    public static String str_pad(String string, int length) {
        return str_pad(string, length, " ", StringConstants.STR_PAD_RIGHT);
    }

    @Immutable
    public static int crc32(Environment env, Memory value) {
        CRC32 crc = new CRC32();
        crc.update(value.getBinaryBytes(env.getDefaultCharset()));

        return (int) crc.getValue();
    }

    @Immutable
    public static String nl2br(String string, boolean isXhtml) {
        String br = "<br />";

        if (!isXhtml) {
            br = "<br>";
        }
        return string.replaceAll("(\\r?\\n)", br + "$1");
    }

    @Immutable
    public static String htmlspecialchars_decode(String string) {
        return htmlspecialchars_decode(string, StringConstants.ENT_COMPAT);
    }

    @Immutable
    public static String htmlspecialchars_decode(String string, int quoteStyle) {
        int len = string.length();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);

            if (ch != '&') {
                sb.append(ch);

                continue;
            }

            switch (string.charAt(i + 1)) {
                case 'a':
                    sb.append('&');
                    if (i + 4 < len
                            && string.charAt(i + 2) == 'm'
                            && string.charAt(i + 3) == 'p'
                            && string.charAt(i + 4) == ';') {
                        i += 4;
                    }
                    break;

                case 'q':
                    if ((quoteStyle & StringConstants.ENT_HTML_QUOTE_DOUBLE) != 0
                            && i + 5 < len
                            && string.charAt(i + 2) == 'u'
                            && string.charAt(i + 3) == 'o'
                            && string.charAt(i + 4) == 't'
                            && string.charAt(i + 5) == ';') {
                        i += 5;
                        sb.append('"');
                    } else {
                        sb.append('&');
                    }
                    break;

                case '#':
                    if ((quoteStyle & StringConstants.ENT_HTML_QUOTE_SINGLE) != 0
                            && i + 5 < len
                            && string.charAt(i + 2) == '0'
                            && string.charAt(i + 3) == '3'
                            && string.charAt(i + 4) == '9'
                            && string.charAt(i + 5) == ';') {
                        i += 5;
                        sb.append('\'');
                    } else {
                        sb.append('&');
                    }

                    break;

                case 'l':
                    if (i + 3 < len
                            && string.charAt(i + 2) == 't'
                            && string.charAt(i + 3) == ';') {
                        i += 3;

                        sb.append('<');
                    } else {
                        sb.append('&');
                    }
                    break;

                case 'g':
                    if (i + 3 < len
                            && string.charAt(i + 2) == 't'
                            && string.charAt(i + 3) == ';') {
                        i += 3;

                        sb.append('>');
                    } else {
                        sb.append('&');
                    }
                    break;

                default:
                    sb.append('&');
            }
        }

        return sb.toString();
    }

    @Immutable
    public static Memory htmlspecialchars(Environment env, TraceInfo trace,
                                          Memory _string, int quoteStyle) {
        return htmlspecialchars(env, trace, _string, quoteStyle, "UTF-8");
    }

    @Immutable
    public static Memory htmlspecialchars(Environment env, TraceInfo trace,
                                          Memory _string) {
        return htmlspecialchars(env, trace, _string, StringConstants.ENT_COMPAT, "UTF-8");
    }

    public static Memory htmlspecialchars(Environment env, TraceInfo trace,
                                          Memory _string, int quoteStyle, String charset) {
        try {
            String string = new String(_string.getBinaryBytes(env.getDefaultCharset()), charset);
            int len = string.length();

            StringBuilderMemory sb = new StringBuilderMemory();

            for (int i = 0; i < len; i++) {
                char ch = string.charAt(i);

                switch (ch) {
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '"':
                        if ((quoteStyle & StringConstants.ENT_HTML_QUOTE_DOUBLE) != 0) {
                            sb.append("&quot;");
                        } else {
                            sb.append(ch);
                        }
                        break;
                    case '\'':
                        if ((quoteStyle & StringConstants.ENT_HTML_QUOTE_SINGLE) != 0) {
                            sb.append("&#039;");
                        } else {
                            sb.append(ch);
                        }
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    default:
                        sb.append(ch);
                        break;
                }
            }

            return sb;
        } catch (UnsupportedEncodingException e) {
            env.warning(trace, "htmlspecialchars(): unsupported encoding - %s", charset);
            return Memory.FALSE;
        }
    }

    @Immutable
    public static Memory html_entity_decode(Environment env, TraceInfo trace,
                                            Memory _string, int flags, String encoding) {
        try {
            String string = new String(_string.getBinaryBytes(env.getDefaultCharset()), encoding);

            int len = string.length();
            int htmlEntityStart = -1;
            StringBuilder result = new StringBuilder();

            for (int i = 0; i < len; i++) {
                char ch = string.charAt(i);

                if (ch == '&' && htmlEntityStart < 0) {
                    htmlEntityStart = i;
                } else if (htmlEntityStart < 0) {
                    result.append(ch);
                } else if (ch == ';') {
                    String entity = string.substring(htmlEntityStart, i + 1);
                    Memory value = htmlEntriesMap().getByScalar(entity);

                    if (value == null) {
                        result.append(entity);
                    } else {
                        result.append(value);
                    }

                    htmlEntityStart = -1;
                } else if (!(('a' <= ch && ch <= 'z') || ('A' <= ch && ch <= 'Z'))) {
                    result.append('&');
                    i = htmlEntityStart;
                    htmlEntityStart = -1;
                }
            }

            if (htmlEntityStart > 0) {
                result.append(string, htmlEntityStart, len);
            }

            return new StringMemory(result.toString());
        } catch (UnsupportedEncodingException e) {
            env.warning(trace, "html_entity_decode(): unsupported encoding - %s", encoding);
            return Memory.FALSE;
        }
    }

    public static Memory htmlentities(Environment env, TraceInfo trace,
                                      Memory _string, int quoteStyle, String encoding) {
        try {
            String string = new String(_string.getBinaryBytes(env.getDefaultCharset()), encoding);
            StringBuffer sb = new StringBuffer();

            int length = string.length();
            for (int i = 0; i < length; ++i) {
                char ch = string.charAt(i);
                Memory el = htmlEntriesMap().getByScalar(String.valueOf(ch));

                if (ch == '"') {
                    if ((quoteStyle & StringConstants.ENT_HTML_QUOTE_DOUBLE) != 0) {
                        sb.append("&quot;");
                    } else {
                        sb.append(ch);
                    }
                } else if (ch == '\'') {
                    if ((quoteStyle & StringConstants.ENT_HTML_QUOTE_SINGLE) != 0) {
                        sb.append("&#039;");
                    } else {
                        sb.append('\'');
                    }
                } else if (el != null) {
                    sb.append(el);
                } else {
                    sb.append(ch);
                }
            }
            return new StringMemory(sb.toString());
        } catch (UnsupportedEncodingException e) {
            env.warning(trace, "htmlentities(): unsupported encoding - %s", encoding);
            return Memory.FALSE;
        }
    }

    @Immutable
    public static int levenshtein(String str1, String str2) {
        return levenshtein(str1, str2, 1, 1, 1);
    }

    @Immutable
    public static int levenshtein(String s1, String s2, int cost_ins, int cost_rep, int cost_del) {
        int i, j, flip, ii, ii2, cost;
        int l1 = s1.length();
        int l2 = s2.length();

        if (l1 > 255)
            return -1;
        if (l2 > 255)
            return -1;

        int cr = cost_rep;
        int ci = cost_ins;
        int cd = cost_del;

        int cutHalf = flip = Math.max(l1, l2);

        int minCost = Math.min(Math.min(cd, ci), cr);

        int minD = Math.max(minCost, (l1 - l2) * cd);
        int minI = Math.max(minCost, (l2 - l1) * ci);

        int[] buf = new int[(cutHalf * 2) + 1];
        for (i = 0; i <= l2; ++i) {
            buf[i] = i * minD;
        }

        for (i = 0; i < l1; ++i, flip = cutHalf - flip) {
            char ch = s1.charAt(i);
            buf[flip] = (i + 1) * minI;

            ii = flip;
            ii2 = cutHalf - flip;

            for (j = 0; j < l2; ++j, ++ii, ++ii2) {
                cost = (ch == s2.charAt(j) ? 0 : cr);
                buf[ii + 1] = Math.min(Math.min(buf[ii2 + 1] + cd, buf[ii] + ci), buf[ii2] + cost);
            }
        }
        return buf[l2 + cutHalf - flip];
    }

    @Immutable
    public static Memory convert_uudecode(String source) {
        int length = source.length();

        if (length == 0) {
            return Memory.FALSE;
        }

        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < length) {
            int ch1 = source.charAt(i++);

            if (ch1 == 0x60 || ch1 == 0x20) {
                break;
            } else if (ch1 < 0x20 || 0x5f < ch1) {
                continue;
            }

            int sublen = ch1 - 0x20;

            while (sublen > 0) {
                int code;

                code = ((source.charAt(i++) - 0x20) & 0x3f) << 18;
                code += ((source.charAt(i++) - 0x20) & 0x3f) << 12;
                code += ((source.charAt(i++) - 0x20) & 0x3f) << 6;
                code += ((source.charAt(i++) - 0x20) & 0x3f);

                builder.append(code >> 16);

                if (sublen > 1) {
                    builder.append(code >> 8);
                }

                if (sublen > 2) {
                    builder.append(code);
                }

                sublen -= 3;
            }
        }

        return new StringMemory(builder.toString());
    }

    @Immutable
    public static Memory convert_uuencode(String source) {
        if (source.length() == 0) {
            return Memory.FALSE;
        }

        StringBuilderMemory result = new StringBuilderMemory();
        int i = 0;
        int length = source.length();
        while (i < length) {
            int sublen = length - i;

            if (45 < sublen) {
                sublen = 45;
            }

            result.append((char) (sublen + 0x20));

            int end = i + sublen;

            while (i < end) {
                int code = source.charAt(i++) << 16;

                if (i < length) {
                    code += source.charAt(i++) << 8;
                }

                if (i < length) {
                    code += source.charAt(i++);
                }

                result.append(toUUChar(((code >> 18) & 0x3f)));
                result.append(toUUChar(((code >> 12) & 0x3f)));
                result.append(toUUChar(((code >> 6) & 0x3f)));
                result.append(toUUChar(((code) & 0x3f)));
            }

            result.append('\n');
        }

        result.append((char) 0x60);
        result.append('\n');
        return result;
    }

    @Immutable
    public static String metaphone(String string) {
        int length = string.length();
        int index = 0;
        char ch = 0;

        // ignore everything up until first letter
        for (; index < length; index++) {
            ch = toUpperCase(string.charAt(index));

            if ('A' <= ch && ch <= 'Z') {
                break;
            }
        }

        if (index == length) {
            return "";
        }

        int lastIndex = length - 1;
        StringBuilder result = new StringBuilder(length);

        char nextCh = index < lastIndex
                ? toUpperCase(string.charAt(index + 1))
                : 0;

        switch (ch) {
            case 'A':
                if (nextCh == 'E') {
                    result.append('E');
                    index += 2;
                } else {
                    result.append('A');
                    index += 1;
                }

                break;

            case 'E':
            case 'I':
            case 'O':
            case 'U':
                result.append(ch);
                index += 1;
                break;

            case 'G':
            case 'K':
            case 'P':
                if (nextCh == 'N') {
                    result.append('N');
                    index += 2;
                }

                break;

            case 'W':
                if (nextCh == 'H' || nextCh == 'R') {
                    result.append(nextCh);
                    index += 2;
                } else {
                    switch (nextCh) {
                        case 'A':
                        case 'E':
                        case 'I':
                        case 'O':
                        case 'U':
                            result.append('W');
                            index += 2;
                            break;
                        default:
                            break;
                    }
                }

                break;

            case 'X':
                result.append('S');
                index += 1;
                break;

            default:
                break;
        }

        char prevCh;
        for (; index < length; index++) {

            if (index > 0) {
                prevCh = toUpperCase(string.charAt(index - 1));
            } else {
                prevCh = 0;
            }

            ch = toUpperCase(string.charAt(index));

            if (ch < 'A' || ch > 'Z') {
                continue;
            }

            if (ch == prevCh && ch != 'C') {
                continue;
            }

            if (index + 1 < length) {
                nextCh = toUpperCase(string.charAt(index + 1));
            } else {
                nextCh = 0;
            }

            char nextnextCh;

            if (index + 2 < length) {
                nextnextCh = toUpperCase(string.charAt(index + 2));
            } else {
                nextnextCh = 0;
            }


            switch (ch) {
                case 'B':
                    if (prevCh != 'M') {
                        result.append('B');
                    }
                    break;

                case 'C':
                    switch (nextCh) {
                        case 'E':
                        case 'I':
                        case 'Y':
                            // makesoft
                            if (nextCh == 'I' && nextnextCh == 'A') {
                                result.append('X');
                            } else if (prevCh == 'S') {
                            } else {
                                result.append('S');
                            }
                            break;
                        default:
                            if (nextCh == 'H') {
                                result.append('X');
                                index++;
                            } else {
                                result.append('K');
                            }
                            break;
                    }

                    break;

                case 'D':
                    if (nextCh == 'G') {
                        switch (nextnextCh) {
                            case 'E':
                            case 'I':
                            case 'Y':
                                // makesoft
                                result.append('J');
                                index++;
                                break;
                            default:
                                result.append('T');
                                break;
                        }
                    } else {
                        result.append('T');
                    }

                    break;

                case 'G':
                    if (nextCh == 'H') {
                        boolean isSilent = false;

                        if (index - 3 >= 0) {
                            char prev3Ch = toUpperCase(string.charAt(index - 3));
                            switch (prev3Ch) {
                                // noghtof
                                case 'B':
                                case 'D':
                                case 'H':
                                    isSilent = true;
                                    break;
                                default:
                                    break;
                            }
                        }

                        if (!isSilent) {
                            if (index - 4 >= 0) {
                                char prev4Ch = toUpperCase(string.charAt(index - 4));

                                isSilent = (prev4Ch == 'H');
                            }
                        }

                        if (!isSilent) {
                            result.append('F');
                            index++;
                        }
                    } else if (nextCh == 'N') {
                        char nextnextnextCh;

                        if (index + 3 < length) {
                            nextnextnextCh = toUpperCase(string.charAt(index + 3));
                        } else {
                            nextnextnextCh = 0;
                        }

                        if (nextnextCh < 'A' || nextnextCh > 'Z') {
                        } else if (nextnextCh == 'E' && nextnextnextCh == 'D') {
                        } else {
                            result.append('K');
                        }
                    } else if (prevCh == 'G') {
                        result.append('K');
                    } else {
                        switch (nextCh) {
                            case 'E':
                            case 'I':
                            case 'Y':
                                // makesoft
                                result.append('J');
                                break;
                            default:
                                result.append('K');
                                break;
                        }
                    }

                    break;

                case 'H':
                case 'W':
                case 'Y':
                    switch (nextCh) {
                        case 'A':
                        case 'E':
                        case 'I':
                        case 'O':
                        case 'U':
                            // followed by a vowel

                            if (ch == 'H') {
                                switch (prevCh) {
                                    case 'C':
                                    case 'G':
                                    case 'P':
                                    case 'S':
                                    case 'T':
                                        // affecth
                                        break;
                                    default:
                                        result.append('H');
                                        break;
                                }
                            } else {
                                result.append(ch);
                            }

                            break;
                        default:
                            // not followed by a vowel
                            break;
                    }

                    break;

                case 'K':
                    if (prevCh != 'C') {
                        result.append('K');
                    }

                    break;

                case 'P':
                    if (nextCh == 'H') {
                        result.append('F');
                    } else {
                        result.append('P');
                    }

                    break;

                case 'Q':
                    result.append('K');
                    break;

                case 'S':
                    if (nextCh == 'I' && (nextnextCh == 'O' || nextnextCh == 'A')) {
                        result.append('X');
                    } else if (nextCh == 'H') {
                        result.append('X');
                        index++;
                    } else {
                        result.append('S');
                    }

                    break;

                case 'T':
                    if (nextCh == 'I' && (nextnextCh == 'O' || nextnextCh == 'A')) {
                        result.append('X');
                    } else if (nextCh == 'H') {
                        result.append('0');
                        index++;
                    } else {
                        result.append('T');
                    }

                    break;

                case 'V':
                    result.append('F');

                    break;

                case 'X':
                    result.append('K');
                    result.append('S');
                    break;

                case 'Z':
                    result.append('S');
                    break;

                case 'F':
                case 'J':
                case 'L':
                case 'M':
                case 'N':
                case 'R':
                    result.append(ch);
                    break;

                default:
                    break;
            }
        }

        return result.toString();
    }

    @Immutable
    public static Memory strspn(String string, String characters, int offset, int length) {
        return strspnImpl(string, characters, offset, length, true);
    }

    @Immutable
    public static Memory strspn(String string, String characters, int offset) {
        return strspnImpl(string, characters, offset, -2147483648, true);
    }

    @Immutable
    public static Memory strspn(String string, String characters) {
        return strspnImpl(string, characters, 0, -2147483648, true);
    }

    private static Memory strspnImpl(String string, String characters, int offset, int length, boolean isMatch) {
        int strlen = string.length();

        if (offset < 0) {
            offset += strlen;

            if (offset < 0) {
                offset = 0;
            }
        }

        if (offset > strlen) {
            return Memory.FALSE;
        }

        if (length == -2147483648) {
            length = strlen;
        } else if (length < 0) {
            length += (strlen - offset);

            if (length < 0) {
                length = 0;
            }
        }

        int end = offset + length;

        if (strlen < end) {
            end = strlen;
        }

        int count = 0;

        for (; offset < end; offset++) {
            char ch = string.charAt(offset);
            boolean isPresent = characters.indexOf(ch) > -1;

            if (isPresent == isMatch) {
                count++;
            } else {
                return LongMemory.valueOf(count);
            }
        }

        return LongMemory.valueOf(count);
    }

    @Immutable
    public static Memory strpbrk(String haystack, String charList) {
        int len = haystack.length();
        int sublen = charList.length();

        for (int i = 0; i < len; i++) {
            for (int j = 0; j < sublen; j++) {
                if (haystack.charAt(i) == charList.charAt(j)) {
                    return new StringMemory(haystack.substring(i));
                }
            }
        }

        return Memory.FALSE;
    }

    @Immutable
    public static Memory stristr(String haystack, Memory needleV) {
        String needleLower;

        if (needleV.isString()) {
            needleLower = needleV.toString().toLowerCase();
        } else {
            char lower = Character.toLowerCase((char) needleV.toLong());

            needleLower = String.valueOf(lower);
        }

        String haystackLower = haystack.toLowerCase();

        int i = haystackLower.indexOf(needleLower);

        if (i >= 0) {
            return new StringMemory(haystack.substring(i));
        } else {
            return Memory.FALSE;
        }
    }

    @Immutable
    public static String stripslashes(String string) {
        StringBuilder sb = new StringBuilder();
        int len = string.length();

        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);

            if (ch == '\\') {
                if (i + 1 < len) {
                    char ch2 = string.charAt(i + 1);
                    if (ch2 == '0') {
                        ch2 = 0x0;
                    }
                    sb.append(ch2);
                    i++;
                }
            } else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    @Immutable
    public static String stripcslashes(String source) {
        StringBuilder result = new StringBuilder(source.length());
        int length = source.length();
        for (int i = 0; i < length; i++) {
            int ch = source.charAt(i);

            if (ch == '\\') {
                i++;
                if (i == length) {
                    ch = '\\';
                } else {
                    ch = source.charAt(i);
                    switch (ch) {
                        case 'a':
                            ch = 0x07;
                            break;
                        case 'b':
                            ch = '\b';
                            break;
                        case 't':
                            ch = '\t';
                            break;
                        case 'n':
                            ch = '\n';
                            break;
                        case 'v':
                            ch = 0xb;
                            break;
                        case 'f':
                            ch = '\f';
                            break;
                        case 'r':
                            ch = '\r';
                            break;
                        case 'x':
                            // up to two digits for a hex number
                            if (i + 1 == length) {
                                break;
                            }
                            int digitValue = hexToDigit(source.charAt(i + 1));
                            if (digitValue < 0) {
                                break;
                            }
                            ch = digitValue;
                            i++;
                            if (i + 1 == length) {
                                break;
                            }

                            digitValue = hexToDigit(source.charAt(i + 1));
                            if (digitValue < 0) {
                                break;
                            }

                            ch = ((ch << 4) | digitValue);
                            i++;
                            break;
                        default:
                            // up to three digits from 0 to 7 for an octal number
                            digitValue = octToDigit((char) ch);
                            if (digitValue < 0) {
                                break;
                            }

                            ch = digitValue;
                            if (i + 1 == length) {
                                break;
                            }

                            digitValue = octToDigit(source.charAt(i + 1));
                            if (digitValue < 0) {
                                break;
                            }

                            ch = ((ch << 3) | digitValue);
                            i++;

                            if (i + 1 == length) {
                                break;
                            }

                            digitValue = octToDigit(source.charAt(i + 1));
                            if (digitValue < 0) {
                                break;
                            }

                            ch = ((ch << 3) | digitValue);
                            i++;
                    }
                }
            } // if ch == '/'

            result.append((char) ch);
        }

        return result.toString();
    }

    @Immutable
    public static String strip_tags(String string) {
        return strip_tags(string, null);
    }

    @Immutable
    public static String strip_tags(String string, Memory allowTags) {
        StringBuilder result = new StringBuilder();

        Set<String> allowedTagMap = null;

        if (allowTags != null) {
            allowedTagMap = getAllowedTags(allowTags.toString());
        }

        int len = string.length();
        for (int i = 0; i < len; i++) {
            char ch = string.charAt(i);

            if (i + 1 >= len || ch != '<') {
                result.append(ch);
                continue;
            }

            ch = string.charAt(i + 1);

            if (Character.isWhitespace(ch)) {
                i++;

                result.append('<');
                result.append(ch);
                continue;
            }

            int tagNameStart = i + 1;

            if (ch == '/') {
                tagNameStart++;
            }

            int j = tagNameStart;
            while (j < len
                    && (ch = string.charAt(j)) != '>'
                    // && ch != '/'
                    && !Character.isWhitespace(ch)) {
                j++;
            }

            String tagName = string.substring(tagNameStart, j);
            int tagEnd = 0;

            if (allowedTagMap != null && allowedTagMap.contains(tagName)) {
                result.append(string, i, Math.min(j + 1, len));
            } else {
                while (j < len && (ch = string.charAt(j)) != '<') {

                    if (ch == '>') {
                        tagEnd = j;
                    }
                    j++;
                }
            }

            i = (tagEnd != 0) ? tagEnd : j;
        }

        return result.toString();
    }

    private static Set<String> getAllowedTags(String str) {
        int len = str.length();

        Set<String> set = new HashSet<String>();

        for (int i = 0; i < len; i++) {
            char ch = str.charAt(i);

            switch (ch) {
                case '<':
                    int j = i + 1;
                    while (j < len
                            && (ch = str.charAt(j)) != '>'
                            //&& ch != '/'
                            && !Character.isWhitespace(ch)) {
                        j++;
                    }
                    if (ch == '>'
                            && i + 1 < j
                            && j < len) {
                        set.add(str.substring(i + 1, j));
                    }

                    i = j;
                default:
                    continue;
            }

        }
        return set;
    }


    public static Memory str_word_count(String string, int format, String additionalWordCharacters) {
        if (format < 0 || format > 2) {
            return Memory.NULL;
        }

        int strlen = string.length();
        boolean isAdditionalWordCharacters = false;

        if (additionalWordCharacters != null) {
            isAdditionalWordCharacters = additionalWordCharacters.length() > 0;
        }

        ArrayMemory resultArray = null;

        if (format > 0) {
            resultArray = new ArrayMemory();
        }

        boolean isBetweenWords = true;
        int wordCount = 0;
        int lastWordStart = 0;

        for (int i = 0; i <= strlen; i++) {
            boolean isWordCharacter;

            if (i < strlen) {
                int ch = string.charAt(i);

                isWordCharacter = Character.isLetter(ch)
                        || ch == '-'
                        || ch == '\''
                        || (isAdditionalWordCharacters
                        && additionalWordCharacters.indexOf(ch) > -1);
            } else {
                isWordCharacter = false;
            }

            if (isWordCharacter) {
                if (isBetweenWords) {
                    // starting a word
                    isBetweenWords = false;

                    lastWordStart = i;
                    wordCount++;
                }
            } else {
                if (!isBetweenWords) {
                    // finished a word
                    isBetweenWords = true;

                    if (format > 0) {
                        String word = string.substring(lastWordStart, i);

                        if (format == 1) {
                            resultArray.add(new StringMemory(word));
                        } else if (format == 2) {
                            resultArray.refOfIndex(lastWordStart).assign(word);
                        }
                    }
                }
            }
        }

        if (resultArray == null) {
            return LongMemory.valueOf(wordCount);
        } else {
            return resultArray.toConstant();
        }
    }

    public static String base64_encode(Environment env, Memory value) {
        return Base64.getEncoder().encodeToString(value.getBinaryBytes(env.getDefaultCharset()));
    }

    public static Memory base64_decode(String value) {
        try {
            return new BinaryMemory(Base64.getDecoder().decode(value));
        } catch (IllegalArgumentException e) {
            return Memory.FALSE;
        }
    }

    public static Memory parse_url(String url, int component) {
        try {
            URI uri = URI.create(url);

            switch (component) {
                case 0:
                    return uri.getScheme() == null ? Memory.NULL : StringMemory.valueOf(uri.getScheme());
                case 1:
                    return uri.getHost() == null ? Memory.NULL : StringMemory.valueOf(uri.getHost());
                case 2:
                    return uri.getPort() == -1 ? Memory.NULL : LongMemory.valueOf(uri.getPort());
                case 3:
                case 4:
                    return uri.getUserInfo() != null ? Memory.NULL : StringMemory.valueOf(uri.getUserInfo());
                case 5:
                    return uri.getPath() != null ? Memory.NULL : StringMemory.valueOf(uri.getPath());
                case 6:
                    return uri.getQuery() != null ? Memory.NULL : StringMemory.valueOf(uri.getQuery());
                case 7:
                    return uri.getFragment() != null ? Memory.NULL : StringMemory.valueOf(uri.getFragment());
                case -1:
                    ArrayMemory result = new ArrayMemory();
                    if (uri.getScheme() != null) {
                        result.refOfIndex("scheme").assign(uri.getScheme());
                    }

                    if (uri.getHost() != null) {
                        result.refOfIndex("host").assign(uri.getHost());
                    }

                    if (uri.getPort() != -1) {
                        result.refOfIndex("port").assign(uri.getPort());
                    }

                    if (uri.getPath() != null) {
                        result.refOfIndex("path").assign(uri.getPath());
                    }

                    if (uri.getUserInfo() != null) {
                        result.refOfIndex("user").assign(uri.getUserInfo());
                    }

                    if (uri.getQuery() != null) {
                        result.refOfIndex("query").assign(uri.getQuery());
                    }

                    if (uri.getFragment() != null) {
                        result.refOfIndex("fragment").assign(uri.getFragment());
                    }

                    return result.toConstant();
                default:
                    return Memory.FALSE;
            }
        } catch (IllegalArgumentException e) {
            return Memory.FALSE;
        }
    }

    public static Memory parse_url(String url) {
        return parse_url(url, -1);
    }

    public static String uniqid(String prefix) {
        return prefix + UUID.fromString(String.valueOf(System.currentTimeMillis())).toString().replace("-", "");
    }

    public static String uniqid() {
        return uniqid("");
    }

    public static String urldecode(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, "UTF-8");
    }

    public static String urlencode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, "UTF-8");
    }

    public static String rawurlencode(String url) throws UnsupportedEncodingException {
        return URLEncoder.encode(url, "UTF-8")
                // OAuth encodes some characters differently:
                .replace("+", "%20").replace("*", "%2A")
                .replace("%7E", "~");
    }

    public static String rawurldecode(String url) throws UnsupportedEncodingException {
        return URLDecoder.decode(url, "UTF-8")
                // OAuth encodes some characters differently:
                .replace("%20", "+").replace("%2A", "*")
                .replace("~", "%7E");
    }

    public static void setLocale() {
        // nop.
    }

    public static void dl(String extension) {
        // ... nop
    }

    public static int strnatcmp(String one, String two) {
        return new NaturalOrderComparator(false, false).compare(one, two);
    }

    public static int strnatcasecmp(String one, String two) {
        return new NaturalOrderComparator(true, false).compare(one, two);
    }

    static class MyGZIPOutputStream
            extends GZIPOutputStream {

        public MyGZIPOutputStream( OutputStream out ) throws IOException {
            super( out );
        }

        public void setLevel( int level ) {
            def.setLevel(level);
        }
    }

    /*protected static byte[] gzencodeImpl(String str, int level) throws IOException
    {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream())
        {
            try (MyGZIPOutputStream gzip = new MyGZIPOutputStream(out))
            {
                gzip.setLevel(level);
                gzip.write(str.getBytes(StandardCharsets.UTF_8));
            }
            return out.toByteArray();
            //return out.toString(StandardCharsets.ISO_8859_1);
            // Some single byte encoding
        }
    }

    public static String gzdecodeImpl(byte[] str) throws IOException
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str)))
        {
            int b;
            while ((b = gis.read()) != -1) {
                baos.write((byte) b);
            }
        }
        return new String(baos.toByteArray(), StandardCharsets.UTF_8);
    }

    public static Memory gzencode(String value) throws IOException {
        return gzencode(value, -1);
    }

    public static Memory gzencode(String value, int level) {
        if (level == -1) {
            level = 6;
        }

        try {
            return new BinaryMemory(gzencodeImpl(value, level));
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }

    public static Memory gzdecode(Environment env, Memory value) {
        try {
            return StringMemory.valueOf(gzdecodeImpl(value.getBinaryBytes(env.getDefaultCharset())));
        } catch (IOException e) {
            return Memory.FALSE;
        }
    }*/

    private static ArrayMemory htmlEntriesMap() {
        if (HTML_ENTITIES == null) {
            HTML_ENTITIES = new ArrayMemory();

            HTML_ENTITIES.put("\"", new StringMemory("&quot;"));
            HTML_ENTITIES.put("&", new StringMemory("&amp;"));
            HTML_ENTITIES.put("\"", new StringMemory("&#039;"));
            HTML_ENTITIES.put("<", new StringMemory("&lt;"));
            HTML_ENTITIES.put(">", new StringMemory("&gt;"));
            HTML_ENTITIES.put(" ", new StringMemory("&nbsp;"));
            HTML_ENTITIES.put("¡", new StringMemory("&iexcl;"));
            HTML_ENTITIES.put("¢", new StringMemory("&cent;"));
            HTML_ENTITIES.put("£", new StringMemory("&pound;"));
            HTML_ENTITIES.put("¤", new StringMemory("&curren;"));
            HTML_ENTITIES.put("¥", new StringMemory("&yen;"));
            HTML_ENTITIES.put("¦", new StringMemory("&brvbar;"));
            HTML_ENTITIES.put("§", new StringMemory("&sect;"));
            HTML_ENTITIES.put("¨", new StringMemory("&uml;"));
            HTML_ENTITIES.put("©", new StringMemory("&copy;"));
            HTML_ENTITIES.put("ª", new StringMemory("&ordf;"));
            HTML_ENTITIES.put("«", new StringMemory("&laquo;"));
            HTML_ENTITIES.put("¬", new StringMemory("&not;"));
            HTML_ENTITIES.put("­", new StringMemory("&shy;"));
            HTML_ENTITIES.put("®", new StringMemory("&reg;"));
            HTML_ENTITIES.put("¯", new StringMemory("&macr;"));
            HTML_ENTITIES.put("°", new StringMemory("&deg;"));
            HTML_ENTITIES.put("±", new StringMemory("&plusmn;"));
            HTML_ENTITIES.put("²", new StringMemory("&sup2;"));
            HTML_ENTITIES.put("³", new StringMemory("&sup3;"));
            HTML_ENTITIES.put("´", new StringMemory("&acute;"));
            HTML_ENTITIES.put("µ", new StringMemory("&micro;"));
            HTML_ENTITIES.put("¶", new StringMemory("&para;"));
            HTML_ENTITIES.put("·", new StringMemory("&middot;"));
            HTML_ENTITIES.put("¸", new StringMemory("&cedil;"));
            HTML_ENTITIES.put("¹", new StringMemory("&sup1;"));
            HTML_ENTITIES.put("º", new StringMemory("&ordm;"));
            HTML_ENTITIES.put("»", new StringMemory("&raquo;"));
            HTML_ENTITIES.put("¼", new StringMemory("&frac14;"));
            HTML_ENTITIES.put("½", new StringMemory("&frac12;"));
            HTML_ENTITIES.put("¾", new StringMemory("&frac34;"));
            HTML_ENTITIES.put("¿", new StringMemory("&iquest;"));
            HTML_ENTITIES.put("À", new StringMemory("&Agrave;"));
            HTML_ENTITIES.put("Á", new StringMemory("&Aacute;"));
            HTML_ENTITIES.put("Â", new StringMemory("&Acirc;"));
            HTML_ENTITIES.put("Ã", new StringMemory("&Atilde;"));
            HTML_ENTITIES.put("Ä", new StringMemory("&Auml;"));
            HTML_ENTITIES.put("Å", new StringMemory("&Aring;"));
            HTML_ENTITIES.put("Æ", new StringMemory("&AElig;"));
            HTML_ENTITIES.put("Ç", new StringMemory("&Ccedil;"));
            HTML_ENTITIES.put("È", new StringMemory("&Egrave;"));
            HTML_ENTITIES.put("É", new StringMemory("&Eacute;"));
            HTML_ENTITIES.put("Ê", new StringMemory("&Ecirc;"));
            HTML_ENTITIES.put("Ë", new StringMemory("&Euml;"));
            HTML_ENTITIES.put("Ì", new StringMemory("&Igrave;"));
            HTML_ENTITIES.put("Í", new StringMemory("&Iacute;"));
            HTML_ENTITIES.put("Î", new StringMemory("&Icirc;"));
            HTML_ENTITIES.put("Ï", new StringMemory("&Iuml;"));
            HTML_ENTITIES.put("Ð", new StringMemory("&ETH;"));
            HTML_ENTITIES.put("Ñ", new StringMemory("&Ntilde;"));
            HTML_ENTITIES.put("Ò", new StringMemory("&Ograve;"));
            HTML_ENTITIES.put("Ó", new StringMemory("&Oacute;"));
            HTML_ENTITIES.put("Ô", new StringMemory("&Ocirc;"));
            HTML_ENTITIES.put("Õ", new StringMemory("&Otilde;"));
            HTML_ENTITIES.put("Ö", new StringMemory("&Ouml;"));
            HTML_ENTITIES.put("×", new StringMemory("&times;"));
            HTML_ENTITIES.put("Ø", new StringMemory("&Oslash;"));
            HTML_ENTITIES.put("Ù", new StringMemory("&Ugrave;"));
            HTML_ENTITIES.put("Ú", new StringMemory("&Uacute;"));
            HTML_ENTITIES.put("Û", new StringMemory("&Ucirc;"));
            HTML_ENTITIES.put("Ü", new StringMemory("&Uuml;"));
            HTML_ENTITIES.put("Ý", new StringMemory("&Yacute;"));
            HTML_ENTITIES.put("Þ", new StringMemory("&THORN;"));
            HTML_ENTITIES.put("ß", new StringMemory("&szlig;"));
            HTML_ENTITIES.put("à", new StringMemory("&agrave;"));
            HTML_ENTITIES.put("á", new StringMemory("&aacute;"));
            HTML_ENTITIES.put("â", new StringMemory("&acirc;"));
            HTML_ENTITIES.put("ã", new StringMemory("&atilde;"));
            HTML_ENTITIES.put("ä", new StringMemory("&auml;"));
            HTML_ENTITIES.put("å", new StringMemory("&aring;"));
            HTML_ENTITIES.put("æ", new StringMemory("&aelig;"));
            HTML_ENTITIES.put("ç", new StringMemory("&ccedil;"));
            HTML_ENTITIES.put("è", new StringMemory("&egrave;"));
            HTML_ENTITIES.put("é", new StringMemory("&eacute;"));
            HTML_ENTITIES.put("ê", new StringMemory("&ecirc;"));
            HTML_ENTITIES.put("ë", new StringMemory("&euml;"));
            HTML_ENTITIES.put("ì", new StringMemory("&igrave;"));
            HTML_ENTITIES.put("í", new StringMemory("&iacute;"));
            HTML_ENTITIES.put("î", new StringMemory("&icirc;"));
            HTML_ENTITIES.put("ï", new StringMemory("&iuml;"));
            HTML_ENTITIES.put("ð", new StringMemory("&eth;"));
            HTML_ENTITIES.put("ñ", new StringMemory("&ntilde;"));
            HTML_ENTITIES.put("ò", new StringMemory("&ograve;"));
            HTML_ENTITIES.put("ó", new StringMemory("&oacute;"));
            HTML_ENTITIES.put("ô", new StringMemory("&ocirc;"));
            HTML_ENTITIES.put("õ", new StringMemory("&otilde;"));
            HTML_ENTITIES.put("ö", new StringMemory("&ouml;"));
            HTML_ENTITIES.put("÷", new StringMemory("&divide;"));
            HTML_ENTITIES.put("ø", new StringMemory("&oslash;"));
            HTML_ENTITIES.put("ù", new StringMemory("&ugrave;"));
            HTML_ENTITIES.put("ú", new StringMemory("&uacute;"));
            HTML_ENTITIES.put("û", new StringMemory("&ucirc;"));
            HTML_ENTITIES.put("ü", new StringMemory("&uuml;"));
            HTML_ENTITIES.put("ý", new StringMemory("&yacute;"));
            HTML_ENTITIES.put("þ", new StringMemory("&thorn;"));
            HTML_ENTITIES.put("ÿ", new StringMemory("&yuml;"));
            HTML_ENTITIES.put("Œ", new StringMemory("&OElig;"));
            HTML_ENTITIES.put("œ", new StringMemory("&oelig;"));
            HTML_ENTITIES.put("Š", new StringMemory("&Scaron;"));
            HTML_ENTITIES.put("š", new StringMemory("&scaron;"));
            HTML_ENTITIES.put("Ÿ", new StringMemory("&Yuml;"));
            HTML_ENTITIES.put("ƒ", new StringMemory("&fnof;"));
            HTML_ENTITIES.put("ˆ", new StringMemory("&circ;"));
            HTML_ENTITIES.put("˜", new StringMemory("&tilde;"));
            HTML_ENTITIES.put("Α", new StringMemory("&Alpha;"));
            HTML_ENTITIES.put("Β", new StringMemory("&Beta;"));
            HTML_ENTITIES.put("Γ", new StringMemory("&Gamma;"));
            HTML_ENTITIES.put("Δ", new StringMemory("&Delta;"));
            HTML_ENTITIES.put("Ε", new StringMemory("&Epsilon;"));
            HTML_ENTITIES.put("Ζ", new StringMemory("&Zeta;"));
            HTML_ENTITIES.put("Η", new StringMemory("&Eta;"));
            HTML_ENTITIES.put("Θ", new StringMemory("&Theta;"));
            HTML_ENTITIES.put("Ι", new StringMemory("&Iota;"));
            HTML_ENTITIES.put("Κ", new StringMemory("&Kappa;"));
            HTML_ENTITIES.put("Λ", new StringMemory("&Lambda;"));
            HTML_ENTITIES.put("Μ", new StringMemory("&Mu;"));
            HTML_ENTITIES.put("Ν", new StringMemory("&Nu;"));
            HTML_ENTITIES.put("Ξ", new StringMemory("&Xi;"));
            HTML_ENTITIES.put("Ο", new StringMemory("&Omicron;"));
            HTML_ENTITIES.put("Π", new StringMemory("&Pi;"));
            HTML_ENTITIES.put("Ρ", new StringMemory("&Rho;"));
            HTML_ENTITIES.put("Σ", new StringMemory("&Sigma;"));
            HTML_ENTITIES.put("Τ", new StringMemory("&Tau;"));
            HTML_ENTITIES.put("Υ", new StringMemory("&Upsilon;"));
            HTML_ENTITIES.put("Φ", new StringMemory("&Phi;"));
            HTML_ENTITIES.put("Χ", new StringMemory("&Chi;"));
            HTML_ENTITIES.put("Ψ", new StringMemory("&Psi;"));
            HTML_ENTITIES.put("Ω", new StringMemory("&Omega;"));
            HTML_ENTITIES.put("α", new StringMemory("&alpha;"));
            HTML_ENTITIES.put("β", new StringMemory("&beta;"));
            HTML_ENTITIES.put("γ", new StringMemory("&gamma;"));
            HTML_ENTITIES.put("δ", new StringMemory("&delta;"));
            HTML_ENTITIES.put("ε", new StringMemory("&epsilon;"));
            HTML_ENTITIES.put("ζ", new StringMemory("&zeta;"));
            HTML_ENTITIES.put("η", new StringMemory("&eta;"));
            HTML_ENTITIES.put("θ", new StringMemory("&theta;"));
            HTML_ENTITIES.put("ι", new StringMemory("&iota;"));
            HTML_ENTITIES.put("κ", new StringMemory("&kappa;"));
            HTML_ENTITIES.put("λ", new StringMemory("&lambda;"));
            HTML_ENTITIES.put("μ", new StringMemory("&mu;"));
            HTML_ENTITIES.put("ν", new StringMemory("&nu;"));
            HTML_ENTITIES.put("ξ", new StringMemory("&xi;"));
            HTML_ENTITIES.put("ο", new StringMemory("&omicron;"));
            HTML_ENTITIES.put("π", new StringMemory("&pi;"));
            HTML_ENTITIES.put("ρ", new StringMemory("&rho;"));
            HTML_ENTITIES.put("ς", new StringMemory("&sigmaf;"));
            HTML_ENTITIES.put("σ", new StringMemory("&sigma;"));
            HTML_ENTITIES.put("τ", new StringMemory("&tau;"));
            HTML_ENTITIES.put("υ", new StringMemory("&upsilon;"));
            HTML_ENTITIES.put("φ", new StringMemory("&phi;"));
            HTML_ENTITIES.put("χ", new StringMemory("&chi;"));
            HTML_ENTITIES.put("ψ", new StringMemory("&psi;"));
            HTML_ENTITIES.put("ω", new StringMemory("&omega;"));
            HTML_ENTITIES.put("ϑ", new StringMemory("&thetasym;"));
            HTML_ENTITIES.put("ϒ", new StringMemory("&upsih;"));
            HTML_ENTITIES.put("ϖ", new StringMemory("&piv;"));
            HTML_ENTITIES.put(" ", new StringMemory("&ensp;"));
            HTML_ENTITIES.put(" ", new StringMemory("&emsp;"));
            HTML_ENTITIES.put(" ", new StringMemory("&thinsp;"));
            HTML_ENTITIES.put("‌", new StringMemory("&zwnj;"));
            HTML_ENTITIES.put("‍", new StringMemory("&zwj;"));
            HTML_ENTITIES.put("‎", new StringMemory("&lrm;"));
            HTML_ENTITIES.put("‏", new StringMemory("&rlm;"));
            HTML_ENTITIES.put("–", new StringMemory("&ndash;"));
            HTML_ENTITIES.put("—", new StringMemory("&mdash;"));
            HTML_ENTITIES.put("‘", new StringMemory("&lsquo;"));
            HTML_ENTITIES.put("’", new StringMemory("&rsquo;"));
            HTML_ENTITIES.put("‚", new StringMemory("&sbquo;"));
            HTML_ENTITIES.put("“", new StringMemory("&ldquo;"));
            HTML_ENTITIES.put("”", new StringMemory("&rdquo;"));
            HTML_ENTITIES.put("„", new StringMemory("&bdquo;"));
            HTML_ENTITIES.put("†", new StringMemory("&dagger;"));
            HTML_ENTITIES.put("‡", new StringMemory("&Dagger;"));
            HTML_ENTITIES.put("•", new StringMemory("&bull;"));
            HTML_ENTITIES.put("…", new StringMemory("&hellip;"));
            HTML_ENTITIES.put("‰", new StringMemory("&permil;"));
            HTML_ENTITIES.put("′", new StringMemory("&prime;"));
            HTML_ENTITIES.put("″", new StringMemory("&Prime;"));
            HTML_ENTITIES.put("‹", new StringMemory("&lsaquo;"));
            HTML_ENTITIES.put("›", new StringMemory("&rsaquo;"));
            HTML_ENTITIES.put("‾", new StringMemory("&oline;"));
            HTML_ENTITIES.put("⁄", new StringMemory("&frasl;"));
            HTML_ENTITIES.put("€", new StringMemory("&euro;"));
            HTML_ENTITIES.put("ℑ", new StringMemory("&image;"));
            HTML_ENTITIES.put("℘", new StringMemory("&weierp;"));
            HTML_ENTITIES.put("ℜ", new StringMemory("&real;"));
            HTML_ENTITIES.put("™", new StringMemory("&trade;"));
            HTML_ENTITIES.put("ℵ", new StringMemory("&alefsym;"));
            HTML_ENTITIES.put("←", new StringMemory("&larr;"));
            HTML_ENTITIES.put("↑", new StringMemory("&uarr;"));
            HTML_ENTITIES.put("→", new StringMemory("&rarr;"));
            HTML_ENTITIES.put("↓", new StringMemory("&darr;"));
            HTML_ENTITIES.put("↔", new StringMemory("&harr;"));
            HTML_ENTITIES.put("↵", new StringMemory("&crarr;"));
            HTML_ENTITIES.put("⇐", new StringMemory("&lArr;"));
            HTML_ENTITIES.put("⇑", new StringMemory("&uArr;"));
            HTML_ENTITIES.put("⇒", new StringMemory("&rArr;"));
            HTML_ENTITIES.put("⇓", new StringMemory("&dArr;"));
            HTML_ENTITIES.put("⇔", new StringMemory("&hArr;"));
            HTML_ENTITIES.put("∀", new StringMemory("&forall;"));
            HTML_ENTITIES.put("∂", new StringMemory("&part;"));
            HTML_ENTITIES.put("∃", new StringMemory("&exist;"));
            HTML_ENTITIES.put("∅", new StringMemory("&empty;"));
            HTML_ENTITIES.put("∇", new StringMemory("&nabla;"));
            HTML_ENTITIES.put("∈", new StringMemory("&isin;"));
            HTML_ENTITIES.put("∉", new StringMemory("&notin;"));
            HTML_ENTITIES.put("∋", new StringMemory("&ni;"));
            HTML_ENTITIES.put("∏", new StringMemory("&prod;"));
            HTML_ENTITIES.put("∑", new StringMemory("&sum;"));
            HTML_ENTITIES.put("−", new StringMemory("&minus;"));
            HTML_ENTITIES.put("∗", new StringMemory("&lowast;"));
            HTML_ENTITIES.put("√", new StringMemory("&radic;"));
            HTML_ENTITIES.put("∝", new StringMemory("&prop;"));
            HTML_ENTITIES.put("∞", new StringMemory("&infin;"));
            HTML_ENTITIES.put("∠", new StringMemory("&ang;"));
            HTML_ENTITIES.put("∧", new StringMemory("&and;"));
            HTML_ENTITIES.put("∨", new StringMemory("&or;"));
            HTML_ENTITIES.put("∩", new StringMemory("&cap;"));
            HTML_ENTITIES.put("∪", new StringMemory("&cup;"));
            HTML_ENTITIES.put("∫", new StringMemory("&int;"));
            HTML_ENTITIES.put("∴", new StringMemory("&there4;"));
            HTML_ENTITIES.put("∼", new StringMemory("&sim;"));
            HTML_ENTITIES.put("≅", new StringMemory("&cong;"));
            HTML_ENTITIES.put("≈", new StringMemory("&asymp;"));
            HTML_ENTITIES.put("≠", new StringMemory("&ne;"));
            HTML_ENTITIES.put("≡", new StringMemory("&equiv;"));
            HTML_ENTITIES.put("≤", new StringMemory("&le;"));
            HTML_ENTITIES.put("≥", new StringMemory("&ge;"));
            HTML_ENTITIES.put("⊂", new StringMemory("&sub;"));
            HTML_ENTITIES.put("⊃", new StringMemory("&sup;"));
            HTML_ENTITIES.put("⊄", new StringMemory("&nsub;"));
            HTML_ENTITIES.put("⊆", new StringMemory("&sube;"));
            HTML_ENTITIES.put("⊇", new StringMemory("&supe;"));
            HTML_ENTITIES.put("⊕", new StringMemory("&oplus;"));
            HTML_ENTITIES.put("⊗", new StringMemory("&otimes;"));
            HTML_ENTITIES.put("⊥", new StringMemory("&perp;"));
            HTML_ENTITIES.put("⋅", new StringMemory("&sdot;"));
            HTML_ENTITIES.put("⌈", new StringMemory("&lceil;"));
            HTML_ENTITIES.put("⌉", new StringMemory("&rceil;"));
            HTML_ENTITIES.put("⌊", new StringMemory("&lfloor;"));
            HTML_ENTITIES.put("⌋", new StringMemory("&rfloor;"));
            HTML_ENTITIES.put("〈", new StringMemory("&lang;"));
            HTML_ENTITIES.put("〉", new StringMemory("&rang;"));
            HTML_ENTITIES.put("◊", new StringMemory("&loz;"));
            HTML_ENTITIES.put("♠", new StringMemory("&spades;"));
            HTML_ENTITIES.put("♣", new StringMemory("&clubs;"));
            HTML_ENTITIES.put("♥", new StringMemory("&hearts;"));
            HTML_ENTITIES.put("♦", new StringMemory("&diams;"));
        }

        return HTML_ENTITIES;
    }

    private static ArrayMemory htmlSpecialCharsMap() {
        if (HTML_SPECIALCHARS == null) {
            HTML_SPECIALCHARS = new ArrayMemory();
            HTML_SPECIALCHARS.put("<", new StringMemory("&lt;"));
            HTML_SPECIALCHARS.put(">", new StringMemory("&gt;"));
            HTML_SPECIALCHARS.put("&", new StringMemory("&amp;"));

            ForeachIterator iterator = htmlEntriesMap().foreachIterator(false, false);
            while (iterator.next()) {
                HTML_SPECIALCHARS.refOfIndex(iterator.getValue()).assign(iterator.getKey().toString());
            }
        }

        return HTML_SPECIALCHARS;
    }

    static {
        DEFAULT_DECIMAL_FORMAT_SYMBOLS = new DecimalFormatSymbols();
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setDecimalSeparator('.');
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setGroupingSeparator(',');
        DEFAULT_DECIMAL_FORMAT_SYMBOLS.setZeroDigit('0');
    }
}
