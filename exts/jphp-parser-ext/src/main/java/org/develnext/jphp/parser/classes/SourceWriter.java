package org.develnext.jphp.parser.classes;

import org.develnext.jphp.parser.ParserExtension;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

@Namespace(ParserExtension.NS)
abstract public class SourceWriter extends BaseObject {
    public SourceWriter(Environment env) {
        super(env);
    }

    public SourceWriter(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void write(Environment env, SourceManager manager, SourceFile file) throws Throwable {
        Context context = file.getContext();

        if (!file.isReadOnly()) {
            Stream stream = Stream.create(env, context.getFileName(), "w+");

            try {
                env.invokeMethod(
                        this, "writeModule",
                        ObjectMemory.valueOf(manager),
                        ObjectMemory.valueOf(file),
                        ObjectMemory.valueOf(file.getModuleRecord()),
                        ObjectMemory.valueOf(stream)
                );
            } finally {
                env.invokeMethod(stream, "close");
            }
        }
    }

    @Signature
    abstract public void writeModule(Environment env, SourceManager manager, SourceFile file, ModuleRecord record, Stream stream);
}
