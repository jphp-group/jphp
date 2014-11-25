package org.develnext.jphp.android.ext.classes.view;

import android.view.View;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "view\\View")
public class WrapView extends BaseWrapper<View> {
    public WrapView(Environment env, View wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        throw new RuntimeException("Stub");
    }
}
