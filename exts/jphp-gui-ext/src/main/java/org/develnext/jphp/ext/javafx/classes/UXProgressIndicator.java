package org.develnext.jphp.ext.javafx.classes;

import javafx.scene.control.ProgressIndicator;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.reflection.ClassEntity;

@Name(JavaFXExtension.NS + "UXProgressIndicator")
public class UXProgressIndicator extends UXControl {
    interface WrappedInterface {
        @Property("progressK") double progress();
        @Property boolean indeterminate();
    }

    public UXProgressIndicator(Environment env, ProgressIndicator wrappedObject) {
        super(env, wrappedObject);
    }

    public UXProgressIndicator(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public ProgressIndicator getWrappedObject() {
        return (ProgressIndicator) super.getWrappedObject();
    }

    @Signature
    public void __construct() {
        __wrappedObject = new ProgressIndicator();
    }

    @Signature
    public void __construct(double progress) {
        __wrappedObject = new ProgressIndicator(progress);
    }

    @Getter("progress")
    public int getProgressPercent() {
        int round = (int) (Math.round(getWrappedObject().getProgress() * 100));
        return round > 100 ? 100 : round;
    }

    @Setter("progress")
    public void setProgressPercent(int value) {
        getWrappedObject().setProgress(value / 100.0);
    }
}
