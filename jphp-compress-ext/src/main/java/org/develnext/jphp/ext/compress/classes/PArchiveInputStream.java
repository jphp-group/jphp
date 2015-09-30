package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.MiscStream;
import php.runtime.reflection.ClassEntity;

import java.io.IOException;
import java.io.InputStream;

@Reflection.Name("ArchiveInputStream")
@Reflection.Namespace(CompressExtension.NS)
public class PArchiveInputStream extends MiscStream {
    public PArchiveInputStream(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(String format, InputStream is) throws ArchiveException {
        inputStream = new ArchiveStreamFactory().createArchiveInputStream(format, is);
        setPath("");
        setMode("r");
    }

    @Signature
    public ArchiveEntry nextEntry() throws IOException {
        return ((ArchiveInputStream) inputStream).getNextEntry();
    }
}
