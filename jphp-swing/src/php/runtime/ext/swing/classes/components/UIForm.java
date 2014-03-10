package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.XYLayout;
import php.runtime.ext.swing.classes.components.support.UIWindow;
import php.runtime.ext.swing.support.JFrameX;
import php.runtime.reflection.ClassEntity;

import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIForm")
public class UIForm extends UIWindow {
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
}
