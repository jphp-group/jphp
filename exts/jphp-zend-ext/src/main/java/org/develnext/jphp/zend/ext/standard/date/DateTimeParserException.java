package org.develnext.jphp.zend.ext.standard.date;

public class DateTimeParserException extends Exception {

    public DateTimeParserException(String parsedString, int errorIndex) {
        super(String.format("Failed to parse time string (%s) at position %d (%c)",
                parsedString, errorIndex, parsedString.charAt(errorIndex)));
    }
}
