package org.develnext.jphp.ext.javafx.classes.animation;

import javafx.animation.AnimationTimer;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "animation\\UXAnimationTimer")
public class UXAnimationTimer extends BaseWrapper<AnimationTimer> {
    public static final float FRAME_INTERVAL = 1 / 60f;
    public static final float FRAME_INTERVAL_MS = FRAME_INTERVAL * 1000;

    interface WrappedInterface {
        void start();
        void stop();
    }

    protected Invoker handler;

    class AnimationTimerImpl extends AnimationTimer {
        @Override
        public void handle(long now) {
            if (handler.callAny(now).toBoolean()) {
                stop();
            }
        }
    }

    public UXAnimationTimer(Environment env, AnimationTimer wrappedObject) {
        super(env, wrappedObject);
    }

    public UXAnimationTimer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Invoker invoker) {
        handler = invoker;
        __wrappedObject = new AnimationTimerImpl();
    }
}
