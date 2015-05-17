package org.develnext.jphp.debug.impl;

import php.runtime.exceptions.CriticalException;

public class DebuggerException extends CriticalException {
    public DebuggerException(Throwable cause) {
        super(cause);
    }

    public DebuggerException(String message) {
        super(message);
    }
}
