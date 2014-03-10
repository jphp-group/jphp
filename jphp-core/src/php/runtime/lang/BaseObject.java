package php.runtime.lang;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

import java.lang.ref.WeakReference;

@Reflection.Ignore
abstract public class BaseObject implements IObject {
    protected final ArrayMemory __dynamicProperties__;
    protected ClassEntity __class__;
    protected final WeakReference<Environment> __env__;

    private boolean isFinalized;

    public BaseObject(Environment env){
        this(env, null);
        __class__ = env.fetchClass(getClass());
    }

    protected BaseObject(ClassEntity entity) {
        this.__class__ = entity;
        this.__dynamicProperties__ = null;
        this.__env__ = new WeakReference<Environment>(null);
    }

    protected BaseObject(ArrayMemory __dynamicProperties__, Environment __env__, ClassEntity __class__) {
        this.__dynamicProperties__ = __dynamicProperties__;
        this.__class__ = __class__;
        this.__env__ = new WeakReference<Environment>(__env__);
    }

    public BaseObject(Environment env, ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory(true);
        this.__env__ = new WeakReference<Environment>(env);
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
        return __dynamicProperties__;
    }

    @Override
    public Environment getEnvironment() {
        return __env__.get();
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
}
