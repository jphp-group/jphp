package org.develnext.jphp.json.gson;

import com.google.gson.*;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.StdClass;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.Map;

public class MemoryDeserializer implements JsonDeserializer<Memory> {

    protected boolean assoc;
    protected int maxDepth = 512;
    protected WeakReference<Environment> env;

    public boolean isAssoc() {
        return assoc;
    }

    public void setEnv(Environment env) {
        this.env = new WeakReference<Environment>(env);
    }

    public void setAssoc(boolean assoc) {
        this.assoc = assoc;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    protected Memory convert(JsonElement json, int depth) {
        if (depth > maxDepth)
            throw new MaxDepthException();

        if (json.isJsonNull())
            return Memory.NULL;
        if (json.isJsonPrimitive()) {
            JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive();
            if (jsonPrimitive.isString())
                return StringMemory.valueOf(jsonPrimitive.getAsString());
            else if (jsonPrimitive.isBoolean())
                return jsonPrimitive.getAsBoolean() ? Memory.TRUE : Memory.FALSE;
            else if (jsonPrimitive.isNumber()) {
                Memory l = StringMemory.toLong(jsonPrimitive.getAsString());
                if (l != null)
                    return l;
                else
                    return new DoubleMemory(json.getAsDouble());
            }
            return Memory.NULL;
        } else if (json.isJsonArray()) {
            ArrayMemory array = new ArrayMemory();
            for(JsonElement el : json.getAsJsonArray())
                array.add(convert(el, depth + 1).toImmutable());
            return array.toConstant();
        } else if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();
            StdClass stdClass = assoc ? null : new StdClass(env.get());
            ArrayMemory array = assoc ? new ArrayMemory() : stdClass.getProperties();

            for(Map.Entry<String, JsonElement> el : jsonObject.entrySet()){
                String key = el.getKey();
                if (!key.startsWith("\0"))
                    array.put(key, convert(el.getValue(), depth + 1).toImmutable());
            }

            return assoc ? array : new ObjectMemory(stdClass);
        } else
            return Memory.NULL;
    }

    @Override
    public Memory deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return convert(json, 0);
    }

    public static class MaxDepthException extends RuntimeException {
        @Override
        public synchronized Throwable fillInStackTrace() {
            return this;
        }
    }
}
