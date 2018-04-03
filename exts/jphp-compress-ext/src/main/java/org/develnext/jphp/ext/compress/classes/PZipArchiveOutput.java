package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@Name("ZipArchiveOutput")
@Namespace(CompressExtension.NS)
public class PZipArchiveOutput extends PArchiveOutput<ZipArchiveOutputStream> {
    public PZipArchiveOutput(Environment env, ZipArchiveOutputStream wrappedObject) {
        super(env, wrappedObject);
    }

    public PZipArchiveOutput(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(OutputStream outputStream) {
        __wrappedObject = new ZipArchiveOutputStream(outputStream);
    }

    @Override
    public PArchiveEntry createEntry(Environment env, File inputFile, String entryName) throws IOException {
        return new PZipArchiveEntry(env, (ZipArchiveEntry) getWrappedObject().createArchiveEntry(inputFile, entryName));
    }
}
