package ru.regenix.jphp.runtime.env.message;


import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.support.ErrorException;
import ru.regenix.jphp.runtime.env.TraceInfo;

public class WarningMessage extends SystemMessage {

    public WarningMessage(TraceInfo trace, Messages.Item message, Object... args) {
        super(trace, message, args);
    }

    @Override
    public ErrorException.Type getType() {
        return ErrorException.Type.E_WARNING;
    }
}
