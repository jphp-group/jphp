package org.develnext.jphp.ext.compress.classes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name("ArchiveInput")
@Namespace(CompressExtension.NS)
abstract public class PArchiveInput<T extends ArchiveInputStream> extends BaseWrapper<T> {
    interface WrappedInterface {
        boolean canReadEntryData(ArchiveEntry archiveEntry);
    }

    public PArchiveInput(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveInput(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    abstract public PArchiveEntry nextEntry(Environment env) throws IOException;

    @Override
    public T getWrappedObject() {
        return super.getWrappedObject();
    }

    @Signature
    public InputStream stream() {
        return getWrappedObject();
    }
}
