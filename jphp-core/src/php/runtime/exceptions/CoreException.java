package php.runtime.exceptions;

import php.runtime.exceptions.support.ErrorType;
import php.runtime.env.Context;
import php.runtime.exceptions.support.ErrorException;

public class CoreException extends ErrorException {
    public CoreException(String message, Context context) {
        super(message, context);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_CORE_ERROR;
    }
}
