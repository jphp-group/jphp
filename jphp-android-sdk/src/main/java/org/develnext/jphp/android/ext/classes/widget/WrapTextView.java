package org.develnext.jphp.android.ext.classes.widget;

import android.widget.TextView;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import org.develnext.jphp.android.ext.classes.view.WrapView;
import php.runtime.Memory;
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
    public void __construct(WrapActivity activity) {
        __wrappedObject = new TextView(activity);
        __wrappedObject.setId(idCounter.getAndIncrement());
    }

    @Override
    public TextView getWrappedObject() {
        return (TextView) super.getWrappedObject();
    }

    @Signature
    public void setText(String text) {
        getWrappedObject().setText(text);
    }

    @Signature
    public CharSequence getText() {
        return getWrappedObject().getText();
    }

    @Signature
    public void setInputType(int type) {
        getWrappedObject().setInputType(type);
    }

    @Signature
    public int getInputType() {
        return getWrappedObject().getInputType();
    }
}
