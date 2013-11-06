package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.exceptions.support.UserException;

abstract public class ExceptionHandler {
    abstract public boolean onException(UserException exception);
}
