package php.runtime.memory.output.serialization;

import php.runtime.Memory;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.IObject;
import php.runtime.lang.spl.Serializable;
import php.runtime.memory.*;
import php.runtime.reflection.ClassEntity;

public class Deserializer {
    protected final Environment env;
    protected final TraceInfo trace;
    protected int pos;

    public Deserializer(Environment env, TraceInfo trace) {
        this.env = env;
        this.trace = trace;
    }

    protected Memory error(int offset, int length){
        this.pos = -1;
        env.error(trace, ErrorType.E_NOTICE, "unserialize(): Error at offset %s of %s bytes", offset, length);
        return Memory.NULL;
    }

    private int readSize(String input, int pos){
        boolean done = false;
        int j, length = input.length();
        for(j = pos; j < length; j++){
            char ch = input.charAt(j);
            if (ch == ':') {
                done = true;
                break;
            }
        }

        this.pos = j;
        if (!done)
            return -1;

        String what = input.substring(pos, j);
        try {
            return Integer.parseInt(what);
        } catch (NumberFormatException e){
            return -1;
        }
    }

    public Memory read(String input){
        return read(input, 0);
    }

    public Memory readString(String input, int offset, char endChar){
        int i = offset, length = input.length();
        int size = readSize(input, i);
        if (size == -1)
            return error(i, length);

        i = this.pos;

        i++;
        if (i < length && input.charAt(i) == '"'){
            i++;
            if (i + size <= length){
                String str = input.substring(i, i + size);
                i += size;
                if (i < length && input.charAt(i) == '"'){
                    i++;
                    if (i < length && input.charAt(i) == endChar){
                        this.pos = i + 1;
                        return new StringMemory(str);
                    } else
                        return error(i, length);
                } else {
                    return error(i + 1, length);
                }
            } else {
                return error(i, length);
            }
        } else {
            return error(i, length);
        }
    }

    public Memory readArray(String input, int offset){
        int i = offset, length = input.length();
        int size = readSize(input, i);
        if (size == -1){
            error(i, length);
            return Memory.NULL;
        }

        i = this.pos;
        if (i >= length || input.charAt(i) != ':'){
            error(i, length);
            return Memory.NULL;
        }

        i++;
        if (i >= length || input.charAt(i) != '{'){
            error(i, length);
            return Memory.NULL;
        }

        i++;
        ArrayMemory result = new ArrayMemory();
        for(int k = 0; k < size; k++){
            Memory key = read(input, i);
            if (this.pos == -1)
                return Memory.NULL;
            i = this.pos;

            Memory value = read(input, i);
            if (this.pos == -1)
                return Memory.NULL;
            i = this.pos;

            result.put(ArrayMemory.toKey(key), value);
        }

        if (i >= length || input.charAt(i) != '}'){
            error(i, length);
            return Memory.NULL;
        }
        this.pos = i + 1;
        return result;
    }

    public Memory read(String input, int offset){
        int length = input.length();
        if (offset >= length){
            error(offset, length);
            return Memory.NULL;
        }

        for(int i = offset; i < length; i++){
            char ch = input.charAt(i);

            Memory.Type type = null;
            switch (ch){
                case 'N':
                    i++;
                    if (i < length && input.charAt(i) == ';'){
                        this.pos = i + 1;
                        return Memory.NULL;
                    } else
                        return error(i, length);
                case 'i': type = Memory.Type.INT;
                case 'd': if (type == null) type = Memory.Type.DOUBLE;
                case 'b': if (type == null) type = Memory.Type.BOOL;
                case 's': if (type == null) type = Memory.Type.STRING;
                case 'a': if (type == null) type = Memory.Type.ARRAY;
                case 'C':
                case 'O': if (type == null) type = Memory.Type.OBJECT;

                    boolean isSerializable = ch == 'C';

                    i++;
                    ch = input.charAt(i);
                    if (ch != ':'){
                        error(i, length);
                        return Memory.NULL;
                    }

                    i++;
                    switch (type){
                        case INT:
                        case DOUBLE:
                        case BOOL:
                            boolean done = false;
                            int j;
                            for(j = i; j < length; j++){
                                ch = input.charAt(j);
                                if (ch == ';') {
                                    done = true;
                                    break;
                                }
                            }
                            if (!done){
                                error(i, length);
                                return Memory.NULL;
                            }
                            String what = input.substring(i, j);
                            this.pos = i = j + 1;

                            switch (type){
                                case BOOL:
                                    if ("1".equals(what))
                                        return Memory.TRUE;
                                    else if ("0".equals(what))
                                        return Memory.FALSE;
                                    else {
                                        error(i + 1, length);
                                        return Memory.NULL;
                                    }
                                case INT:
                                    try {
                                        return LongMemory.valueOf(Long.parseLong(what));
                                    } catch (NumberFormatException e){
                                        error(i + 1, length);
                                        return Memory.NULL;
                                    }
                                case DOUBLE:
                                    try {
                                        return DoubleMemory.valueOf(Double.valueOf(what));
                                    } catch (NumberFormatException e){
                                        error(i + 1, length);
                                        return Memory.NULL;
                                    }
                            }
                            break;
                        case STRING:
                            return readString(input, i, ';');
                        case ARRAY:
                            return readArray(input, i);
                        case OBJECT:
                            Memory memory = readString(input, i, ':');

                            if (this.pos == -1){
                                return Memory.NULL;
                            }
                            if (!memory.isString()){
                                error(i, length);
                                return Memory.NULL;
                            }

                            i = this.pos;
                            ClassEntity classEntity = env.fetchClass(memory.toString(), true);
                            if (classEntity == null){
                                env.error(trace, ErrorType.E_ERROR, Messages.ERR_CLASS_NOT_FOUND, memory.toString());
                                return Memory.NULL;
                            }

                            try {
                                IObject iObject = classEntity.newObjectWithoutConstruct(env);
                                if (iObject == null) {
                                    env.exception(
                                            trace,
                                            new Messages.Item("Unserialization of '%s' is not allowed").fetch(classEntity.getName())
                                    );
                                }

                                if (isSerializable) {
                                    if (!(iObject instanceof Serializable)){
                                        env.warning(trace, "Class %s has no unserializer", classEntity.getName());
                                        return Memory.NULL;
                                    } else {
                                        int size = readSize(input, i);
                                        if (size == -1)
                                            return error(i, length);

                                        i = this.pos + 1;
                                        what = input.substring(i + 1, i + size + 1);
                                        if (i >= length || input.charAt(i) != '{') return error(i, length);

                                        i += size;
                                        i++;
                                        if (i >= length || input.charAt(i) != '}') return error(i, length);

                                        env.pushCall(trace, iObject, "unserialize", new StringMemory(what));
                                        try {
                                            ((Serializable) iObject).unserialize(env, new StringMemory(what));
                                        } finally {
                                            env.popCall();
                                        }
                                    }
                                } else {
                                    ArrayMemory props = iObject.getProperties();
                                    Memory serProps = readArray(input, i);
                                    if (serProps.isArray()) {
                                        props.putAll(serProps.toValue(ArrayMemory.class));
                                        if (classEntity.methodMagicWakeup != null){
                                            env.pushCall(trace, iObject, classEntity.methodMagicWakeup.getName());
                                            try {
                                                classEntity.methodMagicWakeup.invokeDynamic(iObject, env, trace);
                                            } finally {
                                                env.popCall();
                                            }
                                        }
                                    } else
                                        return Memory.NULL;
                                }

                                return new ObjectMemory(iObject);
                            } catch (RuntimeException e){
                                throw e;
                            } catch (Throwable throwable) {
                                throw new RuntimeException(throwable);
                            }
                    }

                    break;
                default:
                    error(i, length);
                    return Memory.NULL;
            }
        }
        return Memory.NULL;
    }
}
