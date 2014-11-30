package org.develnext.jphp.android.ext;

import android.view.View;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;

abstract public class EventProvider {
    abstract public String getCode();
    abstract public void bind(Environment env, View view, Invoker invoker);
    abstract public void unbind(Environment env, View view);
    abstract public void trigger(Environment env, View view);
}
