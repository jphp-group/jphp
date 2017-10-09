package org.develnext.jphp.android.ext.classes.widget;

import android.widget.Button;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "widget\\Button")
public class WrapButton extends WrapTextView {
    public WrapButton(Environment env, Button wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapButton(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(WrapActivity activity) {
        __wrappedObject = new Button(activity);
        __wrappedObject.setId(idCounter.getAndIncrement());
    }

    @Override
    public Button getWrappedObject() {
        return (Button) super.getWrappedObject();
    }
}
