package org.develnext.jphp.zend.ext.json;

import com.google.gson.GsonBuilder;
import org.develnext.jphp.json.gson.MemoryDeserializer;
import org.develnext.jphp.json.gson.MemorySerializer;
import php.runtime.Memory;
import php.runtime.env.CompileScope;
import php.runtime.ext.support.Extension;
import php.runtime.memory.*;
import php.runtime.memory.helper.UndefinedMemory;

public class JsonExtension extends Extension {

    public final static GsonBuilder DEFAULT_GSON_BUILDER;
    public final static GsonBuilder DEFAULT_GSON_BUILDER_FOR_DECODE;

    @Override
    public String getName() {
        return "json";
    }

    @Override
    public String[] getRequiredExtensions() {
        return new String[]{
                org.develnext.jphp.json.JsonExtension.class.getName()
        };
    }

    @Override
    public String getVersion() {
        return "~";
    }

    @Override
    public Status getStatus() {
        return Status.ZEND_LEGACY;
    }

    @Override
    public void onRegister(CompileScope scope) {
        registerConstants(new JsonConstants());
        registerFunctions(new JsonFunctions());
    }

    public static GsonBuilder createGsonBuilder(MemorySerializer memorySerializer) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Memory.class, memorySerializer);
        builder.registerTypeAdapter(NullMemory.class, memorySerializer);
        builder.registerTypeAdapter(UndefinedMemory.class, memorySerializer);
        builder.registerTypeAdapter(ReferenceMemory.class, memorySerializer);
        builder.registerTypeAdapter(TrueMemory.class, memorySerializer);
        builder.registerTypeAdapter(FalseMemory.class, memorySerializer);
        builder.registerTypeAdapter(LongMemory.class, memorySerializer);
        builder.registerTypeAdapter(DoubleMemory.class, memorySerializer);
        builder.registerTypeAdapter(ObjectMemory.class, memorySerializer);
        builder.registerTypeAdapter(ArrayMemory.class, memorySerializer);
        builder.registerTypeAdapter(BinaryMemory.class, memorySerializer);
        builder.registerTypeAdapter(CharMemory.class, memorySerializer);
        builder.registerTypeAdapter(KeyValueMemory.class, memorySerializer);
        builder.registerTypeAdapter(StringBuilderMemory.class, memorySerializer);
        builder.registerTypeAdapter(StringMemory.class, memorySerializer);
        return builder;
    }

    public static GsonBuilder createGsonBuilderForDecode(MemoryDeserializer memoryDeserializer){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Memory.class, memoryDeserializer);
        return builder;
    }

    static {
        DEFAULT_GSON_BUILDER = createGsonBuilder(new MemorySerializer());
        DEFAULT_GSON_BUILDER.disableHtmlEscaping();

        DEFAULT_GSON_BUILDER_FOR_DECODE  = createGsonBuilderForDecode(new MemoryDeserializer());
    }
}
