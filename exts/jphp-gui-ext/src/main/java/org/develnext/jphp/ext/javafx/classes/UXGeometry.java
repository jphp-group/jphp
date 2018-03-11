package org.develnext.jphp.ext.javafx.classes;

import javafx.geometry.Point2D;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

@Abstract
@Reflection.Name(JavaFXExtension.NS + "UXGeometry")
public class UXGeometry extends BaseObject {
    public UXGeometry(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public static double distance(double x1, double y1, double x2, double y2) {
        return new Point2D(x1, y1).distance(x2, y2);
    }
}
