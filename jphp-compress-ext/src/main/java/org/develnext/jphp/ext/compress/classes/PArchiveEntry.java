package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.arj.ArjArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.jar.JarArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("ArchiveEntry")
@Reflection.Namespace(CompressExtension.NS)
public class PArchiveEntry extends BaseWrapper<ArchiveEntry> {
    public PArchiveEntry(Environment env, ArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, ArArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, DumpArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, ZipArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, JarArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, TarArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, SevenZArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, ArjArchiveEntry wrappedObject) {
        super(env, wrappedObject);
    }

    public PArchiveEntry(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String format, String name, ArrayMemory options) {
        switch (format) {
            case "zip":
                __wrappedObject = new ZipArchiveEntry(name);
                break;

            case "tar":
                __wrappedObject = new TarArchiveEntry(name);
                break;

            case "jar":
                __wrappedObject = new JarArchiveEntry(name);
                break;

            case "7z":
                __wrappedObject = make7Zip(name, options);
                break;

            default:
                throw new IllegalArgumentException("Invalid archive format - " + format);
        }
    }

    @Signature
    public String getName() {
        return getWrappedObject().getName();
    }

    @Signature
    public boolean isDirectory() {
        return getWrappedObject().isDirectory();
    }

    @Signature
    public long getSize() {
        return getWrappedObject().getSize();
    }

    @Signature
    public void setSize(long size) {
        if (getWrappedObject() instanceof ZipArchiveEntry) {
            ((ZipArchiveEntry) getWrappedObject()).setSize(size);
        } else if (getWrappedObject() instanceof TarArchiveEntry) {
            ((TarArchiveEntry) getWrappedObject()).setSize(size);
        }
    }

    @Signature
    public long getLastModifiedDate() {
        return getWrappedObject().getLastModifiedDate().getTime();
    }

    protected SevenZArchiveEntry make7Zip(String name, ArrayMemory options) {
        SevenZArchiveEntry entry = new SevenZArchiveEntry();
        entry.setName(name);
        entry.setAntiItem(options.valueOfIndex("antiItem").toBoolean());
        entry.setDirectory(options.valueOfIndex("directory").toBoolean());

        if (options.containsKey("crc")) {
            entry.setCrcValue(options.valueOfIndex("crc").toLong());
            entry.setHasCrc(true);
        }

        if (options.containsKey("size")) {
            entry.setSize(options.valueOfIndex("size").toLong());
        }

        return entry;
    }
}
