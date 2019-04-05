package org.develnext.jphp.zend.ext.standard.date;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Name("DateTimeZone")
public class DateTimeZone extends BaseObject {
    public DateTimeZone(Environment env) {
        super(env);
    }

    public DateTimeZone(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
