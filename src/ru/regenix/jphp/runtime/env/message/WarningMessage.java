package ru.regenix.jphp.runtime.env.message;


import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.CallStackItem;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;

public class WarningMessage extends SystemMessage {

    public WarningMessage(CallStackItem trace, Messages.Item message, Object... args) {
        super(trace, message, args);
    }

    public WarningMessage(Environment environment, Messages.Item message, Object... args) {
        super(environment, message, args);
    }

    @Override
    public ErrorException.Type getType() {
        return ErrorException.Type.E_WARNING;
    }
}
