package ru.regenix.jphp.exceptions;

public class CriticalException extends RuntimeException implements JPHPException {

    public CriticalException(Throwable cause) {
        super(cause);
    }
}
