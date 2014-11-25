package org.develnext.jphp.android.ext.classes;

import android.content.Intent;
import org.develnext.jphp.android.AndroidApplication;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(AndroidExtension.NAMESPACE + "Android")
public class WrapAndroid extends BaseObject {
    public WrapAndroid(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public static void startActivity(Environment env, String clazz) throws ClassNotFoundException {
        Intent intent = new Intent(AndroidApplication.INSTANCE.getContext(), env.fetchClass(clazz).getNativeClazz());
        AndroidApplication.INSTANCE.getMainActivity().startActivity(intent);
    }
}
