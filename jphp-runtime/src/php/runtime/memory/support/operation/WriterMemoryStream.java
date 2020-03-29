package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.ext.core.classes.stream.FileObject;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.memory.support.MemoryOperation;

import java.io.*;

public class ReaderMemoryStream extends MemoryOperation<Reader> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class[] { Reader.class };
    }

    @Override
    public Reader convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) return null;

        if (arg.instanceOf(Stream.class)) {
            InputStream inputStream = Stream.getInputStream(env, arg);
            if (inputStream == null) return null;

            return new InputStreamReader(inputStream, env.getDefaultCharset());
        } else if (arg.instanceOf(FileObject.class)) {
            Stream stream = Stream.create(env, arg.toString(), "r");
            return new InputStreamReader(Stream.getInputStream(env, stream), env.getDefaultCharset());
        }

        return new StringReader(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Reader arg) throws Throwable {
        throw new CriticalException("Unsupported operation");
    }
}
