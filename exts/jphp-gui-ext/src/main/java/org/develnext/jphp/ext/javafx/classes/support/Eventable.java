package org.develnext.jphp.ext.javafx.classes.support;

import javafx.event.Event;
import org.develnext.jphp.ext.javafx.support.JavaFxUtils;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.invoke.Invoker;

public interface Eventable {
    void on(String event, Invoker invoker, String group);
    void on(String event, Invoker invoker);
    void off(String event, @Nullable String group);
    void off(String event);
    void trigger(String event, @Nullable Event e);
}
