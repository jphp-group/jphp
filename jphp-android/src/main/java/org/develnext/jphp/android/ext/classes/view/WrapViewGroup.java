package org.develnext.jphp.android.ext.classes.view;

import android.view.ViewGroup;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(AndroidExtension.NAMESPACE + "view\\ViewGroup")
public class WrapViewGroup extends WrapView {
    public WrapViewGroup(Environment env, ViewGroup wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapViewGroup(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ViewGroup getWrappedObject() {
        return (ViewGroup) super.getWrappedObject();
    }
}
