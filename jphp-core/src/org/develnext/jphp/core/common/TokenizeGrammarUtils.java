package org.develnext.jphp.core.common;

import org.develnext.jphp.core.tokenizer.token.expr.value.StringExprToken;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TokenizeGrammarUtils {
    public final static String CLOSE_TAG = "?>";
    public final static String OPEN_TAG = "<?";
    public final static String CLOSE_COMMENT = "*/";

    private final static Set<Character> DELIMITERS = new HashSet<>();

    static {
        DELIMITERS.addAll(Arrays.asList(
                '~', '+', '-', '=', '/', '*', ':', '<', '>', '!', '?', '%',
                '@', '&', '^', '.', '|',
                '(', '[', '{', '}', ']', ')',
                ';', ',', '"', '\'', '\t', '\n', '\r', ' '
        ));
    }

    public static boolean isEngLetter(char ch){
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }

    public static boolean isNameChar(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || (c == '_')
                || (c >= '0' && c <= '9')
                || (c >= 127);
    }

    public static boolean isVariableChar(char ch){
        return ch == '$';
    }

    public static boolean isSpace(char ch){
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    public static boolean isNumeric(char ch) {
        return Character.isDigit(ch);
    }

    public static boolean isFloatDot(char ch){
        return '.' == ch;
    }

    public static StringExprToken.Quote isQuote(char ch){
        switch (ch) {
            case '\'': return StringExprToken.Quote.SINGLE;
            case '"': return StringExprToken.Quote.DOUBLE;
            case '`': return StringExprToken.Quote.SHELL;
        }

        return null;
    }

    public static boolean isBackslash(char ch){
        return ch == '\\';
    }

    public static boolean isDelimiter(char ch) {
        return DELIMITERS.contains(ch);
    }

    public static boolean isNewline(char ch){
        return ch == '\n';
    }

    public static boolean isCloseTag(String word){
        return CLOSE_TAG.equals(word);
    }


    public static boolean isCloseTag(char... chars) {
        int len = CLOSE_TAG.length();

        if (chars.length != len) return false;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != CLOSE_TAG.charAt(i)) return false;
        }

        return true;
    }


    public static boolean isOpenTag(String word) {
        return OPEN_TAG.equals(word);
    }

    public static boolean isOpenTag(char... chars) {
        int len = OPEN_TAG.length();

        if (chars.length != len) return false;

        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != OPEN_TAG.charAt(i)) return false;
        }

        return true;
    }

    public static boolean isCloseComment(String word){
        return CLOSE_COMMENT.equals(word);
    }
}
