package org.develnext.jphp.json.classes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonWriter;
import org.develnext.jphp.json.gson.MemoryDeserializer;
import org.develnext.jphp.json.gson.MemorySerializer;
import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.memory.*;
import php.runtime.memory.helper.*;
import php.runtime.memory.support.ArrayMapEntryMemory;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static php.runtime.annotation.Reflection.*;

@Name("php\\format\\JsonProcessor")
public class JsonProcessor extends WrapProcessor {
    public static final int SERIALIZE_PRETTY_PRINT = 1;
    public static final int DESERIALIZE_AS_ARRAYS = 1024;
    public static final int DESERIALIZE_LENIENT = 2048;

    protected GsonBuilder builder;
    protected Gson gson;
    protected MemorySerializer memorySerializer;
    protected MemoryDeserializer memoryDeserializer;

    protected final static List<Class<? extends Memory>> memClasses = new ArrayList<Class<? extends Memory>>() {{
        add(Memory.class);
        add(NullMemory.class);
        add(UndefinedMemory.class);
        add(ReferenceMemory.class);
        add(TrueMemory.class);
        add(FalseMemory.class);
        add(LongMemory.class);
        add(DoubleMemory.class);
        add(ObjectMemory.class);
        add(ArrayMemory.class);
        add(BinaryMemory.class);
        add(CharMemory.class);
        add(KeyValueMemory.class);
        add(StringBuilderMemory.class);
        add(StringMemory.class);
        add(ArrayValueMemory.class);
        add(BinaryCharArrayMemory.class);
        add(CharArrayMemory.class);
        add(ShortcutMemory.class);
        add(VariadicMemory.class);
    }};

    public JsonProcessor(Environment env, GsonBuilder builder) {
        super(env);
        this.builder = builder;
        this.gson = builder.create();
    }

    public JsonProcessor(Environment env, ClassEntity clazz) {
        super(env, clazz);
        builder = new GsonBuilder();

        memorySerializer = new MemorySerializer();
        memorySerializer.setEnv(env);

        memoryDeserializer = new MemoryDeserializer();
        memoryDeserializer.setEnv(env);

        for (Class<? extends Memory> el : memClasses) {
            builder.registerTypeAdapter(el, memorySerializer);
        }

        builder.registerTypeAdapter(Memory.class, memoryDeserializer);
        builder.disableHtmlEscaping();

        gson = builder.create();
    }

    @Signature(@Arg(value = "flags", optional = @Optional("0")))
    public Memory __construct(Environment env, Memory... args) {
        int flags = args[0].toInteger();

        if ((flags & SERIALIZE_PRETTY_PRINT) == SERIALIZE_PRETTY_PRINT) {
            builder.setPrettyPrinting();
        }

        if ((flags & DESERIALIZE_LENIENT) == DESERIALIZE_LENIENT) {
            builder.setLenient();
        }

        if ((flags & DESERIALIZE_AS_ARRAYS) == DESERIALIZE_AS_ARRAYS) {
            memoryDeserializer.setAssoc(true);
        }

        if (flags > 0) {
            gson = builder.create();
        }

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory parse(Environment env, Memory... args) {
        Memory r;

        try {
            if (args[0].instanceOf(Stream.class)) {
                r = gson.fromJson(new InputStreamReader(Stream.getInputStream(env, args[0]), env.getDefaultCharset()), Memory.class);
            } else {
                r = gson.fromJson(args[0].toString(), Memory.class);
            }
        } catch (JsonSyntaxException e) {
            env.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        }

        if (r == null)
            return Memory.NULL;

        return r;
    }

    @Override
    @Signature
    public Memory format(Environment env, Memory... args) {
        try {
            return StringMemory.valueOf(gson.toJson(args[0]));
        } catch (JsonIOException e) {
            env.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        }
    }

    @Override
    @Signature
    public Memory formatTo(Environment env, Memory... args) {
        OutputStream outputStream = Stream.getOutputStream(env, args[1]);

        try {
            OutputStreamWriter writer = new OutputStreamWriter(outputStream);
            gson.toJson(args[0], Memory.class, writer);
            try {
                writer.close();
            } catch (IOException e) {
                throw new JsonIOException(e);
            }
        } catch (JsonIOException e) {
            env.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        } finally {
            Stream.closeStream(env, outputStream);
        }

        return Memory.NULL;
    }

    @Signature({
            @Arg("type"),
            @Arg(value = "callback", type = HintType.CALLABLE, optional = @Optional("null"))
    })
    public Memory onSerialize(Environment env, Memory... args) {
        Memory.Type type = Memory.Type.of(args[0].toString());
        if (type == null)
            throw new IllegalArgumentException("Invalid type - " + args[0]);

        MemorySerializer.Handler handler = null;
        if (!args[1].isNull()) {
            final Invoker invoker = Invoker.valueOf(env, env.trace(), args[1]);
            handler = new MemorySerializer.Handler() {
                @Override
                public Memory call(Environment env, Memory value) {
                    return invoker.callNoThrow(value);
                }
            };
        }

        memorySerializer.setTypeHandler(type, handler);
        return null;
    }

    @Signature({
            @Arg("className"),
            @Arg(value = "callback", type = HintType.CALLABLE, optional = @Optional("null"))
    })
    public Memory onClassSerialize(Environment env, Memory... args) {
        ClassEntity entity = env.fetchClass(args[0].toString(), true);
        if (entity == null)
            throw new IllegalArgumentException("Class not found - " + args[0]);

        MemorySerializer.Handler handler = null;
        if (!args[1].isNull()) {
            final Invoker invoker = Invoker.valueOf(env, env.trace(), args[1]);
            handler = new MemorySerializer.Handler() {
                @Override
                public Memory call(Environment env, Memory value) {
                    return invoker.callNoThrow(value);
                }
            };
        }

        memorySerializer.setClassHandler(entity.getName(), handler);
        return Memory.NULL;
    }
}
