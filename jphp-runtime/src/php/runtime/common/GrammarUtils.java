package php.runtime.common;

public class GrammarUtils {
    public static boolean isValidName(String name){
        return (name.matches("^[a-z_\\x7f-\\xff][a-z0-9_\\x7f-\\xff]{0,60}$"));
    }

    public static boolean isNameChar(char c) {
        return (c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')
                || (c == '_')
                || (c >= '0' && c <= '9')
                || (c >= 127);
    }

    public static boolean isNewline(char ch){
        return ch == '\n';
    }

    public static boolean isVariable(String word) {
        char ch = word.charAt(0);
        if (ch != '$' || word.length() < 2) {
            return false;
        }
        for (int i = 1; i < word.length(); i++) {
            ch = word.charAt(i);
            if ((i == 1 && Character.isDigit(ch)) || (ch != '_' && !GrammarUtils.isNameChar(ch))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isOctalInteger(String word) {
        if (word.length() < 2) { // at least 0[digit]
            return false;
        }
        char ch = word.charAt(0);
        if (ch != '0') {
            return false;
        }
        for (int i = 1; i < word.length(); i++) {
            ch = word.charAt(i);
            if (ch < '0' || ch > '7') {
                return false;
            }
        }
        return true;
    }

    public static boolean isInteger(String word) {
        char ch;
        for (int i = 0; i < word.length(); i++) {
            ch = word.charAt(i);
            if (ch < '0' || ch > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean isBinaryInteger(String word) {
        if (word.length() < 3 || word.charAt(0) != '0' || word.charAt(1) != 'b') {
            return false;
        }
        char ch;
        for (int i = 2; i < word.length(); i++) {
            ch = word.charAt(i);
            if (ch != '0' && ch != '1') {
                return false;
            }
        }
        return true;
    }

    public static boolean isHexInteger(String word) {
        char ch;
        if (word.length() < 3 || word.charAt(0) != '0') {
            return false;
        }
        ch = word.charAt(1);
        if (ch != 'x' && ch != 'X') {
            return false;
        }
        for (int i = 2; i < word.length(); i++) {
            ch = word.charAt(i);
            if (!(ch >= 'A' && ch <= 'F' || ch >= 'a' && ch <= 'f' || Character.isDigit(ch))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSimpleFloat(String word) {
        if (word.length() < 2) { // because must be at least 0. or .0
            return false;
        }
        boolean dotPresent = false;
        char ch;
        for (int i = 0; i < word.length(); i++) {
            ch = word.charAt(i);
            if (ch == '.') {
                if (dotPresent) {
                    return false;
                }
                dotPresent = true;
            } else if (!Character.isDigit(ch)) {
                return false;
            }
        }
        return dotPresent;
    }

    public static boolean isExpFloat(String word) {
        if (word.length() < 3) { // at least [digit]e[digit]
            return false;
        }
        int ePosition = word.indexOf('e');
        if (ePosition == -1) {
            ePosition = word.indexOf('E');
            if (ePosition == -1) {
                return false;
            }
        }
        String mantissa = word.substring(0, ePosition);
        if (!isInteger(mantissa) && !isSimpleFloat(mantissa)) {
            return false; //invalid mantissa
        }
        int powerPosition = ePosition + 1;
        char ch = word.charAt(powerPosition);
        if (ch == '+' || ch == '-') {
            powerPosition++;
        }
        if (powerPosition >= word.length()) { // e.g 3e+
            return false;
        }
        for (int i = powerPosition; i < word.length(); i++) {
            if (!Character.isDigit(word.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
