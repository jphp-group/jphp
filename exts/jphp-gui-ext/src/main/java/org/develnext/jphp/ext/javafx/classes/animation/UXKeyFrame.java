package org.develnext.jphp.ext.javafx.classes.animation;

import javafx.animation.KeyFrame;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "animation\\UXKeyFrame")
public class UXKeyFrame extends BaseWrapper<KeyFrame> {
    interface WrappedInterface {
        @Property String name();
        @Property Duration time();
    }

    public UXKeyFrame(Environment env, KeyFrame wrappedObject) {
        super(env, wrappedObject);
    }

    public UXKeyFrame(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(final Environment env, long duration, final Invoker invoker) {
        __construct(env, duration, invoker, null);
    }

    @Signature
    public void __construct(final Environment env, long duration, final Invoker invoker, String name) {
        __wrappedObject = new KeyFrame(Duration.millis(duration), name, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    invoker.callAny(event);
                } catch (Throwable throwable) {
                    env.wrapThrow(throwable);
                }
            }
        });
    }
}
