package org.develnext.jphp.ext.image.classes;

import org.develnext.jphp.ext.image.GraphicExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Namespace;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

@Name("Color")
@Namespace(GraphicExtension.NS)
public class PColor extends BaseObject {
    private Color color;

    public PColor(Environment env, Color color) {
        super(env);
        this.color = color;
    }

    public PColor(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Color getColor() {
        return color;
    }

    @Signature
    public void __construct(Memory value) {
        color = Color.decode(value.toString());
    }


}
