package php.runtime.ext.swing.classes;

import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.reflection.ClassEntity;

import javax.swing.border.Border;

import static php.runtime.annotation.Reflection.Name;

@Name(SwingExtension.NAMESPACE + "Border")
public class WrapBorder extends RootObject {
    protected Border border;

    public WrapBorder(Environment env, Border border) {
        super(env);
        this.border = border;
    }

    public WrapBorder(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Border getBorder() {
        return border;
    }
}
