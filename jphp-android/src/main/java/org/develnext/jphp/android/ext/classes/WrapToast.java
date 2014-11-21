package org.develnext.jphp.android.ext.classes;


import android.widget.Toast;
import org.develnext.jphp.android.AndroidApplication;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(AndroidExtension.NAMESPACE + "widget\\Toast")
public class WrapToast extends BaseWrapper<Toast> {
    public WrapToast(Environment env, Toast wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapToast(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public static void makeText(String value) {
        Toast.makeText(AndroidApplication.INSTANCE.getContext(), value, Toast.LENGTH_LONG);
    }
}
