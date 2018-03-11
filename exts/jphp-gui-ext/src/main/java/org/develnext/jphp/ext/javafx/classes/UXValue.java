package org.develnext.jphp.ext.javafx.classes;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.WrapInvoker;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Abstract
@Reflection.Name(JavaFXExtension.NS + "UXValue")
public class UXValue extends BaseWrapper<ObservableValue> {
    public UXValue(Environment env, ObservableValue wrappedObject) {
        super(env, wrappedObject);
    }

    public UXValue(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public Memory getValue(Environment env) {
        return Memory.wrap(env, getWrappedObject().getValue());
    }

    @Signature
    public WrapInvoker addListener(final Environment env, final Invoker invoker) {
        ChangeListener changeListener = (observable, oldValue, newValue) -> {
            try {
                invoker.callAny(oldValue, newValue);
            } catch (Throwable throwable) {
                env.wrapThrow(throwable);
            }
        };

        getWrappedObject().addListener(changeListener);
        invoker.setUserData(changeListener);
        return new WrapInvoker(env, invoker);
    }

    @Signature
    public WrapInvoker addOnceListener(final Environment env, final Invoker invoker) {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    invoker.callAny(oldValue, newValue);
                } catch (Throwable throwable) {
                    env.wrapThrow(throwable);
                } finally {
                    getWrappedObject().removeListener(this);
                }
            }
        };

        getWrappedObject().addListener(changeListener);
        invoker.setUserData(changeListener);
        return new WrapInvoker(env, invoker);
    }

    @Signature
    public boolean removeListener(final Environment env, final WrapInvoker invoker) {
        Object userData = invoker.getInvoker().getUserData();

        if (userData instanceof ChangeListener) {
            getWrappedObject().removeListener((ChangeListener) userData);
            return true;
        }

        return false;
    }
}
