package org.develnext.jphp.swing.classes.components;

import org.develnext.jphp.swing.classes.WrapImage;
import php.runtime.Memory;
import php.runtime.env.Environment;
import org.develnext.jphp.swing.SwingExtension;
import org.develnext.jphp.swing.XYLayout;
import org.develnext.jphp.swing.classes.components.support.UIWindow;
import org.develnext.jphp.swing.support.JFrameX;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIForm")
public class UIForm extends UIWindow {
    // UIForm.setDefaultCloseOperation(define)
    public final static int DO_NOTHING_ON_CLOSE = JFrameX.DO_NOTHING_ON_CLOSE;
    public final static int HIDE_ON_CLOSE       = JFrameX.HIDE_ON_CLOSE;
    public final static int DISPOSE_ON_CLOSE    = JFrameX.DISPOSE_ON_CLOSE;
    public final static int EXIT_ON_CLOSE       = JFrameX.EXIT_ON_CLOSE;


    protected JFrameX frame;

    public UIForm(Environment env, JFrameX frame) {
        super(env);
        this.frame = frame;
    }

    public UIForm(final Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        frame = new JFrameX();
        frame.setLayout(new XYLayout());
    }

    @Override
    public Window getWindow() {
        return frame;
    }

    @Override
    public Component getContentComponent() {
        return frame.getContentPane();
    }

    @Override
    public void setComponent(Component component) {
        frame = (JFrameX)component;
    }

    @Signature(@Arg("action"))
    public Memory setDefaultCloseOperation(Environment env, Memory... args){
        frame.setDefaultCloseOperation(args[0].toInteger());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMaximized(Environment env, Memory... args) {
        return (frame.getExtendedState() & Frame.MAXIMIZED_BOTH) == Frame.MAXIMIZED_BOTH
                ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    public Memory __setMaximized(Environment env, Memory... args) {
        if (args[0].toBoolean()) {
            frame.setExtendedState(frame.getExtendedState() | Frame.MAXIMIZED_BOTH);
        } else {
            frame.setExtendedState(Frame.NORMAL);
        }

        return Memory.NULL;
    }


    @Signature(@Arg(value = "image", typeClass = SwingExtension.NAMESPACE + "Image", nullable = true))
    public Memory setIconImage(Environment env, Memory... args) {
        this.frame.setIconImage(
            args[0].toBoolean()
                ? args[0].toObject(WrapImage.class).getImage()
                : null
        );

        return Memory.NULL;
    }
}
