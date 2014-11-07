package php.runtime.reflection;


import php.runtime.Memory;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;

import java.lang.reflect.Field;

public class WrapCompilePropertyEntity extends CompilePropertyEntity {
    public WrapCompilePropertyEntity(Context context, Field field) {
        super(context, field);
    }

    @Override
    public Memory assignValue(Environment env, TraceInfo trace, Object object, String name, Memory value) throws IllegalAccessException {
        return super.assignValue(env, trace, ((BaseWrapper)object).getWrappedObject(), name, value);
    }

    @Override
    public Memory getValue(Environment env, TraceInfo trace, Object object) throws IllegalAccessException {
        return super.getValue(env, trace, ((BaseWrapper)object).getWrappedObject());
    }
}
