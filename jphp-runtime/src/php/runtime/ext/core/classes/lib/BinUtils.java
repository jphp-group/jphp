package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.BinaryMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ParameterEntity;

import java.io.ByteArrayOutputStream;

import static php.runtime.annotation.Reflection.*;
import static php.runtime.annotation.Runtime.FastMethod;

@Name("php\\lib\\Bin")
public class BinUtils extends BaseObject {
    public BinUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private Memory __construct(Environment env, Memory... args) { return Memory.NULL; }

    @FastMethod
    @Signature(@Arg("value"))
    public static Memory of(Environment env, Memory... args) {
        if (ParameterEntity.checkTypeHinting(env, args[0], HintType.TRAVERSABLE)) {
            ForeachIterator iterator = args[0].getNewIterator(env, false, false);
            ByteArrayOutputStream tmp = new ByteArrayOutputStream();
            while (iterator.next()) {
                tmp.write(iterator.getValue().toInteger());
            }
            return new BinaryMemory(tmp.toByteArray());
        }

        return new BinaryMemory(args[0].getBinaryBytes(env.getDefaultCharset()));
    }
}
