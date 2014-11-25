package org.develnext.jphp.android.ext.classes.widget;


import android.widget.Toast;
import org.develnext.jphp.android.AndroidApplication;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(AndroidExtension.NAMESPACE + "widget\\Toast")
@Reflection.WrapInterface(WrapToast.Methods.class)
public class WrapToast extends BaseWrapper<Toast> {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    public WrapToast(Environment env, Toast wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapToast(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
        __wrappedObject = new Toast(AndroidApplication.INSTANCE.getContext());
    }

    @Signature
    public static Toast makeText(String value) {
        return Toast.makeText(AndroidApplication.INSTANCE.getContext(), value, Toast.LENGTH_LONG);
    }

    interface Methods {
        void show();
        void cancel();
        void setDuration(int duration);
        void getDuration();
        void setMargin(float horizontalMargin, float verticalMargin);
        float getHorizontalMargin();
        float getVerticalMargin();
        void setGravity(int gravity, int xOffset, int yOffset);
        int getGravity();
        int getXOffset();
        int getYOffset();
        void setText(java.lang.CharSequence s);
    }
}
