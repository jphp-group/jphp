package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.*;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.memory.LongMemory;
import php.runtime.reflection.ClassEntity;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

@Reflection.Name("ArchiveOutputStream")
@Reflection.Namespace(CompressExtension.NS)
public class PArchiveOutputStream extends MiscStream {
    public PArchiveOutputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String format, OutputStream is) throws ArchiveException {
        outputStream = new ArchiveStreamFactory().createArchiveOutputStream(format, is);
        setPath("");
        setMode("w+");
    }

    @Signature
    public ArchiveEntry createEntry(File file, String name) throws IOException {
        return ((ArchiveOutputStream) outputStream).createArchiveEntry(file, name);
    }

    @Signature
    public void addEntry(ArchiveEntry entry) throws IOException {
        ((ArchiveOutputStream) outputStream).putArchiveEntry(entry);
    }

    @Signature
    public void closeEntry() throws IOException {
        ((ArchiveOutputStream) outputStream).closeArchiveEntry();
    }

    @Override
    @Signature
    public Memory getPosition(Environment env, Memory... args) {
        return LongMemory.valueOf( ((ArchiveOutputStream) outputStream).getBytesWritten() );
    }

    @Signature
    public boolean canAddEntry(ArchiveEntry entry) {
        return ((ArchiveOutputStream) outputStream).canWriteEntryData(entry);
    }

    @Signature
    public ArchiveEntry addFile(File file, String name) throws IOException {
        ArchiveEntry entry = createEntry(file, name);
        addEntry(entry);
        return entry;
    }

}
