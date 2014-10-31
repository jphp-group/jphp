package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class UriMemoryOperation extends MemoryOperation<URI> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { URI.class };
    }

    @Override
    public URI convert(Environment env, TraceInfo trace, Memory arg) {
        return URI.create(arg.toString());
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, URI arg) {
        return StringMemory.valueOf(arg.toString());
    }
}
