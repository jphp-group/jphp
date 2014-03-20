package org.develnext.jphp.json.gson;

import com.google.gson.*;
import org.develnext.jphp.json.JsonSerializable;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class MemorySerializer implements JsonSerializer<Memory> {
    protected boolean forceObject;
    protected boolean numericCheck;
    protected Environment env;

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
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

    protected JsonElement convert(Memory src, Set<Integer> used) {
        switch (src.getRealType()) {
            case BOOL: return new JsonPrimitive(src.toBoolean());
            case DOUBLE: return new JsonPrimitive(src.toDouble());
            case INT: return new JsonPrimitive(src.toLong());
            case STRING: {
                if (numericCheck) {
                    Memory m = StringMemory.toLong(src.toString());
                    if (m != null)
                        return new JsonPrimitive(m.toLong());
                    else
                        return new JsonPrimitive(src.toString());
                } else
                    return new JsonPrimitive(src.toString());
            }
            case NULL: return JsonNull.INSTANCE;
            case ARRAY: {
                if (used.add(src.getPointer())){
                    JsonArray array = new JsonArray();
                    JsonObject object = new JsonObject();

                    boolean isList = !forceObject && src.toValue(ArrayMemory.class).isList();
                    ForeachIterator iterator = src.toValue(ArrayMemory.class).foreachIterator(false, false);
                    while (iterator.next()) {
                        if (isList)
                            array.add(convert(iterator.getValue(), used));
                        else
                            object.add(iterator.getKey().toString(), convert(iterator.getValue(), used));
                    }
                    used.remove(src.getPointer());
                    return isList ? array : object;
                } else
                    return JsonNull.INSTANCE;
            }
            case OBJECT: {
                if (used.add(src.getPointer())){
                    IObject object = src.toValue(ObjectMemory.class).value;
                    if (object instanceof JsonSerializable) {
                        Environment env = this.env == null ? Environment.current() : this.env;

                        env.pushCall(object, "jsonSerialize");
                        try {
                            Memory r = ((JsonSerializable) object).jsonSerialize(Environment.current());
                            return convert(r, used);
                        } finally {
                            env.popCall();
                        }
                    } else {
                        ForeachIterator iterator = object.getProperties().foreachIterator(false, false);
                        JsonObject r = new JsonObject();
                        while (iterator.next()) {
                            String key = iterator.getKey().toString();
                            if (!key.startsWith("\0"))
                                r.add(iterator.getKey().toString(), convert(iterator.getValue(), used));
                        }
                        return r;
                    }
                } else
                    return JsonNull.INSTANCE;
            }
            default:
                return JsonNull.INSTANCE;
        }
    }

    public JsonElement serialize(Memory src, Type typeOfSrc, JsonSerializationContext context) {
        return convert(src, new HashSet<Integer>());
    }
}
