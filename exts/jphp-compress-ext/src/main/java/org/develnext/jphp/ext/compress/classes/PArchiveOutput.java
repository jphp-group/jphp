package org.develnext.jphp.ext.compress.classes;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.lang.BaseObject;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

@Name("ArchiveOutput")
@Namespace(CompressExtension.NS)
abstract public class PArchiveOutput<T extends ArchiveOutputStream> extends BaseWrapper<T> {
    interface WrappedInterface {
        void finish();
        @Name("closeEntry") void closeArchiveEntry();
        @Name("putEntry") void putArchiveEntry(ArchiveEntry entry);
        boolean canWriteEntryData(ArchiveEntry entry);

        long getBytesWritten();
    }

    public PArchiveOutput(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveOutput(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    abstract public PArchiveEntry createEntry(Environment env, File inputFile, String entryName) throws IOException;

    @Signature
    public OutputStream stream() {
        return getWrappedObject();
    }
}
