package php.runtime.ext.core.classes.lib.legacy;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.ItemsUtils;
import php.runtime.ext.core.classes.lib.NumUtils;
import php.runtime.reflection.ClassEntity;

@Name("php\\lib\\Number")
public class OldNumUtils extends NumUtils {
    public OldNumUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
