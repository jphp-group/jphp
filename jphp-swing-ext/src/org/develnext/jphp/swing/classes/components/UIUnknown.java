package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "UIUnknown")
public class UIUnknown extends UIContainer {
    protected Component component;

    public UIUnknown(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return (Container) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        env.exception(env.trace(), "Cannot create an instance of php\\swing\\UIUnknown");
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = component;
        if (component.getName() == null || component.getName().isEmpty())
            SwingExtension.registerComponent(component);
    }


    @Reflection.Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = super.__debugInfo(env).toValue(ArrayMemory.class);
        r.refOfIndex("*java_class").assign(component.getClass().getName());

        return r;
    }

}
