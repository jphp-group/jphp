package org.develnext.jphp.zend.ext.standard.date;

import java.util.HashMap;
import java.util.Map;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;

/**
 *
 */
public final class DateTimeMessageContainer {
    private static final ArrayMemory messages;

    static {
        Map<String, Memory> tmp = new HashMap<>();
        tmp.put("warning_count", Memory.CONST_INT_0);
        tmp.put("warnings", ArrayMemory.createListed(0));
        tmp.put("error_count", Memory.CONST_INT_0);
        tmp.put("errors", ArrayMemory.createListed(0));

        messages = ArrayMemory.ofMap(tmp);
    }

    public static void addWarning(Environment env, TraceInfo trace, String message, int errorIndex) {
        ArrayMemory warnings = messages.refOfIndex(env, trace, "warnings").toValue(ArrayMemory.class);
        warnings.refOfIndex(env, trace, errorIndex).assign(message);
        messages.refOfIndex(env, trace, "warning_count").assign(warnings.size());
    }

    public static void addError(Environment env, TraceInfo trace, String message, int errorIndex) {
        ArrayMemory errors = messages.refOfIndex(env, trace, "errors").toValue(ArrayMemory.class);
        errors.refOfIndex(env, trace, errorIndex).assign(message);
        messages.refOfIndex(env, trace, "error_count").assign(errors.size());
    }

    public static Memory getMessages() {
        return messages.toImmutable();
    }

    public static void clear(Environment env, TraceInfo trace) {
        clearPart(env, trace, "errors", "error_count");
        clearPart(env, trace, "warnings", "warning_count");
    }

    private static void clearPart(Environment env, TraceInfo trace, String name, String counterName) {
        messages.refOfIndex(env, trace, counterName).assign(0);
        messages.refOfIndex(env, trace, name).toValue(ArrayMemory.class).clear();
    }
}
