package org.develnext.jphp.ext.compress.classes;

import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.Stream;
import php.runtime.reflection.ClassEntity;

@Name("ZipArchive")
@Namespace(CompressExtension.NS)
public class PZipArchive extends PArchive {
    public PZipArchive(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected PArchiveInput createInput(Environment env) {
        return new PZipArchiveInput(env, new ZipArchiveInputStream(Stream.getInputStream(env, getSource())));
    }

    @Override
    protected PArchiveOutput createOutput(Environment env) {
        return new PZipArchiveOutput(env, new ZipArchiveOutputStream(Stream.getOutputStream(env, getSource())));
    }
}
