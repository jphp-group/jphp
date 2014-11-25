package org.develnext.jphp.android.ext.classes;

import org.develnext.jphp.android.ext.AndroidExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.lang.reflect.Field;

@Name(AndroidExtension.NAMESPACE + "R")
final public class WrapR extends BaseObject {
    public WrapR(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() { }

    @Signature
    public static int string(String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = android.R.string.class.getField(name);
        return field.getInt(null);
    }

    @Signature
    public static int id(String name) throws NoSuchFieldException, IllegalAccessException {
        Field field = android.R.id.class.getField(name);
        return field.getInt(null);
    }
}
