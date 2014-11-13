package php.runtime.env.message;

import php.runtime.common.Messages;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;

public class NoticeMessage extends SystemMessage {

    public NoticeMessage(CallStackItem trace, Messages.Item message, Object... args) {
        super(trace, message, args);
    }

    public NoticeMessage(Environment environment, Messages.Item message, Object... args) {
        super(environment, message, args);
    }

    @Override
    public ErrorType getType() {
        return ErrorType.E_NOTICE;
    }
}
