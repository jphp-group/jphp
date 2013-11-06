package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.exceptions.support.ErrorException;

abstract public class ErrorHandler {
    abstract public boolean onError(ErrorException error);
}
