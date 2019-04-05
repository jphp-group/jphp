package org.develnext.jphp.zend.ext.standard.date;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Traversable;
import php.runtime.reflection.ClassEntity;

@Reflection.Name("DatePeriod")
public class DatePeriod extends BaseObject implements Traversable {
    public DatePeriod(Environment env) {
        super(env);
    }

    public DatePeriod(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return null;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return null;
    }
}
