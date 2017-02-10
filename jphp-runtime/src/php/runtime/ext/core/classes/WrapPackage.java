package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.Package;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Name("php\\lang\\Package")
public class WrapPackage extends BaseObject {
    private Package pkg;

    public WrapPackage(Environment env, Package pkg) {
        super(env);
        this.pkg = pkg;
    }

    public WrapPackage(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Package getPackage() {
        return pkg;
    }

    @Signature
    public Memory __construct(Environment env, Memory... args) {
        pkg = new Package();
        return Memory.NULL;
    }

    @Signature(@Arg("name"))
    public Memory hasClass(Environment env, Memory... args) {
        return pkg.hasClass(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("name"))
    public Memory hasFunction(Environment env, Memory... args) {
        return pkg.hasFunction(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("name"))
    public Memory hasConstant(Environment env, Memory... args) {
        return pkg.hasConstant(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg("name"))
    public Memory addClass(Environment env, Memory... args) {
        return pkg.addClass(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "names", type = HintType.ARRAY))
    public Memory addClasses(Environment env, Memory... args) {
        for (Memory one : args[0].toValue(ArrayMemory.class)) {
            pkg.addClass(one.toString());
        }

        return Memory.NULL;
    }

    @Signature(@Arg("name"))
    public Memory addFunction(Environment env, Memory... args) {
        return pkg.addFunction(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }

    @Signature(@Arg(value = "names", type = HintType.ARRAY))
    public Memory addFunctions(Environment env, Memory... args) {
        for (Memory one : args[0].toValue(ArrayMemory.class)) {
            pkg.addFunction(one.toString());
        }

        return Memory.NULL;
    }

    @Signature(@Arg("name"))
    public Memory addConstant(Environment env, Memory... args) {
        return pkg.addConstant(args[0].toString()) ? Memory.TRUE : Memory.FALSE;
    }


    @Signature(@Arg(value = "names", type = HintType.ARRAY))
    public Memory addConstants(Environment env, Memory... args) {
        for (Memory one : args[0].toValue(ArrayMemory.class)) {
            pkg.addConstant(one.toString());
        }

        return Memory.NULL;
    }
}
