package org.develnext.jphp.ext.compress.classes;

import java.util.Date;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Setter;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Abstract
@Name("ArchiveEntry")
@Namespace(CompressExtension.NS)
abstract public class PArchiveEntry<T extends ArchiveEntry> extends BaseWrapper<T> {


    public PArchiveEntry(Environment env, T wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public T getWrappedObject() {
        return super.getWrappedObject();
    }

    @Signature
    public boolean isDirectory() {
        return getWrappedObject().isDirectory();
    }

    @Getter
    public String getName() {
        return getWrappedObject().getName();
    }

    @Getter
    public long getSize() {
        return getWrappedObject().getSize();
    }

    @Getter
    public Date getLastModifiedDate() {
        return getWrappedObject().getLastModifiedDate();
    }
}
