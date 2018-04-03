package org.develnext.jphp.ext.compress.classes;

import org.develnext.jphp.ext.compress.CompressExtension;
import org.develnext.jphp.ext.compress.support.ConstrainedInputStream;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.FsUtils;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.io.*;

@Name("Archive")
@Namespace(CompressExtension.NS)
abstract public class PArchive extends BaseObject {
    protected Memory source;

    private PArchiveOutput output;
    private OutputStream outputStream;

    public PArchive(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    abstract protected PArchiveInput createInput(Environment env);

    @Signature
    abstract protected PArchiveOutput createOutput(Environment env);

    @Signature
    public void __construct(Environment env, Memory source) {
        this.source = source;
    }

    @Getter
    public Memory getSource() {
        return source;
    }

    private PArchiveOutput fetchOutput(Environment env) throws IOException {
        if (output == null) {
            throw new IOException("Archive is not opened to write, use open() method before write any data.");
        }

        return output;
    }

    @Signature
    public void open(Environment env) {
        output = createOutput(env);
        outputStream = output.stream();
    }

    @Signature
    public void close(Environment env) throws IOException {
        PArchiveOutput output = fetchOutput(env);
        outputStream.close();
    }

    @Signature
    public void addFile(Environment env,
                        File file,
                        @Nullable @Optional("null") Memory localName,
                        @Optional("null") Invoker progressHandler,
                        @Optional("8192") int bufferSize
    ) throws Throwable {
        PArchiveEntry entry = fetchOutput(env).createEntry(env, file, localName.isNull() ? file.getName() : localName.toString());
        env.invokeMethod(output, "putEntry", ObjectMemory.valueOf(entry));

        try (InputStream inputStream = new FileInputStream(file)) {
            FsUtils.copy(inputStream, outputStream, progressHandler, bufferSize);
            env.invokeMethod(output, "closeEntry");
        }
    }

    @Signature
    public void addFromString(Environment env, PArchiveEntry entry, Memory contents) throws Throwable {
        fetchOutput(env);

        byte[] binaryBytes = contents.getBinaryBytes(env.getDefaultCharset());
        env.assignProperty(entry, "size", LongMemory.valueOf(binaryBytes.length));

        env.invokeMethod(output, "putEntry", ObjectMemory.valueOf(entry));
        outputStream.write(binaryBytes);
        env.invokeMethod(output, "closeEntry");
    }

    @Signature
    public void addEntry(Environment env,
                         PArchiveEntry entry,
                         @Nullable InputStream source,
                         @Optional("null") Invoker progressHandler,
                         @Optional("8192") int bufferSize
    ) throws Throwable {
        fetchOutput(env);

        env.invokeMethod(output, "putEntry", ObjectMemory.valueOf(entry));
        if (source != null) {
            FsUtils.copy(source, outputStream, progressHandler, bufferSize);
        }

        env.invokeMethod(output, "closeEntry");
    }

    @Signature
    public void addEmptyEntry(Environment env, PArchiveEntry entry) throws Throwable {
        fetchOutput(env);

        env.invokeMethod(output, "putEntry", ObjectMemory.valueOf(entry));
        env.invokeMethod(output, "closeEntry");
    }

    protected Memory read(Environment env, InputStream inputStream, PArchiveEntry entry, Invoker callback) {
        if (entry != null) {
            if (!entry.isDirectory()) {
                long size = entry.getSize();

                if (callback != null) {
                    if (size != -1) {
                        ConstrainedInputStream entryStream = new ConstrainedInputStream(inputStream, size);
                        return callback.callAny(entry, new MiscStream(env, entryStream));
                    } else {
                        return callback.callAny(entry, new MiscStream(env, inputStream));
                    }
                }
            } else {
                if (callback != null) {
                    return callback.callAny(entry, Memory.NULL);
                }
            }
        }

        return Memory.NULL;
    }

    @Signature
    public PArchiveEntry entry(Environment env, String path) throws Throwable {
        PArchiveInput input = createInput(env);

        try {
            PArchiveEntry entry;

            while ((entry = env.invokeMethod(input, "nextEntry").toObject(PArchiveEntry.class)) != null) {
                String name = entry.getName();

                if (name.equals(path)) {
                    return entry;
                }
            }

            return null;
        } finally {
            input.getWrappedObject().close();
        }
    }

    @Signature
    public Memory entries(Environment env) throws Throwable {
        ArrayMemory result = ArrayMemory.createHashed();

        PArchiveInput input = createInput(env);

        try {
            PArchiveEntry entry;

            while ((entry = env.invokeMethod(input, "nextEntry").toObject(PArchiveEntry.class)) != null) {
                result.put(entry.getName(), entry);
            }
        } finally {
            input.getWrappedObject().close();
        }

        return result;
    }

    @Signature
    public PArchiveEntry read(Environment env, String path, @Nullable @Reflection.Optional("null") final Invoker callback) throws Throwable {
        PArchiveInput input = createInput(env);

        try {
            PArchiveEntry entry;

            while ((entry = env.invokeMethod(input, "nextEntry").toObject(PArchiveEntry.class)) != null) {
                String name = entry.getName();

                if (name.equals(path)) {
                    read(env, input.stream(), entry, callback);
                    return entry;
                }
            }

            return null;
        } finally {
            input.getWrappedObject().close();
        }
    }

    @Signature
    public Memory readAll(Environment env, @Nullable @Reflection.Optional("null") final Invoker callback) throws Throwable {
        ArrayMemory result = ArrayMemory.createHashed();

        PArchiveInput input = createInput(env);

        try {
            PArchiveEntry entry;

            while ((entry = env.invokeMethod(input, "nextEntry").toObject(PArchiveEntry.class)) != null) {
                if (read(env, input.stream(), entry, callback).toBoolean()) {
                    break;
                }

                result.put(entry.getName(), entry);
            }
        } finally {
            input.getWrappedObject().close();
        }

        return result;
    }
}
