package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;

import java.io.File;

public class FileMemoryOperation extends MemoryOperation<File> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { File.class };
    }

    @Override
    public File convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.isNull() ? null : FileObject.valueOf(arg);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, File arg) throws Throwable {
        return arg == null ? Memory.NULL : ObjectMemory.valueOf(new FileObject(env, arg));
    }
}
