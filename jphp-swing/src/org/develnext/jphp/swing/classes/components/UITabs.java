package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.WrapImage;
import org.develnext.jphp.swing.classes.components.support.UIContainer;
import org.develnext.jphp.swing.classes.components.support.UIElement;
import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.TrueMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UITabs")
public class UITabs extends UIContainer {
    protected JTabbedPane component;

    public UITabs(Environment env, JTabbedPane component) {
        super(env);
        this.component = component;
    }

    public UITabs(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JTabbedPane) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        this.component = new JTabbedPane();
    }

    @Signature
    protected Memory __getSelectedIndex(Environment env, Memory... args) {
        return LongMemory.valueOf(component.getSelectedIndex());
    }

    @Signature(@Arg("value"))
    protected Memory __setSelectedIndex(Environment env, Memory... args) {
        component.setSelectedIndex(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getTabPlacement(Environment env, Memory... args) {
        return SwingExtension.fromDirection(component.getTabPlacement());
    }

    @Signature(@Arg("value"))
    protected Memory __setTabPlacement(Environment env, Memory... args) {
        component.setTabPlacement(SwingExtension.toDirection(args[0]));
        return Memory.NULL;
    }

    @Signature({
            @Arg("title"),
            @Arg(value = "component", typeClass = SwingExtension.NAMESPACE + "UIElement"),
            @Arg(value = "icon", typeClass = SwingExtension.NAMESPACE + "Image", optional = @Optional("NULL"))
    })
    public Memory addTab(Environment env, Memory... args) {
        if (args[2].isNull()) {
            component.addTab(args[0].toString(), args[1].toObject(UIElement.class).getComponent());
        } else {
            component.addTab(
                    args[0].toString(),
                    args[2].toObject(WrapImage.class).getImageIcon(),
                    args[1].toObject(UIElement.class).getComponent()
            );
        }
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory removeTabAt(Environment env, Memory... args) {
        component.removeTabAt(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory getTitleAt(Environment env, Memory... args) {
        return new StringMemory(component.getTitleAt(args[0].toInteger()));
    }

    @Signature({@Arg("index"), @Arg("value")})
    public Memory setTitleAt(Environment env, Memory... args) {
        component.setTitleAt(args[0].toInteger(), args[1].toString());
        return Memory.NULL;
    }


    @Signature(@Arg("index"))
    public Memory getToolTipTextAt(Environment env, Memory... args) {
        return new StringMemory(component.getToolTipTextAt(args[0].toInteger()));
    }

    @Signature({@Arg("index"), @Arg("value")})
    public Memory setToolTipTextAt(Environment env, Memory... args) {
        component.setToolTipTextAt(args[0].toInteger(), args[1].toString());
        return Memory.NULL;
    }

    @Signature
    public Memory removeAll(Environment env, Memory... args) {
        component.removeAll();
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory getTabIconAt(Environment env, Memory... args) {
        Icon icon = component.getIconAt(args[0].toInteger());
        if (icon == null)
            return Memory.NULL;

        return new ObjectMemory(new WrapImage(env, (BufferedImage)((ImageIcon)icon).getImage()));
    }

    @Signature({
            @Arg("index"),
            @Arg(value = "icon", typeClass = SwingExtension.NAMESPACE + "Image", optional = @Optional("NULL"))
    })
    public Memory setTabIconAt(Environment env, Memory... args) {
        if (args[1].isNull())
            component.setIconAt(args[0].toInteger(), null);
        else {
            component.setIconAt(args[0].toInteger(), args[1].toObject(WrapImage.class).getImageIcon());
        }
        return Memory.NULL;
    }

    @Signature(@Arg("index"))
    public Memory getTabComponentAt(Environment env, Memory... args) {
        Component cmp = component.getTabComponentAt(args[0].toInteger());
        if (cmp == null)
            return Memory.NULL;

        return new ObjectMemory(UIElement.of(env, cmp));
    }

    @Signature({
            @Arg("index"),
            @Arg(value = "icon", typeClass = SwingExtension.NAMESPACE + "UIElement", optional = @Optional("NULL"))
    })
    public Memory setTabComponentAt(Environment env, Memory... args) {
        if (args[1].isNull())
            component.setTabComponentAt(args[0].toInteger(), null);
        else
            component.setTabComponentAt(args[0].toInteger(), args[1].toObject(UIElement.class).getComponent());

        return Memory.NULL;
    }

    @Signature
    protected Memory __getSelectedComponent(Environment env, Memory... args) {
        Component cmp = component.getSelectedComponent();
        if (cmp == null)
            return Memory.NULL;

        return new ObjectMemory(UIElement.of(env, cmp));
    }

    @Signature(@Arg(value = "icon", typeClass = SwingExtension.NAMESPACE + "UIElement", optional = @Optional("NULL")))
    public Memory __setSelectedComponent(Environment env, Memory... args) {
        if (args[0].isNull())
            component.setSelectedComponent(null);
        else
            component.setSelectedComponent(args[0].toObject(UIElement.class).getComponent());

        return Memory.NULL;
    }

    @Signature({@Arg("x"), @Arg("y")})
    public Memory indexAtPosition(Environment env, Memory... args) {
        return LongMemory.valueOf(component.indexAtLocation(args[0].toInteger(), args[1].toInteger()));
    }

    @Signature(@Arg("index"))
    public Memory isEnabledAt(Environment env, Memory... args) {
        return TrueMemory.valueOf(component.isEnabledAt(args[0].toInteger()));
    }

    @Signature({@Arg("index"), @Arg("enabled")})
    public Memory setEnabledAt(Environment env, Memory... args) {
        component.setEnabledAt(args[0].toInteger(), args[1].toBoolean());
        return Memory.NULL;
    }
}
