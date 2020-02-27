package php.runtime.exceptions.support;

import static php.runtime.common.Messages.MSG_E_ALL;
import static php.runtime.common.Messages.MSG_E_COMPILE_ERROR;
import static php.runtime.common.Messages.MSG_E_COMPILE_WARNING;
import static php.runtime.common.Messages.MSG_E_CORE_ERROR;
import static php.runtime.common.Messages.MSG_E_CORE_WARNING;
import static php.runtime.common.Messages.MSG_E_DEPRECATED;
import static php.runtime.common.Messages.MSG_E_ERROR;
import static php.runtime.common.Messages.MSG_E_NOTICE;
import static php.runtime.common.Messages.MSG_E_PARSE;
import static php.runtime.common.Messages.MSG_E_RECOVERABLE_ERROR;
import static php.runtime.common.Messages.MSG_E_STRICT;
import static php.runtime.common.Messages.MSG_E_UNKNOWN;
import static php.runtime.common.Messages.MSG_E_USER_DEPRECATED;
import static php.runtime.common.Messages.MSG_E_USER_ERROR;
import static php.runtime.common.Messages.MSG_E_USER_NOTICE;
import static php.runtime.common.Messages.MSG_E_USER_WARNING;
import static php.runtime.common.Messages.MSG_E_WARNING;

import java.util.HashMap;
import java.util.Map;
import php.runtime.common.Messages;

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
            case E_DEPRECATED: return MSG_E_DEPRECATED.toString();
            case E_STRICT: return MSG_E_STRICT.toString();
            case E_NOTICE: return MSG_E_NOTICE.toString();
            case E_ALL: return MSG_E_ALL.toString();
            case E_COMPILE_ERROR: return MSG_E_COMPILE_ERROR.toString();
            case E_COMPILE_WARNING: return MSG_E_COMPILE_WARNING.toString();
            case E_CORE_ERROR: return MSG_E_CORE_ERROR.toString();
            case E_CORE_WARNING: return MSG_E_CORE_WARNING.toString();
            case E_ERROR: return MSG_E_ERROR.toString();
            case E_PARSE: return MSG_E_PARSE.toString();
            case E_RECOVERABLE_ERROR: return MSG_E_RECOVERABLE_ERROR.toString();
            case E_USER_DEPRECATED: return MSG_E_USER_DEPRECATED.toString();
            case E_USER_ERROR: return MSG_E_USER_ERROR.toString();
            case E_USER_NOTICE: return MSG_E_USER_NOTICE.toString();
            case E_USER_WARNING: return MSG_E_USER_WARNING.toString();
            case E_WARNING: return MSG_E_WARNING.toString();
        }

        return MSG_E_UNKNOWN.toString();
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
