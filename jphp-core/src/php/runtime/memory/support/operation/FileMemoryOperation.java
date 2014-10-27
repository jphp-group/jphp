package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;

import java.io.File;
import java.io.OutputStream;

public class FileMemoryOperation extends MemoryOperation<File> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { File.class };
    }

    @Override
    public File convert(Environment env, TraceInfo trace, Memory arg) {
        return FileObject.valueOf(arg);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, File arg) {
        return ObjectMemory.valueOf(new FileObject(env, arg));
    }
}
