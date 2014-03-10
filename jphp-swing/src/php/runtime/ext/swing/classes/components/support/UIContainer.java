package php.runtime.ext.swing.classes.components.support;


import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.ComponentProperties;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIContainer")
abstract public class UIContainer extends UIElement {

    public UIContainer(Environment env) {
        super(env);
    }

    public UIContainer(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    abstract public Container getContainer();

    @Override
    public Component getComponent() {
        return getContainer();
    }

    @Signature(
            @Arg(value = "component", typeClass = SwingExtension.NAMESPACE + "UIElement")
    )
    public Memory remove(Environment env, Memory... args) {
        UIElement element = unwrap(args[0]);
        getContainer().remove(element.getComponent());
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory removeByIndex(Environment env, Memory... args) {
        getContainer().remove(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    public Memory removeAll(Environment env, Memory... args) {
        getContainer().removeAll();
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory getComponent(Environment env, Memory... args) {
        return new ObjectMemory(UIElement.of(
                env, getContainer().getComponent(args[0].toInteger())
        ));
    }

    @Signature
    public Memory getComponentCount(Environment env, Memory... args) {
        return LongMemory.valueOf(getContainer().getComponentCount());
    }

    @Signature({
            @Arg(value = "component", typeClass = SwingExtension.NAMESPACE + "UIElement"),
            @Arg(value = "index", optional = @Optional("NULL"))
    })
    public Memory add(Environment env, Memory... args){
        UIElement element = unwrap(args[0]);

        if (args[1].isNull())
            getContainer().add(element.getComponent());
        else {
            getContainer().add(element.getComponent(), args[1].toInteger());
        }

        return args[0];
    }

    protected Component getComponent(Container where, String group){
        if (where instanceof JFrame)
            where = ((JFrame) where).getRootPane();

        int count = where.getComponentCount();
        for(int i = 0; i < count; i++){
            Component el = where.getComponent(i);
            ComponentProperties properties = SwingExtension.getProperties(el);

            if (properties != null && properties.hasGroup(group)){
                return el;
            }
            if (el instanceof Container){
                Component r = getComponent((Container)el, group);
                if (r != null)
                    return r;
            }
        }
        return null;
    }

    protected java.util.List<Component> getComponents(Container where, String group){
        java.util.List<Component> result = new ArrayList<Component>();

        if (where instanceof JFrame)
            where = ((JFrame) where).getRootPane();

        int count = where.getComponentCount();
        for(int i = 0; i < count; i++){
            Component el = where.getComponent(i);
            ComponentProperties properties = SwingExtension.getProperties(el);

            if (properties != null && properties.hasGroup(group)){
                result.add(el);
            }

            if (el instanceof Container){
                result.addAll(getComponents((Container)el, group));
            }
        }
        return result;
    }

    @Signature(@Arg("group"))
    public Memory getComponentByGroup(Environment env, Memory... args){
        Component component = null;
        String name = args[0].toString();

        Container container = getContainer();
        component = getComponent(container, name);

        if (component == null)
            return Memory.NULL;

        return new ObjectMemory(UIElement.of(env, component));
    }

    @Signature(@Arg("group"))
    public Memory getComponentsByGroup(Environment env, Memory... args){
        java.util.List<Component> components = getComponents(getContainer(), args[0].toString());
        ArrayMemory result = new ArrayMemory();
        for(Component el : components)
            result.add(new ObjectMemory(UIElement.of(env, el)));

        return result.toConstant();
    }
}
