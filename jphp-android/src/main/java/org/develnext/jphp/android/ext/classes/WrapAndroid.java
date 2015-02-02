package org.develnext.jphp.android.ext.classes;

import android.content.Intent;
import org.develnext.jphp.android.AndroidStandaloneLoader;
import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Name(AndroidExtension.NAMESPACE + "Android")
public class WrapAndroid extends BaseObject {
    public WrapAndroid(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public static void startActivity(Environment env, String clazz) throws ClassNotFoundException {
        ClassEntity entity = env.fetchClass(clazz);
        if (entity == null) {
            env.exception(Messages.ERR_CLASS_NOT_FOUND.fetch(clazz));
            return;
        }

        Intent intent = new Intent(AndroidStandaloneLoader.getContext(), entity.getNativeClass());

        AndroidStandaloneLoader.getMainActivity().startActivity(intent);
    }
}
