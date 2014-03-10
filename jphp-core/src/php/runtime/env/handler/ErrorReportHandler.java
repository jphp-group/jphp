package php.runtime.env.handler;

import php.runtime.exceptions.support.ErrorException;
import php.runtime.env.message.SystemMessage;

abstract public class ErrorReportHandler {
    abstract public boolean onError(SystemMessage error);
    abstract public boolean onFatal(ErrorException error);
}
