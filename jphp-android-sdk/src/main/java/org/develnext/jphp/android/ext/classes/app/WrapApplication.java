package org.develnext.jphp.android.ext.classes.app;

import android.app.Application;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "app\\Application")
public class WrapApplication extends BaseWrapper<Application> {
    public WrapApplication(Environment env, Application wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapApplication(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
