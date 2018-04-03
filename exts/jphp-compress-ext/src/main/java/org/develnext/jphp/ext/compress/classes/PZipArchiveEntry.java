package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.util.Map;

@Name("ZipArchiveEntry")
@Namespace(CompressExtension.NS)
public class PZipArchiveEntry extends PArchiveEntry<ZipArchiveEntry> implements ICloneableObject<PZipArchiveEntry> {
    interface WrappedInterface {
        @Property String comment();
        @Property int method();
        @Property int unixMode();
        @Property int crc();
        @Property long time();

        boolean isUnixSymlink();
    }

    public PZipArchiveEntry(Environment env, ZipArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PZipArchiveEntry(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String name) {
        __construct(name, 0);
    }

    @Override
    public PZipArchiveEntry __clone(Environment env, TraceInfo trace) {
        return new PZipArchiveEntry(env, (ZipArchiveEntry) getWrappedObject().clone());
    }

    @Signature
    public void __construct(String name, long size) {
        __wrappedObject = new ZipArchiveEntry(name);
        getWrappedObject().setSize(size);
    }

    @Signature
    public PZipArchiveEntry ofFile(Environment env, File file) {
        return new PZipArchiveEntry(env, new ZipArchiveEntry(file, file.getName()));
    }

    @Signature
    public PZipArchiveEntry ofFile(Environment env, File file, String fileName) {
        return new PZipArchiveEntry(env, new ZipArchiveEntry(file, fileName));
    }

    @Setter
    public void setSize(long size) {
        getWrappedObject().setSize(size);
    }
}
