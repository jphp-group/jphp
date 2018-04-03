package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;

@Name("ZipArchiveInput")
@Namespace(CompressExtension.NS)
public class PZipArchiveInput extends PArchiveInput<ZipArchiveInputStream> {
    public PZipArchiveInput(Environment env, ZipArchiveInputStream wrappedObject) {
        super(env, wrappedObject);
    }

    public PZipArchiveInput(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(InputStream inputStream) {
        __construct(inputStream, null);
    }

    @Signature
    public void __construct(InputStream inputStream, String encoding) {
        __wrappedObject = new ZipArchiveInputStream(inputStream, encoding != null && encoding.isEmpty() ? null : encoding);
    }

    @Override
    public PArchiveEntry nextEntry(Environment env) throws IOException {
        ZipArchiveEntry entry = getWrappedObject().getNextZipEntry();
        return entry == null ? null : new PZipArchiveEntry(env, entry);
    }
}
