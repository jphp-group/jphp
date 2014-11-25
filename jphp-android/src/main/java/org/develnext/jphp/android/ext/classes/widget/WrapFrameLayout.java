package org.develnext.jphp.android.ext.classes.widget;

import android.widget.FrameLayout;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.view.WrapViewGroup;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "widget\\FrameLayout")
public class WrapFrameLayout extends WrapViewGroup {
    public WrapFrameLayout(Environment env, FrameLayout wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapFrameLayout(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public FrameLayout getWrappedObject() {
        return (FrameLayout) super.getWrappedObject();
    }
}
