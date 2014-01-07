package ru.regenix.jphp.runtime.env;

import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.message.SystemMessage;

abstract public class ErrorReportHandler {
    abstract public boolean onError(SystemMessage error);
    abstract public boolean onFatal(ErrorException error);
}
