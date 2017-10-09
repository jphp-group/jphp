package org.develnext.jphp.android.ext.bind;

import android.app.Activity;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class ActivityMemoryOperation extends MemoryOperation<Activity> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Activity.class };
    }

    @Override
    public Activity convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        return arg.toObject(WrapActivity.class);
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Activity arg) throws Throwable {
        return Memory.NULL;
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeNativeClass(WrapActivity.class);
    }
}
