package org.develnext.jphp.ext.compress.classes;

import org.develnext.jphp.ext.compress.CompressExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.stream.WrapIOException;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("ZipException")
@Reflection.Namespace(CompressExtension.NS)
public class PZipException extends WrapIOException {
    public PZipException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public PZipException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
