package ru.regenix.jphp.lexer;

import ru.regenix.jphp.lexer.tokens.expr.StringExprToken;

public class GrammarUtils {

    public static boolean isSpace(char ch){
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }

    public static StringExprToken.Quote isQuote(char ch){
        if (ch == '\'')
            return StringExprToken.Quote.SINGLE;
        else if (ch == '"')
            return StringExprToken.Quote.DOUBLE;
        else if (ch == '`')
            return StringExprToken.Quote.SHELL;

        return null;
    }

    public static boolean isBackslash(char ch){
        return ch == '\\';
    }

    public static boolean isDelimiter(char ch){
        return "+-=/*<>!?%@&^\\.|([{}]);,\t\n\r ".indexOf(ch) > -1;
    }

    public static boolean isNewline(char ch){
        return ch == '\n';
    }
}
