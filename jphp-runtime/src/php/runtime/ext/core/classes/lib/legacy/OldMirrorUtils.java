package php.runtime.ext.core.classes.lib.legacy;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.MirrorUtils;
import php.runtime.ext.core.classes.lib.NumUtils;
import php.runtime.reflection.ClassEntity;

@Name("php\\lib\\Mirror")
public class OldMirrorUtils extends MirrorUtils {
    public OldMirrorUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
