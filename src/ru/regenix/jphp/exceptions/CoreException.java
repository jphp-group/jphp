package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.exceptions.support.ErrorType;
import php.runtime.env.Context;
import ru.regenix.jphp.exceptions.support.ErrorException;

public class CoreException extends ErrorException {
    public CoreException(String message, Context context) {
        super(message, context);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_CORE_ERROR;
    }
}
