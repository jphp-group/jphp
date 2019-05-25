package org.develnext.jphp.zend.ext.standard.date;

import php.runtime.Memory;
import php.runtime.memory.ArrayMemory;

/**
 *
 */
public final class DateTimeMessageContainer {
    private static final ArrayMemory messages = ArrayMemory.createHashed(4);

    static {
        messages.refOfIndex("warning_count").assign(0);
        messages.refOfIndex("warnings").assign(ArrayMemory.createListed(0));
        messages.refOfIndex("error_count").assign(0);
        messages.refOfIndex("errors").assign(ArrayMemory.createListed(0));
    }

    public static void addWarning(String message, int errorIndex) {
        ArrayMemory warnings = messages.refOfIndex("warnings").toValue(ArrayMemory.class);
        warnings.refOfIndex(errorIndex).assign(message);
        messages.refOfIndex("warning_count").assign(warnings.size());
    }

    public static void addError(String message, int errorIndex) {
        ArrayMemory errors = messages.refOfIndex("errors").toValue(ArrayMemory.class);
        errors.refOfIndex(errorIndex).assign(message);
        messages.refOfIndex("error_count").assign(errors.size());
    }

    public static Memory getMessages() {
        return messages.toImmutable();
    }

    public static void clear() {
        clearPart("errors", "error_count");
        clearPart("warnings", "warning_count");
    }

    private static void clearPart(String name, String counterName) {
        messages.refOfIndex(counterName).assign(0);
        messages.refOfIndex(name).toValue(ArrayMemory.class).clear();
    }
}
