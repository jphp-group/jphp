package org.develnext.jphp.android.ext.classes.view;

import android.view.View;
import org.develnext.jphp.android.ext.AndroidExtension;
import org.develnext.jphp.android.ext.classes.app.WrapActivity;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.util.concurrent.atomic.AtomicInteger;

@Name(AndroidExtension.NAMESPACE + "view\\View")
public class WrapView extends BaseWrapper<View> {
    protected final static AtomicInteger idCounter = new AtomicInteger(100000);

    public WrapView(Environment env, View wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapView(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(WrapActivity activity) {
        throw new RuntimeException("Stub");
    }

    @Signature
    public void on(Environment env, String event, final Invoker invoker) {
        AndroidExtension.bindEvent(env, getWrappedObject(), event, invoker);
    }

    @Signature
    public void off(Environment env, String event) {
        AndroidExtension.unbindEvent(env, getWrappedObject(), event);
    }

    @Signature
    public void trigger(Environment env, String event) {
        AndroidExtension.triggerEvent(env, getWrappedObject(), event);
    }
}
