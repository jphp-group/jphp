package php.runtime.ext.swing.classes;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.swing.SwingExtension;
import php.runtime.lang.BaseObject;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import javax.swing.*;

import static php.runtime.annotation.Reflection.*;

@Name(SwingExtension.NAMESPACE + "UIManager")
final public class WrapUIManager extends BaseObject {

    public WrapUIManager(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args){
        return Memory.NULL;
    }

    @Signature(@Arg("lookAndFeel"))
    public static Memory setLookAndFeel(Environment env, Memory... args){
        try {
            UIManager.setLookAndFeel(args[0].toString());
        } catch (UnsupportedLookAndFeelException e) {
            env.exception(env.trace(), "Unsupported lookAndFeel - " + args[0].toString());
        } catch (Exception e){
            env.exception(env.trace(), e.getMessage());
        }
        return Memory.NULL;
    }

    @Signature
    public static Memory getSystemLookAndFeel(Environment env, Memory... args){
        return new StringMemory(UIManager.getSystemLookAndFeelClassName());
    }
}
