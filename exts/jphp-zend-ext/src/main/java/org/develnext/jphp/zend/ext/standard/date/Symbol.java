package org.develnext.jphp.zend.ext.standard.date;

public enum Symbol {
    EOF("End of input", ""),
    DIGITS("HH", "[0-9]+"),
    FRACTION("frac", ".[0-9]+"),
    HOUR_12("hh", "\"0\"?[1-9] | \"1\"[0-2]"),
    TWO_DIGITS("HH", "[01][0-9] | \"2\"[0-4]"),
    MERIDIAN("meridian", "[AaPp] .? [Mm] .? [\\0\\t ]"),
    TWO_DIGIT_MINUTE("MM", "[0-5][0-9]"),
    TIMEZONE("tz", "\"(\"? [A-Za-z]{1,6} \")\"? | [A-Z][a-z]+([_/][A-Z][a-z]+)+"),
    TIMEZONE_CORRECTION("tzcorrection", "\"GMT\"? [+-] hh \":\"? MM?"),
    COLON("colon", ":"),
    DOT("dot", "."),
    MINUS("minus", "-"),
    PLUS("plus", "+"),
    SLASH("slash", "/"),
    TIME_MARKER("time", "[tT]"),
    CHARACTER("char", "[a-z][A-Z]+"),
    AT("at", "@"),
    SPACE("space", "[ \\t]");

    private final String description;
    private final String format;

    Symbol(String description, String format) {
        this.description = description;
        this.format = format;
    }

    String description() {
        return description;
    }

    String format() {
        return format;
    }
}
