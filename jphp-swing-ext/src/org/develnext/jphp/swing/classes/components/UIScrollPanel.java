package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.ComponentProperties;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.support.JScrollPanel;
import org.develnext.jphp.swing.support.JScrollPanelX;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIScrollPanel")
public class UIScrollPanel extends UIContainer {
    protected JScrollPanelX component;

    public UIScrollPanel(Environment env, JScrollPanelX component) {
        super(env);
        this.component = component;
    }

    public UIScrollPanel(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JScrollPanelX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JScrollPanelX();
        SwingExtension.registerComponent(component.getContent());
    }

    @Override
    protected Container getComponentsContainer() {
        return component.getContent();
    }

    @Signature
    protected Memory __getHorScrollPolicy(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getHorScrollPolicy().name());
    }

    @Signature(@Arg("value"))
    protected Memory __setHorScrollPolicy(Environment env, Memory... args) {
        component.setHorScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(args[0].toString().toUpperCase()));
        return Memory.NULL;
    }

    @Signature
    protected Memory __getVerScrollPolicy(Environment env, Memory... args) {
        return StringMemory.valueOf(component.getVerScrollPolicy().name());
    }

    @Signature(@Arg("value"))
    protected Memory __setVerScrollPolicy(Environment env, Memory... args) {
        component.setVerScrollPolicy(JScrollPanel.ScrollPolicy.valueOf(args[0].toString().toUpperCase()));
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory __setAutosize(Environment env, Memory... args){
        ComponentProperties properties = SwingExtension.getProperties(component.getContent());
        properties.setAutoSize(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getAutosize(Environment env, Memory... args){
        ComponentProperties properties = SwingExtension.getProperties(component.getContent());
        return properties.isAutoSize() ? Memory.TRUE : Memory.FALSE;
    }
}
