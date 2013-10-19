package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.env.TraceInfo;

public class ParseException extends PHPException {
    public ParseException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }
}
