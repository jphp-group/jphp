package org.develnext.jphp.swing.classes.components.support;

import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.classes.components.UIDialog;
import org.develnext.jphp.swing.classes.components.UIForm;
import org.develnext.jphp.swing.support.JDialogX;
import org.develnext.jphp.swing.support.JFrameX;
import org.develnext.jphp.swing.support.RootWindow;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIWindow")
abstract public class UIWindow extends UIContainer {
    public UIWindow(Environment env) {
        super(env);
    }

    protected UIWindow(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    abstract public Window getWindow();

    public RootWindow getRootWindow() {
        return (RootWindow) getWindow();
    }

    @Override
    public Container getContainer() {
        return getWindow();
    }

    @Override
    public Component getComponent() {
        return getWindow();
    }

    @Signature(@Arg("value"))
    protected Memory __setUndecorated(Environment env, Memory... args){
        getRootWindow().setUndecorated(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getUndecorated(Environment env, Memory... args){
        return getRootWindow().isUndecorated() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("title"))
    protected Memory __setTitle(Environment env, Memory... args){
        getRootWindow().setTitle(args[0].toString());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getTitle(Environment env, Memory... args){
        return new StringMemory(getRootWindow().getTitle());
    }

    @Signature(@Arg("value"))
    protected Memory __setAlwaysOnTop(Environment env, Memory... args){
        getWindow().setAlwaysOnTop(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getAlwaysOnTop(Environment env, Memory... args){
        return getWindow().isAlwaysOnTop() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setOpacity(Environment env, Memory... args) {
        Window window = getWindow();
        try {
            Method m = window.getClass().getMethod("setOpacity", Float.TYPE);
            m.invoke(window, args[0].toFloat());
        } catch (NoSuchMethodException e) {
            return Memory.NULL;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    protected Memory __setResizable(Environment env, Memory... args){
        getRootWindow().setResizable(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getResizable(Environment env, Memory... args){
        return getRootWindow().isResizable() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    protected Memory __getOpacity(Environment env, Memory... args) {
        Window window = getWindow();
        try {
            Method m = window.getClass().getMethod("getOpacity");
            return new DoubleMemory((Float)m.invoke(window));
        } catch (NoSuchMethodException e) {
            return new DoubleMemory(1);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Signature
    public Memory isActive(Environment env, Memory... args){
        return getWindow().isActive() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory isAlwaysOnTopSupported(Environment env, Memory... args){
        return getWindow().isAlwaysOnTopSupported() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    public Memory moveToCenter(Environment env, Memory... args) {
        getRootWindow().moveToCenter();
        return Memory.NULL;
    }

    public static UIWindow of(Environment env, Window window){
        if (window instanceof JFrameX)
            return new UIForm(env, (JFrameX)window);
        else if (window instanceof JDialogX)
            return new UIDialog(env, (JDialogX)window);
        else
            throw new IllegalArgumentException("Unsupported window class: " + window.getClass().getName());
    }
}
