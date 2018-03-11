package org.develnext.jphp.parser.classes;

import org.develnext.jphp.parser.ParserExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Namespace(ParserExtension.NS)
public class SourceManager extends BaseObject {
    protected Environment env = new Environment();
    protected Map<String, SourceFile> sourceFileMap = new ConcurrentHashMap<>();

    public SourceManager(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void update(Environment env, Memory any) throws Throwable {
        if (any.instanceOf(SourceFile.class)) {
            SourceFile sourceFile = any.toObject(SourceFile.class);

            env.invokeMethod(sourceFile, "update", ObjectMemory.valueOf(this));
        }
    }

    @Signature
    public void write(Environment env, SourceFile file, SourceWriter writer) throws Throwable {
        writer.write(env, this, file);
        env.invokeMethod(this, "update", ObjectMemory.valueOf(file));
    }

    @Signature
    public Memory find(Invoker filter) {
        for (SourceFile sourceFile : sourceFileMap.values()) {
            Memory memory = filter.callAny(sourceFile.getModuleRecord());

            if (!memory.isNull()) {
                return memory;
            }
        }

        return Memory.NULL;
    }

    @Signature
    public Memory findAll(Invoker filter) {
        ArrayMemory result = new ArrayMemory();

        for (SourceFile sourceFile : sourceFileMap.values()) {
            Memory memory = filter.callAny(sourceFile.getModuleRecord());

            if (!memory.isNull()) {
                if (memory.isTraversable()) {
                    ForeachIterator iterator = memory.getNewIterator(env);
                    while (iterator.next()) {
                        result.add(iterator.getValue().toImmutable());
                    }
                } else {
                    result.add(memory);
                }
            }
        }

        return result.toConstant();
    }

    @Signature
    synchronized public SourceFile get(Environment env, Memory path, String uniqueId) {
        if (sourceFileMap.containsKey(uniqueId)) {
            return sourceFileMap.get(uniqueId);
        }

        SourceFile sourceFile = new SourceFile(env);
        sourceFile.__construct(env, path, uniqueId);

        sourceFileMap.put(uniqueId, sourceFile);

        return sourceFile;
    }
}
