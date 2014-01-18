package php.runtime.exceptions.support;

import java.util.HashMap;
import java.util.Map;

public enum ErrorType {
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
    private static Map<Integer, ErrorType> map = new HashMap<Integer, ErrorType>();

    static {
        for (ErrorType legEnum : ErrorType.values()) {
            map.put(legEnum.value, legEnum);
        }
    }

    ErrorType(int value) {
        this.value = value;
    }

    public String getTypeName(){
        switch (this){
            case E_DEPRECATED: return "Deprecated";
            case E_STRICT: return "Strict Standards";
            case E_NOTICE: return "Notice";
            case E_ALL: return "Message";
            case E_COMPILE_ERROR: return "Compile error";
            case E_COMPILE_WARNING: return "Compile warning";
            case E_CORE_ERROR: return "Core error";
            case E_CORE_WARNING: return "Core warning";
            case E_ERROR: return "Fatal error";
            case E_PARSE: return "Parse error";
            case E_RECOVERABLE_ERROR: return "Recoverable error";
            case E_USER_DEPRECATED: return "User deprecated";
            case E_USER_ERROR: return "User error";
            case E_USER_NOTICE: return "User notice";
            case E_USER_WARNING: return "User warning";
            case E_WARNING: return "Warning";
        }
        return "Unknown";
    }

    public boolean isHandled(){
        if (!isFatal())
            return true;

        switch (this){
            case E_RECOVERABLE_ERROR:
                return true;
        }

        return false;
    }

    public boolean isFatal(){
        switch (this){
            case E_DEPRECATED:
            case E_USER_DEPRECATED:
            case E_NOTICE:
            case E_WARNING:
            case E_CORE_WARNING:
            case E_USER_NOTICE:
            case E_USER_WARNING:
            case E_STRICT:
                return false;
        }
        return true;
    }

    public static ErrorType valueOf(int typeNo) {
        return map.get(typeNo);
    }

    public static boolean check(int value, ErrorType type){
        return (value & type.value) == type.value;
    }

    public static int getFlags(ErrorType... types){
        int value = 0;
        for (ErrorType type : types) {
            value |= type.value;
        }
        return value;
    }
}
