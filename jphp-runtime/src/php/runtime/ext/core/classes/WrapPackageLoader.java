package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.*;
import php.runtime.env.Package;
import php.runtime.ext.CoreExtension;
import php.runtime.invoke.DynamicMethodInvoker;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ModuleEntity;

@Name("php\\lang\\PackageLoader")
abstract public class WrapPackageLoader extends BaseObject {
    protected PackageLoader loader;

    public WrapPackageLoader(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct() { }

    @Signature({@Arg("name")})
    abstract public Memory load(Environment env, Memory... args) throws Throwable;

    @Signature
    public void register(Environment env) {
        if (loader == null) {
            env.getPackageManager().registerLoader(_getPackageLoader(env));
        } else {
            env.exception("PackageLoader is already registered");
        }
    }

    @Signature
    public void unregister(Environment env) {
        if (loader == null) {
            env.exception("PackageLoader is not registered");
        }

        env.getPackageManager().unregisterLoader(loader);
        loader = null;
    }

    synchronized protected PackageLoader _getPackageLoader(final Environment env) {
        if (loader == null) {
            final Invoker invoker = DynamicMethodInvoker.valueOf(env, env.trace(), this, "load");

            loader = new PackageLoader() {
                @Override
                public Package load(String name, TraceInfo trace) {
                    if (invoker == null) {
                        env.error(trace, "Cannot call load() method of packageLoader");
                        return null;
                    }

                    //invoker.setTrace(trace);
                    //invoker.setPushCallTrace(true);
                    Memory ret = invoker.callNoThrow(StringMemory.valueOf(name));

                    if (ret.isNull()) {
                        return null;
                    }

                    if (ret.instanceOf(WrapPackage.class)) {
                        return ret.toObject(WrapPackage.class).getPackage();
                    }

                    env.error(trace, getReflection().getName() + "::load() method must return an instance of php\\lang\\Package");
                    return null;
                }
            };
        }

        return loader;
    }
}
