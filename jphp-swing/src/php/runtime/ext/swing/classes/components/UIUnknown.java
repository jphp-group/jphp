package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.UIContainer;
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
}
