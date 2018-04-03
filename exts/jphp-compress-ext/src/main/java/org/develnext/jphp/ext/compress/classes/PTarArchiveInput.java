package org.develnext.jphp.ext.compress.classes;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name("TarArchiveInput")
@Namespace(CompressExtension.NS)
public class PTarArchiveInput extends PArchiveInput<TarArchiveInputStream> {

    public PTarArchiveInput(Environment env, TarArchiveInputStream wrappedObject) {
        super(env, wrappedObject);
    }

    public PTarArchiveInput(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(InputStream inputStream) {
        __construct(inputStream, TarConstants.DEFAULT_BLKSIZE);
    }

    @Signature
    public void __construct(InputStream inputStream, int blockSize) {
        __construct(inputStream, blockSize, null);
    }

    @Signature
    public void __construct(InputStream inputStream, int blockSize, String encoding) {
        __wrappedObject = new TarArchiveInputStream(inputStream, blockSize, encoding != null && encoding.isEmpty() ? null : encoding);
    }

    @Override
    public PArchiveEntry nextEntry(Environment env) throws IOException {
        TarArchiveEntry entry = getWrappedObject().getNextTarEntry();
        return entry == null ? null : new PTarArchiveEntry(env, entry);
    }
}
