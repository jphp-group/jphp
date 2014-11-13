package php.runtime.util;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.memory.StringMemory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final public class SScanF {

    private SScanF(){}

    private static void addConstant(
            List<Segment> segmentList, StringBuilder sb) {
        if (sb.length() == 0)
            return;

        segmentList.add(new ConstantSegment(sb.toString()));
        sb.setLength(0);
    }

    public static Segment[] parse(Environment env, TraceInfo trace, String format) {
        int fmtLen = format.length();
        int fIndex = 0;

        List<Segment> segmentList = new ArrayList<Segment>();
        StringBuilder sb = new StringBuilder();

        while (fIndex < fmtLen) {
            char ch = format.charAt(fIndex++);

            if (isWhitespace(ch)) {
                StringBuilder whiteSpaceValue = new StringBuilder();
                whiteSpaceValue.append(ch);
                for (;
                     (fIndex < fmtLen && isWhitespace(ch = format.charAt(fIndex)));
                     fIndex++) {
                    whiteSpaceValue.append(ch);
                }

                addConstant(segmentList, sb);
                segmentList.add(new WhitespaceSegment(whiteSpaceValue.toString()));
            } else if (ch == '%') {
                int maxLen = -1;

                loop:
                while (fIndex < fmtLen) {
                    ch = format.charAt(fIndex++);

                    switch (ch) {
                        case '%':
                            sb.append('%');
                            break loop;

                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            if (maxLen < 0)
                                maxLen = 0;

                            maxLen = 10 * maxLen + ch - '0';
                            break;

                        case 's': {
                            addConstant(segmentList, sb);
                            segmentList.add(new StringSegment(maxLen, ch));
                            break loop;
                        }

                        case 'c': {
                            if (maxLen < 0)
                                maxLen = 1;

                            addConstant(segmentList, sb);
                            segmentList.add(new StringSegment(maxLen, ch));

                            break loop;
                        }
                        case 'n': {
                            addConstant(segmentList, sb);
                            segmentList.add(StringLengthSegment.SEGMENT);

                            break loop;
                        }
                        case 'd': {
                            addConstant(segmentList, sb);
                            segmentList.add(new IntegerSegment(maxLen, 10, false));
                            break loop;
                        }
                        case 'u': {
                            addConstant(segmentList, sb);
                            segmentList.add(new IntegerSegment(maxLen, 10, true));
                            break loop;
                        }
                        case 'o': {
                            addConstant(segmentList, sb);
                            segmentList.add(new IntegerSegment(maxLen, 8, false));
                            break loop;
                        }
                        case 'x':
                        case 'X': {
                            addConstant(segmentList, sb);
                            segmentList.add(new HexSegment(maxLen));

                            break loop;
                        }
                        case 'e':
                        case 'f': {
                            addConstant(segmentList, sb);
                            segmentList.add(new ScientificSegment(maxLen, ch));
                            break loop;
                        }
                        case '[': {
                            addConstant(segmentList, sb);

                            if (fmtLen <= fIndex) {
                                if (env != null)
                                    env.warning(trace, "expected ']', saw end of string");
                                break loop;
                            }

                            boolean isNegated = false;

                            if (fIndex < fmtLen
                                    && format.charAt(fIndex) == '^') {
                                isNegated = true;
                                fIndex++;
                            }

                            Set<Integer> set = new HashSet<Integer>();
                            while (true) {
                                if (fIndex == fmtLen) {
                                    if (env != null)
                                        env.warning(trace, "expected ']', saw end of string");
                                    break loop;
                                }

                                char ch2 = format.charAt(fIndex++);

                                if (ch2 == ']') {
                                    break;
                                } else {
                                    set.add((int) ch2);
                                }
                            }

                            if (isNegated)
                                segmentList.add(new SetNegatedSegment(set));
                            else
                                segmentList.add(new SetSegment(set));

                            break loop;
                        }
                        default:
                            env.warning(trace, "'%s' is a bad sscanf string", format);

                            // XXX:
                            //return isAssign ? LongValue.create(argIndex) : array;
                            break loop;
                    }
                }
            } else
                sb.append(ch);
        }

        addConstant(segmentList, sb);

        Segment[] segmentArray = new Segment[segmentList.size()];
        return segmentList.toArray(segmentArray);
    }

    protected static boolean isWhitespace(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    public abstract static class Segment {
        abstract public boolean isAssigned();

        abstract public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray);

        void sscanfPut(Memory var, Memory val, boolean isReturnArray) {
            if (isReturnArray)
                var.refOfPush().assign(val);
            else
                var.assign(val);
        }

        public String getFormatString(){
            return "";
        }
    }

    static class ConstantSegment extends Segment {
        private final String _string;
        private final int _strlen;

        private ConstantSegment(String string) {
            _string = string;
            _strlen = string.length();
        }

        @Override
        public boolean isAssigned() {
            return false;
        }

        @Override
        public String getFormatString() {
            return _string;
        }

        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            int fStrlen = _strlen;
            String fString = _string;

            if (strlen - sIndex < fStrlen)
                return -1;

            for (int i = 0; i < fStrlen; i++) {
                char ch = string.charAt(sIndex++);
                char ch2 = fString.charAt(i);
                if (ch != ch2)
                    return -1;
            }

            return sIndex;
        }
    }

    static class WhitespaceSegment extends Segment {
        private String originValue = " ";

        private WhitespaceSegment(String originValue) {
            this.originValue = originValue;
        }

        @Override
        public String getFormatString() {
            return originValue;
        }

        @Override
        public boolean isAssigned() {
            return false;
        }

        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            for (;
                 sIndex < strlen && isWhitespace(string.charAt(sIndex));
                 sIndex++) {
            }

            return sIndex;
        }
    }

    static class StringLengthSegment extends Segment {
        static final StringLengthSegment SEGMENT = new StringLengthSegment();

        private StringLengthSegment() {
        }

        @Override
        public String getFormatString() {
            return "%n";
        }

        @Override
        public boolean isAssigned() {
            return true;
        }

        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            sscanfPut(var, LongMemory.valueOf(sIndex), isReturnArray);
            return sIndex;
        }
    }

    static class SetSegment extends Segment {
        private Set<Integer> _set;

        private SetSegment(Set<Integer> set) {
            _set = set;
        }

        @Override
        public boolean isAssigned() {
            return true;
        }

        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            StringBuilder sb = new StringBuilder();

            for (; sIndex < strlen; sIndex++) {
                char ch = string.charAt(sIndex);

                if (_set.contains((int) ch)) {
                    sb.append(ch);
                } else {
                    break;
                }
            }

            if (sb.length() > 0)
                sscanfPut(var, new StringMemory(sb.toString()), isReturnArray);
            else if (isReturnArray)
                var.refOfPush().assign(Memory.NULL);

            return sIndex;
        }
    }

    static class SetNegatedSegment extends Segment {
        private Set<Integer> _set;

        private SetNegatedSegment(Set<Integer> set) {
            _set = set;
        }

        @Override
        public boolean isAssigned() {
            return true;
        }

        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            StringBuilder sb = new StringBuilder();

            for (; sIndex < strlen; sIndex++) {
                char ch = string.charAt(sIndex);

                if (!_set.contains((int) ch)) {
                    sb.append(ch);
                } else {
                    break;
                }
            }

            if (sb.length() > 0)
                sscanfPut(var, new StringMemory(sb.toString()), isReturnArray);
            else if (isReturnArray)
                var.refOfPush().assign(Memory.NULL);

            return sIndex;
        }
    }

    static class ScientificSegment extends Segment {
        private final int _maxLen;
        private final char _ch;

        ScientificSegment(int maxLen, char ch) {
            if (maxLen < 0)
                maxLen = Integer.MAX_VALUE;

            _maxLen = maxLen;
            _ch = ch;
        }

        @Override
        public String getFormatString() {
            return "%" + String.valueOf(_ch);
        }

        @Override
        public boolean isAssigned() {
            return true;
        }

        @Override
        public int apply(String s, int strlen, int i, ReferenceMemory var, boolean isReturnArray) {
            if (i == strlen) {
                if (isReturnArray)
                    var.refOfPush().assign(Memory.NULL);

                return i;
            }

            int start = i;
            int len = strlen;
            int ch = 0;

            int maxLen = _maxLen;

            if (i < len && maxLen > 0 && ((ch = s.charAt(i)) == '+' || ch == '-')) {
                i++;
                maxLen--;
            }

            for (; i < len && maxLen > 0
                    && '0' <= (ch = s.charAt(i)) && ch <= '9'; i++) {
                maxLen--;
            }

            if (ch == '.') {
                maxLen--;

                for (i++; i < len && maxLen > 0
                        && '0' <= (ch = s.charAt(i)) && ch <= '9'; i++) {
                    maxLen--;
                }
            }

            if (ch == 'e' || ch == 'E') {
                maxLen--;

                int e = i++;

                if (start == e) {
                    sscanfPut(var, Memory.NULL, isReturnArray);
                    return start;
                }

                if (i < len && maxLen > 0 && (ch = s.charAt(i)) == '+' || ch == '-') {
                    i++;
                    maxLen--;
                }

                for (; i < len && maxLen > 0
                        && '0' <= (ch = s.charAt(i)) && ch <= '9'; i++) {
                    maxLen--;
                }

                if (i == e + 1)
                    i = e;
            }

            double val;

            if (i == 0)
                val = 0;
            else
                val = Double.parseDouble(s.substring(start, i));

            sscanfPut(var, new DoubleMemory(val), isReturnArray);
            return i;
        }
    }

    static class HexSegment extends Segment {
        private final int _maxLen;

        HexSegment(int maxLen) {
            if (maxLen < 0)
                maxLen = Integer.MAX_VALUE;

            _maxLen = maxLen;
        }

        @Override
        public String getFormatString() {
            return "%x";
        }

        @Override
        public boolean isAssigned() {
            return true;
        }

        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            if (sIndex == strlen) {
                if (isReturnArray)
                    var.refOfPush().assign(Memory.NULL);

                return sIndex;
            }

            int val = 0;
            int sign = 1;
            boolean isMatched = false;

            int maxLen = _maxLen;

            if (sIndex < strlen) {
                char ch = string.charAt(sIndex);

                if (ch == '+') {
                    sIndex++;
                    maxLen--;
                } else if (ch == '-') {
                    sign = -1;

                    sIndex++;
                    maxLen--;
                }
            }

            for (; sIndex < strlen && maxLen-- > 0; sIndex++) {
                char ch = string.charAt(sIndex);

                if ('0' <= ch && ch <= '9') {
                    val = val * 16 + ch - '0';
                    isMatched = true;
                } else if ('a' <= ch && ch <= 'f') {
                    val = val * 16 + ch - 'a' + 10;
                    isMatched = true;
                } else if ('A' <= ch && ch <= 'F') {
                    val = val * 16 + ch - 'A' + 10;
                    isMatched = true;
                } else if (!isMatched) {
                    sscanfPut(var, Memory.NULL, isReturnArray);
                    return sIndex;
                } else
                    break;
            }

            sscanfPut(var, LongMemory.valueOf(val * sign), isReturnArray);
            return sIndex;
        }
    }

    static class IntegerSegment extends Segment {
        private final int _maxLen;
        private final int _base;
        private final boolean _isUnsigned;

        IntegerSegment(int maxLen, int base, boolean isUnsigned) {
            if (maxLen < 0)
                maxLen = Integer.MAX_VALUE;

            _maxLen = maxLen;

            _base = base;
            _isUnsigned = isUnsigned;
        }

        @Override
        public String getFormatString() {
            switch (_base){
                case 10: return _isUnsigned ? "%u" : "%d";
                case 8: return "%o";
            }

            return "%d";
        }

        @Override
        public boolean isAssigned() {
            return true;
        }

        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            if (sIndex == strlen) {
                if (isReturnArray)
                    var.refOfPush().assign(Memory.NULL);

                return sIndex;
            }

            // XXX: 32-bit vs 64-bit
            int val = 0;

            int sign = 1;
            boolean isNotMatched = true;

            int maxLen = _maxLen;

            if (sIndex < strlen) {
                char ch = string.charAt(sIndex);

                if (ch == '+') {
                    sIndex++;
                    maxLen--;
                } else if (ch == '-') {
                    sign = -1;

                    sIndex++;
                    maxLen--;
                }
            }

            int base = _base;

            int topRange = base + '0';

            for (; sIndex < strlen && maxLen-- > 0; sIndex++) {
                char ch = string.charAt(sIndex);

                if ('0' <= ch && ch < topRange) {
                    val = val * base + ch - '0';
                    isNotMatched = false;
                } else if (isNotMatched) {
                    sscanfPut(var, Memory.NULL, isReturnArray);
                    return sIndex;
                } else
                    break;
            }

            if (_isUnsigned) {
                if (sign == -1 && val != 0)
                    sscanfPut(var, new StringMemory((char) (0xffffffffL - val + 1)), isReturnArray);
                else
                    sscanfPut(var, LongMemory.valueOf(val), isReturnArray);
            } else
                sscanfPut(var, LongMemory.valueOf(val * sign), isReturnArray);

            return sIndex;
        }
    }

    static class StringSegment extends Segment {
        private final int _maxLen;
        private final char _ch;

        StringSegment(int maxLen, char ch) {
            if (maxLen < 0)
                maxLen = Integer.MAX_VALUE;

            _maxLen = maxLen;
            _ch = ch;
        }

        @Override
        public String getFormatString() {
            return "%" + String.valueOf(_ch);
        }

        @Override
        public boolean isAssigned() {
            return true;
        }

        /**
         * Scans a string with a given length.
         */
        @Override
        public int apply(String string, int strlen, int sIndex, ReferenceMemory var, boolean isReturnArray) {
            if (sIndex == strlen) {
                if (isReturnArray)
                    var.refOfPush().assign(Memory.NULL);

                return sIndex;
            }

            StringBuilder sb = new StringBuilder();
            int maxLen = _maxLen;

            for (; sIndex < strlen && maxLen-- > 0; sIndex++) {
                char ch = string.charAt(sIndex);

                if (isWhitespace(ch))
                    break;

                sb.append(ch);
            }

            sscanfPut(var, new StringMemory(sb.toString()), isReturnArray);
            return sIndex;
        }
    }
}
