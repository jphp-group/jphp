package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;

import java.io.OutputStream;
import java.io.PrintStream;

public class OutputStreamMemoryOperation extends MemoryOperation<OutputStream> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { OutputStream.class };
    }

    @Override
    public OutputStream convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return Stream.getOutputStream(env, arg);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, OutputStream arg) throws Throwable {
        return ObjectMemory.valueOf(new MiscStream(env, arg));
    }

    @Override
    public void releaseConverted(Environment env, TraceInfo info, OutputStream arg) {
        Stream.closeStream(env, arg);
    }
}
