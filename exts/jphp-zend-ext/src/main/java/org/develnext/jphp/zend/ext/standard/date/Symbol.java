package org.develnext.jphp.zend.ext.standard.date;

public enum Symbol {
    EOF("End of input", ""),
    DIGITS("HH", "[0-9]+"),
    FRACTION("frac", ".[0-9]+"),
    HOUR_12("hh", "\"0\"?[1-9] | \"1\"[0-2]"),
    MERIDIAN("meridian", "[AaPp] .? [Mm] .? [\\0\\t ]"),
    COLON("colon", ":"),
    DOT("dot", "."),
    MINUS("minus", "-"),
    PLUS("plus", "+"),
    STRING("string", "[a-z][A-Z]+"),
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
