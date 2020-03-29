package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.memory.support.MemoryOperation;

import java.io.*;

public class WriterMemoryStream extends MemoryOperation<Writer> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { Writer.class };
    }

    @Override
    public Writer convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) return null;

        if (arg.instanceOf(Stream.class)) {
            OutputStream outputStream = Stream.getOutputStream(env, arg);
            if (outputStream == null) return null;

            return new OutputStreamWriter(outputStream, env.getDefaultCharset());
        } else {
            Stream stream = Stream.create(env, arg.toString(), "w+");
            return new OutputStreamWriter(Stream.getOutputStream(env, stream), env.getDefaultCharset());
        }
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Writer arg) throws Throwable {
        throw new CriticalException("Unsupported operation");
    }
}
