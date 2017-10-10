package php.runtime.lang;

import php.runtime.annotation.Reflection.BaseType;
import php.runtime.annotation.Reflection.Ignore;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.lang.ref.WeakReference;

@Ignore
@BaseType
abstract public class BaseObject implements IObject {
    protected ArrayMemory __dynamicProperties__;
    protected ClassEntity __class__;
    protected final Environment __env__;

    private boolean isFinalized;

    public BaseObject(Environment env){
        this(env, null);
        __class__ = env.fetchClass(getClass());
    }

    protected BaseObject(ClassEntity entity) {
        this.__class__ = entity;
        this.__dynamicProperties__ = null;
        this.__env__ = null;
    }

    public BaseObject(Environment env, ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory(true);
        this.__env__ = env;
    }

    @Override
    final public int getPointer(){
        return super.hashCode();
    }

    @Override
    public ClassEntity getReflection() {
        return __class__;
    }

    @Override
    public ArrayMemory getProperties() {
        if (__dynamicProperties__ == null) {
            synchronized (this) {
                if (__dynamicProperties__ != null) return __dynamicProperties__;

                if (__dynamicProperties__ == null) {
                    __dynamicProperties__ = new ArrayMemory(true);
                }
            }
        }

        return __dynamicProperties__;
    }

    @Override
    public Environment getEnvironment() {
        return __env__;
    }

    @Override
    public boolean isFinalized() {
        return isFinalized;
    }

    @Override
    public void doFinalize() {
        isFinalized = true;
    }

    @Override
    public boolean isMock() {
        return __class__ == null;
    }

    @Override
    public void setAsMock() {
        __class__ = null;
    }

    @Override
    public String toString() {
        if (__class__.methodMagicToString != null) {
            Environment environment = getEnvironment();

            if (environment != null) {
                return environment.invokeMethodNoThrow(this, "__toString").toString();
            }
        }

        return super.toString();
    }
}
