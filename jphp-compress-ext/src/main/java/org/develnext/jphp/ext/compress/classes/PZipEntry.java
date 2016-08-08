package org.develnext.jphp.ext.compress.classes;

import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.zip.ZipEntry;

@Reflection.Name("ZipEntry")
@Reflection.Namespace(CompressExtension.NS)
public class PZipEntry extends BaseWrapper<ZipEntry> {
    interface WrappedInterface {
        @Property String name();
        @Property long size();
        @Property String comment();
        @Property long time();
        @Property long crc();
        @Property long compressedSize();
        @Property int method();

        @Property byte[] extra();

        boolean isDirectory();
    }

    public PZipEntry(Environment env, ZipEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PZipEntry(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String name) {
        __wrappedObject = new ZipEntry(name);
    }
}
