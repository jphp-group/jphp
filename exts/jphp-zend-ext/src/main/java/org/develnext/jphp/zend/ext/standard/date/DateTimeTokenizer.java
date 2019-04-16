package org.develnext.jphp.zend.ext.standard.date;

import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.BufferCharacteristics.DIGITS;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.BufferCharacteristics.LETTERS;
import static org.develnext.jphp.zend.ext.standard.date.DateTimeTokenizer.BufferCharacteristics.PUNCTUATION;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.regex.Pattern;

class DateTimeTokenizer {
    static final Pattern DAY_SUFFIX = Pattern.compile("st|nd|rd|th");
    static final Pattern HOUR_hh = Pattern.compile("0?[1-9]|1[0-2]");
    static final Pattern HOUR_HH = Pattern.compile("[01][0-9]|2[0-4]");
    static final Pattern HH_MM = Pattern.compile("([01][0-9]|2[0-4])(0[0-9]|1[0-2])");
    static final Pattern HH_MM_SS = Pattern.compile("([01][0-9]|2[0-4])([0-5][0-9]){2}");
    static final Pattern HOUR_12 = HOUR_hh;
    static final Pattern HOUR_24 = HOUR_HH;
    static final Pattern TWO_DIGIT_MINUTE = Pattern.compile("[0-5][0-9]");
    static final Pattern MINUTE_ii = Pattern.compile("0?[0-9]|[0-5][0-9]");
    static final Pattern SECOND_ss = MINUTE_ii;
    static final Pattern TWO_DIGIT_SECOND = TWO_DIGIT_MINUTE;
    static final Pattern FRACTION = Pattern.compile("\\.[0-9]+");
    static final Pattern MERIDIAN = Pattern.compile("[AaPp]\\.?[Mm]\\.?\t?");
    static final Pattern TWO_DIGIT_MONTH = Pattern.compile("0[0-9]|1[0-2]");
    static final Pattern MONTH_mm = Pattern.compile("0?[0-9]|1[0-2]");
    static final Pattern DAY_dd = Pattern.compile("([0-2]?[0-9]|3[01])");
    static final Pattern MONTH_M = Pattern.compile("jan|feb|mar|apr|may|jun|jul|aug|sep|sept|oct|nov|dec", Pattern.CASE_INSENSITIVE);
    static final Pattern MONTH = Pattern.compile("january|february|march|april|may|june|july|august|september|october|november|december", Pattern.CASE_INSENSITIVE);
    static final Pattern MONTH_ROMAN = Pattern.compile("I{1,3}|IV|VI{1,3}|IX|XI{0,2}");
    static final Pattern TWO_DIGIT_DAY = Pattern.compile("0[0-9]|[1-2][0-9]|3[01]");
    static final Pattern DAY_OF_YEAR = Pattern.compile("00[1-9]|0[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6]");
    static final Pattern WEEK = Pattern.compile("0[1-9]|[1-4][0-9]|5[0-3]");
    static final Pattern YEAR_y = Pattern.compile("[0-9]{1,4}");
    static final Pattern YEAR_yy = Pattern.compile("[0-9]{2}");

    private static final int UNDEFINED_POSITION = -1;
    private final char[] chars;
    private final int length;
    private final EnumSet<BufferCharacteristics> characteristics;
    private final StringBuilder buff;
    private int cursor;
    private int tokenStart;

    public DateTimeTokenizer(String dateTime) {
        this.chars = dateTime.toCharArray();
        this.length = chars.length;
        this.tokenStart = UNDEFINED_POSITION;
        this.buff = new StringBuilder();
        this.characteristics = EnumSet.noneOf(BufferCharacteristics.class);
    }

    Token next() {
        if (isEnd()) // end reached
            return Token.EOF;

        Token next = null;

        char[] chars = this.chars;
        int i = cursor;

        loop:
        while (i < length) {
            char c = chars[i];

            s0:
            switch (c) {
                case 't':
                case 'T': {
                    buff.append(c);
                    if (isNextDigit(i)) {
                        next = Token.of(Symbol.STRING, i++, 1);
                        resetBuffer();
                        break loop;
                    }
                    break;
                }
                // DIGITS
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9': {
                    if (isUndefined())
                        tokenStart = i;

                    if (Character.isLetter(lastChar())) {
                        next = createAndReset(Symbol.STRING);
                        break loop;
                    }

                    buff.append(c);
                    characteristics.add(DIGITS);

                    if (isNextDigit(i)) {
                        // next character is digit. continuing...
                        break;
                    } else {
                        // advancing pointer to non digit character
                        i++;
                        next = createAndReset(Symbol.DIGITS);

                        // break iteration and hoping to read token with digits.
                        break loop;
                    }
                }
                // PUNCTUATION
                case ':': {
                    if (isUndefined()) {
                        // empty buffer indicates that nothing has been parsed
                        next = Token.of(Symbol.COLON, i++, 1);
                    } else {
                        // buffer is not empty and we reached colon so we must return
                        next = createWithGuessedSymbol();
                    }
                    resetBuffer();
                    break loop;
                }
                case '.': {
                    if (isUndefined()) {
                        next = Token.of(Symbol.DOT, i++, 1);
                        resetBuffer();
                        break loop;
                    } else {
                        char lc = lastChar();

                        if (Character.isLetter(lc)) {
                            next = createAndReset(Symbol.STRING);
                        } else {
                            // most probably this is DIGITS
                            buff.append(c);
                            next = createWithGuessedSymbol();
                        }

                        resetBuffer();
                        break loop;
                    }
                }
                case '_':
                case '/': {
                    if (isUndefined()) {
                        next = Token.of(Symbol.STRING, i++, 1);
                    } else {
                        next = createWithGuessedSymbol();
                    }
                    resetBuffer();
                    break loop;
                }
                case '@': {
                    next = Token.of(Symbol.AT, i++, 1);
                    break loop;
                }
                case '+': {
                    if (isUndefined()) {
                        next = Token.of(Symbol.PLUS, i++, 1);
                    } else {
                        next = createWithGuessedSymbol();
                    }
                    resetBuffer();
                    break loop;
                }
                case '-': {
                    if (isUndefined()) {
                        next = Token.of(Symbol.MINUS, i++, 1);
                    } else {
                        next = createWithGuessedSymbol();
                        resetBuffer();
                    }

                    break loop;
                }
                case ' ':
                case '\t': {
                    if (isUndefined()) {
                        next = Token.of(Symbol.SPACE, i++, 1);
                    } else {
                        next = createWithGuessedSymbol();
                        resetBuffer();
                    }
                    break loop;
                }
                default: {
                    if (isUndefined())
                        tokenStart = i;

                    buff.append(c);

                    if (Character.isLetter(c)) {
                        characteristics.add(LETTERS);
                    }

                    break;
                }
            }

            i++;
        }

        cursor = i;

        // when reached and still can not find token.
        if (isEnd() && next == null) {
            Symbol symbol = guessSymbol();
            next = Token.of(symbol, tokenStart, buff.length());
            resetBuffer();
        }

        return next;
    }

    private boolean isNextDigit(int i) {
        return Character.isDigit(lookahead(i, 1));
    }

    char[] read(Token token) {
        char[] dst = new char[token.length()];
        System.arraycopy(chars, token.start(), dst, 0, dst.length);
        return dst;
    }

    String readString(Token token) {
        return String.valueOf(read(token));
    }

    CharBuffer readCharBuffer(Token token) {
        return CharBuffer.wrap(chars, token.start(), token.length()).asReadOnlyBuffer();
    }

    CharBuffer readCharBuffer(int start, int length) {
        return CharBuffer.wrap(chars, start, length).asReadOnlyBuffer();
    }

    private char lookahead(int current, int to) {
        if (current + to < length && current + to >= 0)
            return chars[current + to];

        return '\0';
    }

    private char lastChar() {
        int idx = buff.length() - 1;
        return idx >= 0 ? buff.charAt(idx) : '\0';
    }

    private Token createWithGuessedSymbol() {
        return Token.of(guessSymbol(), tokenStart, buff.length());
    }

    private Token createAndReset(Symbol symbol) {
        final Token token = Token.of(symbol, tokenStart, buff.length());
        resetBuffer();
        return token;
    }

    private boolean isEnd() {
        return cursor == length;
    }

    private boolean isUndefined() {
        return tokenStart == UNDEFINED_POSITION;
    }

    private void resetBuffer() {
        buff.setLength(0);
        characteristics.clear();
        tokenStart = UNDEFINED_POSITION;
    }

    private Symbol guessSymbol() {
        int buffLen = buff.length();
        boolean onlyDigits = hasOnly(DIGITS);

        switch (buffLen) {
            case 1:
                if (onlyDigits) {
                    return Symbol.HOUR_12;
                }
                break;
            case 2: {
                if (onlyDigits) {
                    if (HOUR_12.matcher(buff).matches()) {
                        return Symbol.HOUR_12;
                    }
                }
                break;
            }
        }

        if (onlyDigits) {
            return Symbol.DIGITS;
        }

        if (hasOnly(DIGITS, PUNCTUATION) && FRACTION.matcher(buff).matches()) {
            return Symbol.FRACTION;
        } else if (hasOnly(LETTERS)) {
            return Symbol.STRING;
        }

        throw new IllegalStateException("Cannot guest type of " + buff.toString());
    }

    private boolean hasOnly(BufferCharacteristics... many) {
        return characteristics.size() == many.length && characteristics.containsAll(Arrays.asList(many));
    }

    char readChar(Token token) {
        return chars[token.start()];
    }

    char readChar(int idx) {
        return chars[idx];
    }

    public long readLong(Token token) {
        if (token.symbol() != Symbol.DIGITS) {
            throw new IllegalArgumentException("Cannot read long from " + token.symbol());
        }

        CharBuffer cb = readCharBuffer(token);
        int start = token.start();
        return toLong(cb, start);
    }

    private long toLong(CharBuffer cb, int start) {
        long result = 0;
        int sign = 1;

        // if number is signed we should consume sign and save it
        char first = cb.get(start);
        if (first == '-' || first == '+') {
            cb.get();
            sign = first == '-' ? -1 : 1;
        }

        while (cb.hasRemaining()) {
            result *= 10;
            result += cb.get() - '0';
        }

        return result * sign;
    }

    int readInt(Token token) {
        return (int) readLong(token);
    }

    int readInt(int start, int length) {
        return (int) toLong(readCharBuffer(start, length), start);
    }

    enum BufferCharacteristics {
        DIGITS,
        LETTERS,
        PUNCTUATION
    }
}
