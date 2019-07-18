package org.develnext.jphp.json.gson;

import com.google.gson.*;
import org.develnext.jphp.json.JsonSerializable;
import php.runtime.Memory;
import php.runtime.common.collections.map.HashedMap;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MemorySerializer implements JsonSerializer<Memory> {
    private WeakReference<Environment> env;
    private boolean forceObject;
    private boolean numericCheck;
    private boolean unescapedUnicode;
    private Map<Memory.Type, Handler> typeHandlers;
    private Map<String, Handler> classHandlers;

    {
        typeHandlers = new HashedMap<>(1);
        classHandlers = new HashedMap<>(1);
    }

    private static String escapeUnicode(String input) {
        int unicodeIdx = indexOfUnicodeChar(input);

        // If there is no unicode characters just return as is
        if (unicodeIdx == -1) {
            return input;
        }

        StringBuilder sb = new StringBuilder(input.length())
            .append(input, 0, unicodeIdx);

        for (int i = unicodeIdx; i < input.length(); i++) {
            int c = input.charAt(i);

            if (c > 0x7F) {
                sb.append('\\').append('u');

                String hex = Integer.toHexString(c);

                // padding with zeros
                for (int j = 0; j < 4 - hex.length(); j++) sb.append('0');

                sb.append(hex);
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * Finds first occurrence of unicode character and returns its index or {@code -1} otherwise.
     */
    private static int indexOfUnicodeChar(String input) {
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > 0x7F) {
                return i;
            }
        }

        return -1;
    }

    public Environment getEnv() {
        return env == null ? null : env.get();
    }

    public void setEnv(Environment env) {
        this.env = env == null ? null : new WeakReference<Environment>(env);
    }

    public boolean isForceObject() {
        return forceObject;
    }

    public void setForceObject(boolean forceObject) {
        this.forceObject = forceObject;
    }

    public boolean isNumericCheck() {
        return numericCheck;
    }

    public void setNumericCheck(boolean numericCheck) {
        this.numericCheck = numericCheck;
    }

    protected JsonElement convert(Memory src, Set<Integer> used, boolean useHandlers) {
        if (useHandlers) {
            Handler handler = typeHandlers.get(src.getRealType());
            if (handler != null) {
                return convert(handler.call(getEnv(), src), used, false);
            }
        }

        switch (src.getRealType()) {
            case BOOL:
                return new JsonPrimitive(src.toBoolean());
            case DOUBLE:
                return new JsonPrimitive(src.toDouble());
            case INT:
                return new JsonPrimitive(src.toLong());
            case STRING: {
                if (numericCheck) {
                    Memory m = StringMemory.toLong(src.toString());
                    if (m != null) {
                        return new JsonPrimitive(m.toLong());
                    } else {
                        return new JsonPrimitive(src.toString());
                    }
                } else {
                    return new JsonPrimitive(unescapedUnicode ? src.toString() : escapeUnicode(src.toString()));
                }
            }
            case NULL:
                return JsonNull.INSTANCE;
            case ARRAY: {
                if (used.add(src.getPointer())) {
                    JsonArray array = new JsonArray();
                    JsonObject object = new JsonObject();

                    boolean isList = !forceObject && src.toValue(ArrayMemory.class).isList();
                    ForeachIterator iterator = src.toValue(ArrayMemory.class).foreachIterator(false, false);
                    while (iterator.next()) {
                        if (isList) {
                            array.add(convert(iterator.getValue(), used, useHandlers));
                        } else {
                            object.add(iterator.getKey().toString(), convert(iterator.getValue(), used, useHandlers));
                        }
                    }
                    used.remove(src.getPointer());
                    return isList ? array : object;
                } else {
                    return JsonNull.INSTANCE;
                }
            }
            case OBJECT: {
                if (used.add(src.getPointer())) {
                    IObject object = src.toValue(ObjectMemory.class).value;

                    if (useHandlers) {
                        Handler handler;

                        ClassEntity pr = object.getReflection();
                        do {
                            handler = classHandlers.get(pr.getLowerName());
                            pr = pr.getParent();
                            if (pr == null) {
                                break;
                            }

                        } while (handler == null);

                        if (handler != null) {
                            return convert(handler.call(getEnv(), src), used, false);
                        }
                    }

                    if (object instanceof JsonSerializable) {
                        Environment env = this.getEnv() == null ? Environment.current() : this.getEnv();

                        env.pushCall(object, "jsonSerialize");
                        try {
                            Memory r = ((JsonSerializable) object).jsonSerialize(Environment.current());
                            return convert(r, used, useHandlers);
                        } finally {
                            env.popCall();
                        }
                    } else {
                        ForeachIterator iterator = object.getProperties().foreachIterator(false, false);
                        JsonObject r = new JsonObject();
                        while (iterator.next()) {
                            String key = iterator.getKey().toString();
                            if (!key.startsWith("\0")) {
                                r.add(iterator.getKey().toString(), convert(iterator.getValue(), used, useHandlers));
                            }
                        }
                        return r;
                    }
                } else {
                    return JsonNull.INSTANCE;
                }
            }
            default:
                return JsonNull.INSTANCE;
        }
    }

    public JsonElement serialize(Memory src, Type typeOfSrc, JsonSerializationContext context) {
        return convert(src, new HashSet<Integer>(), true);
    }

    public void setTypeHandler(Memory.Type type, Handler handler) {
        if (handler == null) {
            typeHandlers.remove(type);
        } else {
            typeHandlers.put(type, handler);
        }
    }

    public void setClassHandler(String className, Handler handler) {
        className = className.toLowerCase();

        if (handler == null) {
            classHandlers.remove(className);
        } else {
            classHandlers.put(className, handler);
        }
    }

    public void unescapedUnicode(boolean unescapedUnicode) {
        this.unescapedUnicode = unescapedUnicode;
    }

    public interface Handler {
        Memory call(Environment env, Memory value);
    }
}
