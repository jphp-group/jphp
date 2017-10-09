package org.develnext.jphp.android.ext.classes.widget;

import android.widget.LinearLayout;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import org.develnext.jphp.android.ext.classes.view.WrapViewGroup;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "widget\\LinearLayout")
public class WrapLinearLayout extends WrapViewGroup {
    public WrapLinearLayout(Environment env, LinearLayout wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapLinearLayout(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public LinearLayout getWrappedObject() {
        return (LinearLayout) super.getWrappedObject();
    }

    @Signature
    public void __construct(WrapActivity activity) {
        __wrappedObject = new LinearLayout(activity);
        getWrappedObject().setOrientation(LinearLayout.VERTICAL);
    }

           /*
    @Override
    @Signature
    public void addView(View view) {
        getWrappedObject().addView(view, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
    } */
}
