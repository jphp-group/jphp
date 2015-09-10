package org.develnext.jphp.zend.ext.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.develnext.jphp.json.gson.MemoryDeserializer;
import org.develnext.jphp.json.gson.MemorySerializer;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.support.compile.FunctionsContainer;
import php.runtime.memory.StringMemory;

import static php.runtime.annotation.Runtime.Immutable;

public class JsonFunctions extends FunctionsContainer {

    public static Memory json_decode(Environment env, String json, boolean assoc, int depth) {
        MemoryDeserializer memoryDeserializer = new MemoryDeserializer();
        memoryDeserializer.setEnv(env);
        GsonBuilder gsonBuilder = JsonExtension.createGsonBuilderForDecode(memoryDeserializer);

        memoryDeserializer.setAssoc(assoc);
        memoryDeserializer.setMaxDepth(depth);

        Gson gson = gsonBuilder.create();
        try {
            env.setUserValue(JsonFunctions.class.getName() + "#error", null);
            Memory r = gson.fromJson(json, Memory.class);
            if (r == null)
                return Memory.NULL;
            else
                return assoc ? r.toImmutable() : r;
        } catch (MemoryDeserializer.MaxDepthException e) {
            env.setUserValue(JsonFunctions.class.getName() + "#error", JsonConstants.JSON_ERROR_DEPTH);
        } catch (JsonSyntaxException e) {
            env.setUserValue(JsonFunctions.class.getName() + "#error", JsonConstants.JSON_ERROR_SYNTAX);
        } catch (JsonParseException e) {
            env.setUserValue(JsonFunctions.class.getName() + "#error", JsonConstants.JSON_ERROR_STATE_MISMATCH);
        }

        return Memory.NULL;
    }

    public static Memory json_decode(Environment env, String json, boolean assoc) {
        return json_decode(env, json, assoc, 512);
    }

    public static int json_last_error(Environment env) {
        Integer error = env.getUserValue(JsonFunctions.class.getName() + "#error", Integer.class);
        if (error == null)
            return JsonConstants.JSON_ERROR_NONE;

        return error;
    }

    public static Memory json_last_error_msg(Environment env) {
        switch (json_last_error(env)) {
            case JsonConstants.JSON_ERROR_NONE: return Memory.NULL;
            case JsonConstants.JSON_ERROR_DEPTH: return new StringMemory("Maximum stack depth exceeded");
            case JsonConstants.JSON_ERROR_STATE_MISMATCH: return new StringMemory("Underflow or the modes mismatch");
            case JsonConstants.JSON_ERROR_SYNTAX: return new StringMemory("Syntax error, malformed JSON");
            case JsonConstants.JSON_ERROR_UTF8: return new StringMemory("Malformed UTF-8 characters, possibly incorrectly encoded");
            case JsonConstants.JSON_ERROR_CTRL_CHAR: return new StringMemory("Unexpected control character found");
            default:
                return Memory.NULL;
        }
    }

    public static Memory json_decode(Environment env, String json) {
        return json_decode(env, json, false);
    }

    @Immutable
    public static String json_encode(Memory memory, int options) {
        GsonBuilder builder;
        if (options != 0){
            MemorySerializer serializer = new MemorySerializer();
            builder = JsonExtension.createGsonBuilder(serializer);

            if ((options & JsonConstants.JSON_PRETTY_PRINT) == JsonConstants.JSON_PRETTY_PRINT){
                builder.setPrettyPrinting();
            }

            if ((options & JsonConstants.JSON_HEX_TAG) != JsonConstants.JSON_HEX_TAG){
                builder.disableHtmlEscaping();
            }

            if ((options & JsonConstants.JSON_FORCE_OBJECT) == JsonConstants.JSON_FORCE_OBJECT){
                serializer.setForceObject(true);
            }

            if ((options & JsonConstants.JSON_NUMERIC_CHECK) == JsonConstants.JSON_NUMERIC_CHECK){
                serializer.setNumericCheck(true);
            }
        } else {
            builder = JsonExtension.DEFAULT_GSON_BUILDER;
        }

        Gson gson = builder.create();
        return gson.toJson(memory);
    }

    @Immutable
    public static String json_encode(Memory memory) {
        return json_encode(memory, 0);
    }
}
