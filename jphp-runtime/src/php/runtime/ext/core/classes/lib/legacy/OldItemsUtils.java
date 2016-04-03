package php.runtime.ext.core.classes.lib.legacy;

import php.runtime.annotation.Reflection.Name;
import php.runtime.env.Environment;
import php.runtime.ext.core.classes.lib.BinUtils;
import php.runtime.ext.core.classes.lib.ItemsUtils;
import php.runtime.reflection.ClassEntity;

@Name("php\\lib\\Items")
public class OldItemsUtils extends ItemsUtils {
    public OldItemsUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }
}
