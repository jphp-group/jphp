package php.runtime.ext.core.classes.lib.legacy;

import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.BinUtils;
import php.runtime.reflection.ClassEntity;

@Name("php\\lib\\Binary")
public class OldBinUtils extends BinUtils {
    public OldBinUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
