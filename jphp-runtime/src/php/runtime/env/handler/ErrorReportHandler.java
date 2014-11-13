package php.runtime.env.handler;

import php.runtime.env.message.SystemMessage;
import php.runtime.exceptions.support.ErrorException;

abstract public class ErrorReportHandler {
    abstract public boolean onError(SystemMessage error);
    abstract public boolean onFatal(ErrorException error);
}
