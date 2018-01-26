package org.develnext.jphp.yaml.classes;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Optional;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.WrapJavaExceptions;
import php.runtime.ext.core.classes.format.WrapProcessor;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.io.InputStream;
import java.io.OutputStreamWriter;

@Name("php\\format\\YamlProcessor")
public class YamlProcessor extends WrapProcessor {
    public static final int SERIALIZE_PRETTY_FLOW = 1;
    public static final int SERIALIZE_CANONICAL = 2;
    public static final int SERIALIZE_EXPLICIT_START = 4;
    public static final int SERIALIZE_EXPLICIT_END = 8;
    public static final int SERIALIZE_NOT_SPLIT_LINES = 16;

    private Yaml yaml;

    public YamlProcessor(Environment env, Yaml yaml) {
        super(env);
        this.yaml = yaml;
    }

    public YamlProcessor(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }


    @Signature(@Arg(value = "flags", optional = @Optional("0")))
    public Memory __construct(Environment env, Memory... args) {
        DumperOptions options = new DumperOptions();

        int flags = args[0].toInteger();

        options.setPrettyFlow((flags & SERIALIZE_PRETTY_FLOW) == SERIALIZE_PRETTY_FLOW);
        options.setCanonical((flags & SERIALIZE_CANONICAL) == SERIALIZE_CANONICAL);
        options.setExplicitStart((flags & SERIALIZE_EXPLICIT_START) == SERIALIZE_EXPLICIT_START);
        options.setExplicitEnd((flags & SERIALIZE_EXPLICIT_END) == SERIALIZE_EXPLICIT_END);
        options.setSplitLines(!((flags & SERIALIZE_NOT_SPLIT_LINES) == SERIALIZE_NOT_SPLIT_LINES));

        yaml = new Yaml(options);
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory parse(Environment env, Memory... args) {
        try {
            if (args[0].instanceOf(Stream.class)) {
                return Memory.wrap(env, yaml.load(Stream.getInputStream(env, args[0])));
            } else {
                return Memory.wrap(env, yaml.load(args[0].toString()));
            }
        } catch (YAMLException e) {
            env.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        }
    }

    @Override
    @Signature
    public Memory format(Environment env, Memory... args) {
        try {
            return StringMemory.valueOf(yaml.dump(Memory.unwrap(env, args[0], true)));
        } catch (YAMLException e) {
            env.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        }
    }

    @Override
    @Signature
    public Memory formatTo(Environment env, Memory... args) {
        try {
            yaml.dump(
                    Memory.unwrap(env, args[0], true),
                    new OutputStreamWriter(Stream.getOutputStream(env, args[0]), env.getDefaultCharset())
            );

            return Memory.NULL;
        } catch (YAMLException e) {
            env.exception(ProcessorException.class, e.getMessage());
            return Memory.NULL;
        }
    }
}
