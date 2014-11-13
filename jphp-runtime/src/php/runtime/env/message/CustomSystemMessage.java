package php.runtime.env.message;

import php.runtime.common.Messages;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;

public class CustomSystemMessage extends SystemMessage {
    protected final ErrorType type;

    public CustomSystemMessage(ErrorType type, CallStackItem trace, Messages.Item message, Object... args) {
        super(trace, message, args);
        this.type = type;
    }

    public CustomSystemMessage(ErrorType type, Environment environment, Messages.Item message, Object... args) {
        super(environment, message, args);
        this.type = type;
    }

    @Override
    public ErrorType getType() {
        return type;
    }
}
