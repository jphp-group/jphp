package org.develnext.jphp.ext.libgdx.classes;

import com.badlogic.gdx.Application;
import org.develnext.jphp.ext.libgdx.LibGDXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.annotation.Reflection.WrapInterface;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(LibGDXExtension.NAMESPACE + "Application")
@WrapInterface(value = Application.class, skipConflicts = true)
public class WrapApplication extends BaseWrapper<Application> {
    public static final int LOG_NONE = 0;
    public static final int LOG_DEBUG = 3;
    public static final int LOG_INFO = 2;
    public static final int LOG_ERROR = 1;

    public WrapApplication(Environment env, Application wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapApplication(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void halt() {
        __wrappedObject.exit();
    }
}
