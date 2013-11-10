package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.runtime.env.message.SystemMessage;

abstract public class ErrorHandler {
    abstract public boolean onError(SystemMessage error);
}
