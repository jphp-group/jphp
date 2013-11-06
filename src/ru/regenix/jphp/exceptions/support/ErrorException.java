package ru.regenix.jphp.exceptions.support;

import ru.regenix.jphp.runtime.env.Context;
import ru.regenix.jphp.runtime.env.TraceInfo;

import java.util.HashMap;
import java.util.Map;

abstract public class ErrorException extends PhpException {

    public enum Type {
        E_ERROR(1),
        E_WARNING(2),
        E_PARSE(4),
        E_NOTICE(8),
        E_CORE_ERROR(16),
        E_CORE_WARNING(32),
        E_COMPILE_ERROR(64),
        E_COMPILE_WARNING(128),
        E_USER_ERROR(256),
        E_USER_WARNING(512),
        E_USER_NOTICE(1024),
        E_STRICT(2048),
        E_RECOVERABLE_ERROR(4096),
        E_DEPRECATED(8192),
        E_USER_DEPRECATED(16384),
        E_ALL(32767);

        public final int value;
        private static Map<Integer, Type> map = new HashMap<Integer, Type>();

        static {
            for (Type legEnum : Type.values()) {
                map.put(legEnum.value, legEnum);
            }
        }

        private Type(int value) {
            this.value = value;
        }

        public static Type valueOf(int typeNo) {
            return map.get(typeNo);
        }

        public static boolean check(int value, Type type){
            return (value & type.value) == type.value;
        }

        public static int getFlags(Type... types){
            int value = 0;
            for (Type type : types) {
                value |= type.value;
            }
            return value;
        }
    }

    public ErrorException(String message, TraceInfo traceInfo) {
        super(message, traceInfo);
    }

    public ErrorException(String message, Context context) {
        super(message, context);
    }

    abstract public Type getType();
}
