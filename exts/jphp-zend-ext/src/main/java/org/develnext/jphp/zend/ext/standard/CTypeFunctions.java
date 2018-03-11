package org.develnext.jphp.zend.ext.standard;

import php.runtime.Memory;
import php.runtime.annotation.Runtime;
import php.runtime.ext.support.compile.FunctionsContainer;

/**
 * ctype extension
 * documentation: http://us1.php.net/manual/ru/book.ctype.php
 *
 *  Notice: supports UNICODE
 */
public class CTypeFunctions extends FunctionsContainer {

    private final static char CHAR_UNDEFINED = 0xFFFF;

    private static int tryGetChar(Memory value){
        if (value.type == Memory.Type.INT){
            int val = (int)value.toLong();
            if (val > -128 && val <= Character.MAX_CODE_POINT){
                if (val < 0)
                    val += 256;

                return val;
            }
        }
        return -1;
    }

    private static boolean isPrintableChar( char c ) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of( c );
        return (!Character.isISOControl(c)) &&
                c != CHAR_UNDEFINED &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    public static boolean ctype_apply(Memory value, Checker checker){
        int c = tryGetChar(value);
        if (c > -1)
            return checker.check((char)c);

        String str = value.toString();
        int len = str.length();
        for(int i = 0; i < len; i++){
            char ch = str.charAt(i);
            if (!checker.check(ch))
                return false;
        }
        return true;
    }

    @Runtime.Immutable
    public static boolean ctype_alnum(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isLetter(ch) || Character.isDigit(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_alpha(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isLetter(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_cntrl(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isISOControl(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_digit(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isDigit(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_graph(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isSpaceChar(ch) || isPrintableChar(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_lower(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isLowerCase(ch);
            }
        });
    }


    @Runtime.Immutable
    public static boolean ctype_print(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return isPrintableChar(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_punct(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return !Character.isDigit(ch)
                        && !Character.isLetter(ch)
                        && !Character.isSpaceChar(ch)
                        && isPrintableChar(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_space(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isSpaceChar(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_upper(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isUpperCase(ch);
            }
        });
    }

    @Runtime.Immutable
    public static boolean ctype_xdigit(Memory value){
        return ctype_apply(value, new Checker() {
            @Override
            boolean check(char ch) {
                return Character.isDigit(ch) || ((ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F'));
            }
        });
    }

    abstract private static class Checker {
        abstract boolean check(char ch);
    }
}
