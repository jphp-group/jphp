package ru.regenix.jphp.env;

import ru.regenix.jphp.exceptions.support.ErrorException;

abstract public class ErrorHandler {
    abstract public boolean onError(ErrorException error);
}
