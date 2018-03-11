package org.develnext.jphp.ext.systemtray.classes;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import org.develnext.jphp.ext.javafx.support.EventProvider;
import org.develnext.jphp.ext.systemtray.SystemTrayExtension;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.env.Environment;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

@Name("TrayIcon")
@Reflection.Namespace(SystemTrayExtension.NS)
public class WrapTrayIcon extends BaseWrapper<TrayIcon> {
    interface WrappedInterface {
        @Property boolean imageAutoSize();
    }

    protected Image image;

    public WrapTrayIcon(Environment env, TrayIcon wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapTrayIcon(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(javafx.scene.image.Image image) {
        __wrappedObject = new TrayIcon(SwingFXUtils.fromFXImage(this.image = image, null));
    }

    @Getter
    public Image getImage() {
        return image;
    }

    @Setter
    public void setImage(Image image) {
        this.image = image;
        getWrappedObject().setImage(SwingFXUtils.fromFXImage(image, null));
    }

    @Getter
    public String getTooltip() {
        return getWrappedObject().getToolTip();
    }

    @Setter
    public void setTooltip(String tooltip) {
        getWrappedObject().setToolTip(tooltip);
    }

    @Signature
    public void displayMessage(String title, String text) {
        displayMessage(title, text, TrayIcon.MessageType.NONE);
    }

    @Signature
    public void displayMessage(String title, String text, TrayIcon.MessageType type) {
        getWrappedObject().displayMessage(title, text, type);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void on(String event, Invoker invoker, String group, Environment env) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.on(target, event, group, invoker);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void on(String event, Invoker invoker, Environment env) {
        on(event, invoker, "general", env);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void off(Environment env, String event, @Nullable String group) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.off(target, event, group);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }

    @Signature
    public void off(Environment env, String event) {
        off(env, event, null);
    }

    @Signature
    public void trigger(Environment env, String event, @Nullable javafx.event.Event e) {
        Object target = getWrappedObject();
        EventProvider eventProvider = EventProvider.get(target, event);

        if (eventProvider != null) {
            eventProvider.trigger(target, event, e);
        } else {
            throw new IllegalArgumentException("Unable to find the '"+event+"' event type");
        }
    }
}
