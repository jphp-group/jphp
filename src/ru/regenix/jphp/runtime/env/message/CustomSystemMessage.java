package ru.regenix.jphp.runtime.env.message;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.CallStackItem;
import ru.regenix.jphp.runtime.env.Environment;

public class CustomSystemMessage extends SystemMessage {
    protected final ErrorException.Type type;

    public CustomSystemMessage(ErrorException.Type type, CallStackItem trace, Messages.Item message, Object... args) {
        super(trace, message, args);
        this.type = type;
    }

    public CustomSystemMessage(ErrorException.Type type, Environment environment, Messages.Item message, Object... args) {
        super(environment, message, args);
        this.type = type;
    }

    @Override
    public ErrorException.Type getType() {
        return type;
    }
}
