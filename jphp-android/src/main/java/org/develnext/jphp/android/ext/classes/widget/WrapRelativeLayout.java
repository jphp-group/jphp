package org.develnext.jphp.android.ext.classes.widget;

import android.widget.RelativeLayout;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.view.WrapViewGroup;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "widget\\RelativeLayout")
public class WrapRelativeLayout extends WrapViewGroup {
    public WrapRelativeLayout(Environment env, RelativeLayout wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapRelativeLayout(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public RelativeLayout getWrappedObject() {
        return (RelativeLayout) super.getWrappedObject();
    }
}
