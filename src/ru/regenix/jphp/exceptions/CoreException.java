package ru.regenix.jphp.exceptions;

import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.exceptions.support.ErrorException;

public class CoreException extends ErrorException {
    public CoreException(String message, Context context) {
        super(message, context);
    }

    @Override
    public Type getType() {
        return Type.E_CORE_ERROR;
    }
}
