package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.runtime.lang.BaseException;

abstract public class ExceptionHandler {
    abstract public boolean onException(BaseException exception);
}
