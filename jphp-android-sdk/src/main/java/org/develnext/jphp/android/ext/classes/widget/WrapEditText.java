package org.develnext.jphp.android.ext.classes.widget;

import android.widget.EditText;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "widget\\EditText")
public class WrapEditText extends WrapTextView {
    public WrapEditText(Environment env, EditText wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapEditText(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(WrapActivity activity) {
        __wrappedObject = new EditText(activity);
        __wrappedObject.setId(idCounter.getAndIncrement());
    }

    @Override
    public EditText getWrappedObject() {
        return (EditText) super.getWrappedObject();
    }
}
