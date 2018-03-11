package org.develnext.jphp.ext.javafx.classes.animation;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Nullable;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;


@Reflection.Name(JavaFXExtension.NS + "animation\\UXFadeAnimation")
public class UXFadeAnimation extends UXAnimation<FadeTransition> {
    interface WrappedInterface {
        @Property
        Duration duration();

        @Property
        double byValue();

        @Property
        double fromValue();

        @Property
        double toValue();

        @Property
        Node node();
    }

    public UXFadeAnimation(Environment env, FadeTransition wrappedObject) {
        super(env, wrappedObject);
    }

    public UXFadeAnimation(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Duration duration, @Nullable Node node) {
        __wrappedObject = new FadeTransition(duration, node);
    }

    @Signature
    public void __construct(Duration duration) {
        __wrappedObject = new FadeTransition(duration);
    }

    @Signature
    public void __construct() {
        __wrappedObject = new FadeTransition();
    }
}
