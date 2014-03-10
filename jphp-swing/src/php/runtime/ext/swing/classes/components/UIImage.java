package php.runtime.ext.swing.classes.components;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.WrapImage;
import php.runtime.ext.swing.classes.components.support.UIContainer;
import php.runtime.ext.swing.support.JImageX;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;
import java.awt.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIImage")
public class UIImage extends UIContainer {
    protected JImageX component;

    public UIImage(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Override
    public Container getContainer() {
        return component;
    }

    @Override
    public Component getComponent() {
        return component;
    }

    @Override
    public void setComponent(Component component) {
        this.component = (JImageX) component;
    }

    @Override
    protected void onInit(Environment env, Memory... args) {
        component = new JImageX();
    }

    @Signature(@Arg("value"))
    protected Memory __setStretch(Environment env, Memory... args) {
        component.setStretch(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getStretch(Environment env, Memory... args) {
        return component.isStretch() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setCentered(Environment env, Memory... args) {
        component.setCentred(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getCentered(Environment env, Memory... args) {
        return component.isCentred() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setProportional(Environment env, Memory... args) {
        component.setProportional(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getProportional(Environment env, Memory... args) {
        return component.isProportional() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setSmooth(Environment env, Memory... args) {
        component.setSmooth(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getSmooth(Environment env, Memory... args) {
        return component.isSmooth() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("value"))
    protected Memory __setMosaic(Environment env, Memory... args) {
        component.setMosaic(args[0].toBoolean());
        return Memory.NULL;
    }

    @Signature
    protected Memory __getMosaic(Environment env, Memory... args) {
        return component.isMosaic() ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "image", typeClass = SwingExtension.NAMESPACE + "Image", optional = @Optional("NULL")))
    public Memory setImage(Environment env, Memory... args) {
        if (args[0].isNull())
            component.setImage(null);
        else
            component.setImage(new ImageIcon(args[0].toObject(WrapImage.class).getImage()));

        return Memory.NULL;
    }
}
