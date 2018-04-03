package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.reflection.ClassEntity;

@Name("TarArchive")
@Namespace(CompressExtension.NS)
public class PTarArchive extends PArchive {
    public PTarArchive(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected PArchiveInput createInput(Environment env) {
        return new PTarArchiveInput(env, new TarArchiveInputStream(Stream.getInputStream(env, getSource())));
    }

    @Override
    protected PArchiveOutput createOutput(Environment env) {
        return new PTarArchiveOutput(env, new TarArchiveOutputStream(Stream.getOutputStream(env, getSource())));
    }
}
