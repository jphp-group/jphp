package org.develnext.jphp.android.ext.classes.widget;

import android.widget.TextView;
import org.develnext.jphp.android.AndroidApplication;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.view.WrapView;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "widget\\TextView")
public class WrapTextView extends WrapView {
    public WrapTextView(Environment env, TextView wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapTextView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new TextView(AndroidApplication.INSTANCE.getContext());
    }
}
