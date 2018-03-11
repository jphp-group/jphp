package org.develnext.jphp.ext.zip.classes;

import org.develnext.jphp.ext.zip.ZipExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("ZipException")
@Reflection.Namespace(ZipExtension.NS)
public class PZipException extends JavaException {
    public PZipException(Environment env, Throwable throwable) {
        super(env, throwable);
    }

    public PZipException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
