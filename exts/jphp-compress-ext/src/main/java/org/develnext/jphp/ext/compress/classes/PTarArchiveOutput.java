package org.develnext.jphp.ext.compress.classes;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name("TarArchiveOutput")
@Namespace(CompressExtension.NS)
public class PTarArchiveOutput extends PArchiveOutput<TarArchiveOutputStream> {
    public static final int BLOCK_SIZE_UNSPECIFIED = -511;

    public PTarArchiveOutput(Environment env, TarArchiveOutputStream wrappedObject) {
        super(env, wrappedObject);
    }

    public PTarArchiveOutput(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(OutputStream outputStream) {
        __construct(outputStream, BLOCK_SIZE_UNSPECIFIED);
    }

    @Signature
    public void __construct(OutputStream outputStream, int blockSize) {
        __construct(outputStream, blockSize, null);
    }

    @Signature
    public void __construct(OutputStream outputStream, int blockSize, String encoding) {
        __wrappedObject = new TarArchiveOutputStream(outputStream, blockSize, encoding != null && encoding.isEmpty() ? null : encoding);
    }

    @Override
    public PArchiveEntry createEntry(Environment env, File inputFile, String entryName) throws IOException {
        return new PTarArchiveEntry(env, (TarArchiveEntry) getWrappedObject().createArchiveEntry(inputFile, entryName));
    }
}
