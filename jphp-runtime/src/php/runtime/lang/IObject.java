package php.runtime.lang;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.exceptions.CriticalException;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.memory.*;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ClassEntity;

@Reflection.Ignore
public interface IObject {
    ClassEntity getReflection();
    ArrayMemory getProperties();
    Environment getEnvironment();
    int getPointer();

    boolean isMock();
    void setAsMock();

    boolean isFinalized();
    void doFinalize();

    default ArrayMemory getPropertiesForChange() {
        ArrayMemory properties = getProperties();
        properties.checkCopied();
        return properties;
    }

    default boolean hasProp(String name) {
        return getProperties().containsKey(name);
    }

    default Memory getProp(String name) {
        return getProperties().valueOfIndex(name);
    }

    default void setProp(String name, Memory value) {
        ArrayMemory properties = getProperties();
        properties.checkCopied();
        properties.putAsKeyString(name, value.fast_toImmutable());
    }

    default void setProp(String name, String value) {
        ArrayMemory properties = getProperties();
        properties.checkCopied();
        properties.putAsKeyString(name, StringMemory.valueOf(value));
    }

    default void setProp(String name, long value) {
        ArrayMemory properties = getProperties();
        properties.checkCopied();
        properties.putAsKeyString(name, LongMemory.valueOf(value));
    }

    default void setProp(String name, double value) {
        ArrayMemory properties = getProperties();
        properties.checkCopied();
        properties.putAsKeyString(name, DoubleMemory.valueOf(value));
    }

    default void setProp(String name, boolean value) {
        ArrayMemory properties = getProperties();
        properties.checkCopied();
        properties.putAsKeyString(name, TrueMemory.valueOf(value));
    }

    default void removeProp(String name) {
        ArrayMemory properties = getProperties();
        properties.checkCopied();
        properties.removeByScalar(name);
    }

    default Memory callMethod(Environment env, String name, Memory... args) {
        try {
            return env.invokeMethod(this, name, args);
        } catch (Throwable throwable) {
            env.forwardThrow(throwable);
            return Memory.NULL;
        }
    }

    @SuppressWarnings("unchecked")
    default Memory callMethodAny(Environment env, String name, Object... args) {
        if (args != null && args.length > 0) {
            Memory[] passed = new Memory[args.length];

            for (int i = 0; i < passed.length; i++) {
                if (args[i] == null) {
                    passed[i] = Memory.NULL;
                    continue;
                }

                MemoryOperation operation = MemoryOperation.get(
                        args[i].getClass(), args[i].getClass().getGenericSuperclass()
                );

                if (operation == null) {
                    throw new CriticalException("Unsupported bind type - " + args[i].getClass().toString());
                }

                passed[i] = operation.unconvertNoThow(env, env.trace(), args[i]);
            }

            return callMethod(env, name, passed);
        } else {
            return callMethod(env, name);
        }
    }
}
