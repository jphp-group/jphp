package org.develnext.jphp.ext.libgdx.classes;

import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.IObject;

import static php.runtime.annotation.Reflection.Arg;
import static php.runtime.annotation.Reflection.Name;
import static php.runtime.annotation.Reflection.Signature;

@Name(LibGDXExtension.NAMESPACE + "ApplicationListener")
public interface WrapApplicationListener extends IObject {
    @Signature
    Memory create(Environment env, Memory... args);

    @Signature({@Arg("width"), @Arg("height")})
    Memory resize(Environment env, Memory... args);

    @Signature
    Memory render(Environment env, Memory... args);

    @Signature
    Memory pause(Environment env, Memory... args);

    @Signature
    Memory resume(Environment env, Memory... args);

    @Signature
    Memory dispose(Environment env, Memory... args);
}
