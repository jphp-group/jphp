package php.runtime.exceptions.support;

import php.runtime.env.Context;
import php.runtime.env.TraceInfo;

abstract public class UserException extends PhpException {
    public UserException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public UserException(String message, Context context) {
        super(message, context);
    }
}
