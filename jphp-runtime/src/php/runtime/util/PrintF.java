package php.runtime.util;

import php.runtime.Memory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PrintF {
    private static final BigInteger BIG_2_64 = BigInteger.ONE.shiftLeft(64);
    private static final BigInteger BIG_TEN = new BigInteger("10");

    private final String format;
    private final Locale locale;
    protected final Memory[] args;

    public PrintF(Locale locale, String format, Memory[] args){
        this.locale = locale;
        this.format = format;
        this.args   = args;
    }

    private List<Segment> parse(){
        List<Segment> segments = new ArrayList<Segment>();

        int length = format.length();
        int start = 0;
        int index = 0;

        StringBuilder sb = new StringBuilder();
        StringBuilder flags = new StringBuilder();

        for(int i = 0; i < length; i++) {
            char ch = format.charAt(i);
            if (i + 1 < length && ch == '%'){
                sb.append(ch);

                boolean isLeft = false;
                boolean isAlt = false;

                boolean isShowSign = false;

                int argIndex = -1;
                int leftPadLength = 0;
                int width = 0;

                int padChar = -1;

                flags.setLength(0);

                int j = i + 1;

                loop:
                for(; j < length; j++){
                    ch = format.charAt(j);
                    switch (ch){
                        case '-':
                            isLeft = true;

                            if (j + 1 < length && format.charAt(j + 1) == '0') {
                                padChar = '0';
                                j++;
                            }
                            break;
                        case '#':
                            isAlt = true;
                            break;
                        case '0':
                        case '1': case '2': case '3': case '4': case '5':
                        case '6': case '7': case '8': case '9':
                            if (ch == '0' && padChar < 0)
                                padChar = '0';
                            else {
                                int value = ch - '0';
                                for (int k = j + 1; k < length; k++) {
                                    char digit = format.charAt(k);

                                    if (Character.isDigit(digit)) {
                                        value = value * 10 + digit - '0';
                                        j++;
                                    } else
                                        break;
                                }

                                if (j + 1 < length && format.charAt(j + 1) == '$') {
                                    argIndex = value - 1;
                                    j++;
                                } else {
                                    width = value;
                                }
                            }
                            break;

                        case '\'':
                            padChar = format.charAt(j + 1);
                            j += 1;
                            break;
                        case '+':
                            isShowSign = true;
                            break;
                        case ' ': case ',': case '(':
                            flags.append(ch);
                            break;
                        default:
                            break loop;
                    }
                }

                int head = j;

                if (argIndex < 0)
                    argIndex = index;

                loop:
                for (; j < length; j++) {
                    ch = format.charAt(j);

                    switch (ch) {
                        case '%':
                            i = j;
                            segments.add(new TextSegment(sb.toString()));
                            sb.setLength(0);
                            break loop;

                        case '0': case '1': case '2': case '3': case '4':
                        case '5': case '6': case '7': case '8': case '9':
                        case '.': case '$':
                            break;


                        case 's': case 'S':
                            sb.setLength(sb.length() - 1);

                            if (width <= 0 && 0 < leftPadLength)
                                width = leftPadLength;

                            index++;

                            segments.add(new StringSegment(
                                    sb, isLeft || isAlt, padChar, ch == 'S', width, format.substring(head, j), argIndex
                            ));
                            sb.setLength(0);
                            i = j;
                            break loop;

                        case 'c': case 'C':
                            sb.setLength(sb.length() - 1);

                            if (width <= 0 && 0 < leftPadLength)
                                width = leftPadLength;

                            index++;

                            segments.add(new CharSegment(
                                    sb, isLeft || isAlt, padChar, ch == 'C', width, format.substring(head, j), argIndex
                            ));
                            sb.setLength(0);
                            i = j;
                            break loop;

                        case 'i':
                            ch = 'd';
                        case 'd': case 'x': case 'o': case 'X':
                        case 'b': case 'B': case 'u':
                            sb.setLength(sb.length() - 1);
                            if (sb.length() > 0)
                                segments.add(new TextSegment(sb.toString()));

                            sb.setLength(0);

                            if (isAlt)
                                sb.append('#');

                            if (isShowSign)
                                sb.append('+');

                            sb.append(flags);

                            if (width > 0) {
                                if (isLeft)
                                    sb.append('-');
                                else if (padChar == '0')
                                    sb.append('0');

                                sb.append(width);
                            }

                            sb.append(format, head, j);
                            sb.append(ch);

                            index++;

                            segments.add(LongSegment.valueOf(sb.toString(), argIndex));
                            sb.setLength(0);
                            i = j;
                            break loop;

                        case 'e': case 'E': case 'f': case 'g': case 'G':
                        case 'F':

                            Locale _locale = locale;
                            if (ch == 'F')
                                ch = 'f';
                            else
                                _locale = null;

                            sb.setLength(sb.length() - 1);
                            if (sb.length() > 0)
                                segments.add(new TextSegment(sb.toString()));
                            sb.setLength(0);

                            if (isAlt)
                                sb.append('#');

                            if (isShowSign)
                                sb.append('+');

                            sb.append(flags);

                            if (width > 0) {
                                if (isLeft)
                                    sb.append('-');
                                else if (padChar == '0')
                                    sb.append('0');

                                sb.append(width);
                            }

                            sb.append(format, head, j);
                            sb.append(ch);

                            index++;

                            segments.add(new DoubleSegment(
                                    sb.toString(), isLeft && padChar == '0', argIndex, _locale
                            ));
                            sb.setLength(0);
                            i = j;

                            break loop;

                        default:
                            if (isLeft)
                                sb.append('-');
                            if (isAlt)
                                sb.append('#');
                            sb.append(flags);
                            sb.append(format, head, j);
                            sb.append(ch);
                            i = j;
                            break loop;
                    }
                }
            } else
                sb.append(ch);
        }

        if (sb.length() > 0)
            segments.add(new TextSegment(sb.toString()));

        return segments;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        for(Segment segment : parse()){
            if (!segment.apply(locale, builder, args))
                return null;
        }
        return builder.toString();
    }

    abstract public static class Segment {
        protected final String format;

        public Segment(String format){
            this.format = format;
        }

        static boolean hasIndex(String format) {
            return format.indexOf('$') >= 0;
        }

        static int getIndex(String format) {
            int value = 0;

            for (int i = 0; i < format.length(); i++) {
                char ch;

                if ('0' <= (ch = format.charAt(i)) && ch <= '9')
                    value = 10 * value + ch - '0';
                else
                    break;
            }

            return value - 1;
        }

        static String getIndexFormat(String format) {
            int p = format.indexOf('$');

            return '%' + format.substring(p + 1);
        }

        abstract public Memory.Type getType();

        abstract protected boolean apply(Locale locale, StringBuilder sb, Memory[] args);
    }

    /**
     * %c
     */
    static class CharSegment extends StringSegment {
        public CharSegment(StringBuilder prefix, boolean isLeft, int pad, boolean isUpper, int width, String format, int index) {
            super(prefix, isLeft, pad, isUpper, width, format, index);
        }

        @Override
        public Memory.Type getType() {
            return Memory.Type.INT;
        }

        @Override
        protected String toValue(Memory[] args) {
            return String.valueOf(args[_index].toChar());
        }
    }

    public class TextSegment extends Segment {
        public TextSegment(String format) {
            super(format);
        }

        @Override
        public Memory.Type getType() {
            return Memory.Type.STRING;
        }

        @Override
        protected boolean apply(Locale locale, StringBuilder sb, Memory[] args) {
            sb.append(format);
            return true;
        }
    }

    /**
     * %s
     */
    static class StringSegment extends Segment {
        protected final char []_prefix;
        protected final int _min;
        protected final int _max;
        protected final boolean _isLeft;
        protected final boolean _isUpper;
        protected final char _pad;
        protected final int _index;

        public StringSegment(StringBuilder prefix,
                             boolean isLeft, int pad, boolean isUpper,
                             int width,
                             String format, int index) {
            super(format);

            _prefix = new char[prefix.length()];

            _isLeft = isLeft;
            _isUpper = isUpper;

            if (pad >= 0)
                _pad = (char) pad;
            else
                _pad = ' ';

            prefix.getChars(0, _prefix.length, _prefix, 0);

            if (hasIndex(format)) {
                index = getIndex(format);
                format = getIndexFormat(format);
            }

            int i = 0;
            int len = format.length();

            int max = Integer.MAX_VALUE;
            char ch;

            if (0 < len && format.charAt(0) == '.') {
                max = 0;

                for (i++; i < len && '0' <= (ch = format.charAt(i)) && ch <= '9'; i++) {
                    max = 10 * max + ch - '0';
                }
            }

            _min = width;
            _max = max;

            _index = index;
        }

        @Override
        public Memory.Type getType() {
            return Memory.Type.STRING;
        }

        protected String toValue(Memory[] args){
            return args[_index].toString();
        }

        @Override
        protected boolean apply(Locale locale, StringBuilder sb, Memory[] args) {
            sb.append(_prefix, 0, _prefix.length);
            if (_index >= args.length)
                return false;

            String value = toValue(args);

            int len = value.length();

            if (_max < len) {
                value = value.substring(0, _max);
                len = _max;
            }

            if (_isUpper)
                value = value.toUpperCase();

            if (!_isLeft) {
                for (int i = len; i < _min; i++) {
                    sb.append(_pad);
                }
            }

            sb.append(value);

            if (_isLeft) {
                for (int i = len; i < _min; i++) {
                    sb.append(_pad);
                }
            }
            return true;
        }
    }

    static class LongSegment extends Segment {
        protected int _index;

        LongSegment(String format, int _index) {
            super(format);
            this._index = _index;
        }

        static Segment valueOf(String format, int index) {
            if (hasIndex(format)) {
                index = getIndex(format);
                format = getIndexFormat(format);
            } else {
                format = '%' + format;
            }

            if (format.length() > 1 && format.charAt(1) == '.') {
                int i;

                for (i = 2; i < format.length(); i++) {
                    char ch = format.charAt(i);
                    if (! (Character.isDigit(ch)))
                        break;
                }

                format = '%' + format.substring(i);
            }

            if (format.charAt(format.length() - 1) == 'x'
                    || format.charAt(format.length() - 1) == 'X') {
                HexSegment hex = HexSegment.valueOf(format, index);

                if (hex != null)
                    return hex;
            }

            if (format.charAt(format.length() - 1) == 'b'
                    || format.charAt(format.length() - 1) == 'B') {
                BinarySegment bin = BinarySegment.valueOf(format, index);

                if (bin != null)
                    return bin;
            }

            if (format.charAt(format.length() - 1) == 'u') {
                UnsignedSegment unsign = UnsignedSegment.valueOf(format, index);

                if (unsign != null)
                    return unsign;
            }

            return new LongSegment(format, index);
        }

        @Override
        public Memory.Type getType() {
            return Memory.Type.INT;
        }

        @Override
        protected boolean apply(Locale locale, StringBuilder sb, Memory[] args) {
            long value;

            if (_index < args.length)
                value = args[_index].toLong();
            else {
                return false;
            }

            sb.append(String.format(Locale.ENGLISH, format, value));
            return true;
        }
    }


    static class HexSegment extends Segment {
        private final int _index;
        private final int _min;
        private final char _pad;
        private boolean _isUpper;

        HexSegment(String format, int index, int min, int pad, boolean isUpper) {
            super(format);
            _index = index;
            _min = min;

            if (pad >= 0)
                _pad = (char) pad;
            else
                _pad = ' ';

            _isUpper = isUpper;
        }

        static HexSegment valueOf(String format, int index) {
            int length = format.length();
            int offset = 1;

            boolean isUpper = format.charAt(length - 1) == 'X';
            char pad = ' ';

            if (format.charAt(offset) == ' ') {
                pad = ' ';
                offset++;
            }
            else if (format.charAt(offset) == '0') {
                pad = '0';
                offset++;
            }

            int min = 0;
            for (; offset < length - 1; offset++) {
                char ch = format.charAt(offset);

                if ('0' <= ch && ch <= '9')
                    min = 10 * min + ch - '0';
                else
                    return null;
            }

            return new HexSegment(format, index, min, pad, isUpper);
        }

        @Override
        public Memory.Type getType() {
            return Memory.Type.INT;
        }

        @Override
        public boolean apply(Locale locale, StringBuilder sb, Memory []args) {
            long value;

            if (_index >= 0 && _index < args.length)
                value = args[_index].toLong();
            else
                return false;

            int digits = 0;

            long shift = value;
            for (int i = 0; i < 16; i++) {
                if (shift != 0)
                    digits = i;

                shift = shift >>> 4;
            }

            for (int i = digits + 1; i < _min; i++)
                sb.append(_pad);

            for (; digits >= 0; digits--) {
                int digit = (int) (value >>> (4 * digits)) & 0xf;

                if (digit <= 9)
                    sb.append((char) ('0' + digit));
                else if (_isUpper)
                    sb.append((char) ('A' + digit - 10));
                else
                    sb.append((char) ('a' + digit - 10));
            }

            return true;
        }
    }


    static class BinarySegment extends Segment {
        private final int _index;
        private final int _min;
        private final char _pad;

        @Override
        public Memory.Type getType() {
            return Memory.Type.INT;
        }

        BinarySegment(String format, int index, int min, int pad) {
            super(format);
            _index = index;
            _min = min;

            if (pad >= 0)
                _pad = (char) pad;
            else
                _pad = ' ';
        }

        static BinarySegment valueOf(String format, int index) {
            int length = format.length();
            int offset = 1;

            char pad = ' ';

            if (format.charAt(offset) == ' ') {
                pad = ' ';
                offset++;
            } else if (format.charAt(offset) == '0') {
                pad = '0';
                offset++;
            }

            int min = 0;
            for (; offset < length - 1; offset++) {
                char ch = format.charAt(offset);

                if ('0' <= ch && ch <= '9')
                    min = 10 * min + ch - '0';
                else
                    return null;
            }

            return new BinarySegment(format, index, min, pad);
        }

        @Override
        public boolean apply(Locale locale, StringBuilder sb, Memory[] args) {
            long value;

            if (_index >= 0 && _index < args.length)
                value = args[_index].toLong();
            else
                return false;

            int digits = 0;

            long shift = value;
            for (int i = 0; i < 64; i++) {
                if (shift != 0)
                    digits = i;

                shift = shift >>> 1;
            }

            for (int i = digits + 1; i < _min; i++)
                sb.append(_pad);

            for (; digits >= 0; digits--) {
                int digit = (int) (value >>> (digits)) & 0x1;

                sb.append((char) ('0' + digit));
            }

            return true;
        }
    }

    static class UnsignedSegment extends Segment {
        private final int _index;
        private final int _min;
        private final char _pad;

        @Override
        public Memory.Type getType() {
            return Memory.Type.INT;
        }

        UnsignedSegment(String format, int index, int min, int pad) {
            super(format);

            _index = index;
            _min = min;

            if (pad >= 0)
                _pad = (char) pad;
            else
                _pad = ' ';
        }

        static UnsignedSegment valueOf(String format, int index) {
            int length = format.length();
            int offset = 1;

            if (format.charAt(offset) == '+')
                offset++;

            char pad = ' ';

            if (format.charAt(offset) == ' ') {
                pad = ' ';
                offset++;
            }
            else if (format.charAt(offset) == '0') {
                pad = '0';
                offset++;
            }

            int min = 0;
            for (; offset < length - 1; offset++) {
                char ch = format.charAt(offset);

                if ('0' <= ch && ch <= '9')
                    min = 10 * min + ch - '0';
                else
                    return null;
            }

            return new UnsignedSegment(format, index, min, pad);
        }

        @Override
        public boolean apply(Locale locale, StringBuilder sb, Memory[] args) {
            long value;

            if (_index >= 0 && _index < args.length)
                value = args[_index].toLong();
            else
                return false;

            char []buf = new char[32];
            int digits = buf.length;

            if (value == 0) {
                buf[--digits] = '0';
            }
            else if (value > 0) {
                while (value != 0) {
                    int digit = (int) (value % 10);

                    buf[--digits] = (char) ('0' + digit);

                    value = value / 10;
                }
            }
            else {
                BigInteger bigInt = new BigInteger(String.valueOf(value));

                bigInt = bigInt.add(BIG_2_64);

                while (bigInt.compareTo(BigInteger.ZERO) != 0) {
                    int digit = bigInt.mod(BIG_TEN).intValue();

                    buf[--digits] = (char) ('0' + digit);

                    bigInt = bigInt.divide(BIG_TEN);
                }
            }

            for (int i = buf.length - digits; i < _min; i++)
                sb.append(_pad);

            for (; digits < buf.length; digits++) {
                sb.append(buf[digits]);
            }

            return true;
        }
    }


    static class DoubleSegment extends Segment {
        private final String _format;
        private final boolean _isLeftZero;
        private final int _index;
        private final Locale _locale;

        @Override
        public Memory.Type getType() {
            return Memory.Type.DOUBLE;
        }

        DoubleSegment(String format, boolean isLeftZero, int index, Locale locale) {
            super(format);
            if (hasIndex(format)) {
                _index = getIndex(format);
                _format = getIndexFormat(format);
            }
            else {
                _format = '%' + format;
                _index = index;
            }

            _isLeftZero = isLeftZero;
            _locale = locale;
        }

        @Override
        public boolean apply(Locale locale, StringBuilder sb, Memory[] args) {
            double value;

            if (_index < args.length)
                value = args[_index].toDouble();
            else
                return false;

            String s;
            if (_locale == null)
                s = String.format(Locale.ENGLISH, _format, value);
            else
                s = String.format(_locale, _format, value);

            if (_isLeftZero) {
                int len = s.length();

                // php/1174 "-0" not allowed by java formatter
                for (int i = 0; i < len; i++) {
                    char ch = s.charAt(i);

                    if (ch == ' ')
                        sb.append('0');
                    else
                        sb.append(ch);
                }
            } else {
                sb.append(s);
            }

            return true;
        }
    }
}
