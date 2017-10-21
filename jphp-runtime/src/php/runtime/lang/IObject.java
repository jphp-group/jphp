package php.runtime.lang;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.memory.*;
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

    default boolean hasProp(String name) {
        return getProperties().containsKey(name);
    }

    default Memory getProp(String name) {
        return getProperties().valueOfIndex(name);
    }

    default void setProp(String name, Memory value) {
        getProperties().putAsKeyString(name, value.toImmutable());
    }

    default void setProp(String name, String value) {
        getProperties().putAsKeyString(name, StringMemory.valueOf(value));
    }

    default void setProp(String name, long value) {
        getProperties().putAsKeyString(name, LongMemory.valueOf(value));
    }

    default void setProp(String name, double value) {
        getProperties().putAsKeyString(name, DoubleMemory.valueOf(value));
    }

    default void setProp(String name, boolean value) {
        getProperties().putAsKeyString(name, TrueMemory.valueOf(value));
    }

    default void removeProp(String name) {
        getProperties().removeByScalar(name);
    }
}
