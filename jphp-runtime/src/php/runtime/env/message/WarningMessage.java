package php.runtime.env.message;


import php.runtime.common.Messages;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;

public class WarningMessage extends SystemMessage {

    public WarningMessage(CallStackItem trace, Messages.Item message, Object... args) {
        super(trace, message, args);
    }

    public WarningMessage(Environment environment, Messages.Item message, Object... args) {
        super(environment, message, args);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_WARNING;
    }
}
