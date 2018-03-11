package org.develnext.jphp.ext.javafx.classes.animation;

import javafx.animation.PathTransition;
import javafx.scene.Node;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Reflection.Name(JavaFXExtension.NS + "animation\\UXPathAnimation")
public class UXPathAnimation extends UXAnimation<PathTransition> {
    interface WrappedInterface {
        @Property
        PathTransition.OrientationType orientation();

        @Property
        Duration duration();

        @Property
        Node node();
    }

    public UXPathAnimation(Environment env, PathTransition wrappedObject) {
        super(env, wrappedObject);
    }

    public UXPathAnimation(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Duration duration, @Reflection.Nullable Node node) {
        __wrappedObject = new PathTransition(duration, new Path(), node);
    }

    @Signature
    public void __construct(Duration duration) {
        __wrappedObject = new PathTransition(duration, new Path());
    }

    @Signature
    public void __construct() {
        __wrappedObject = new PathTransition();
    }

    protected Path path() {
        Shape path = getWrappedObject().getPath();

        if (path == null) {
            path = new Path();
            getWrappedObject().setPath(path);
        }

        return (Path) path;
    }

    @Signature
    public void clearPath() {
        getWrappedObject().setPath(null);
    }

    @Signature
    public UXPathAnimation addMoveTo(double x, double y) {
        path().getElements().add(new MoveTo(x, y));
        return this;
    }

    @Signature
    public UXPathAnimation addLineTo(double x, double y) {
        path().getElements().add(new LineTo(x, y));
        return this;
    }
}
