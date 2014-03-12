package php.runtime.ext.swing.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.ext.swing.classes.components.support.RootObject;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import static php.runtime.annotation.Reflection.*;

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

    @Signature({@Arg("top"), @Arg("left"), @Arg("bottom"), @Arg("right")})
    public static Memory createEmpty(Environment env, Memory... args) {
        return new ObjectMemory(new WrapBorder(env, new EmptyBorder(
            args[0].toInteger(), args[1].toInteger(),
            args[2].toInteger(), args[3].toInteger()
        )));
    }
}
