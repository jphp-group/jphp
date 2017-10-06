package php.runtime.reflection.helper;

import php.runtime.Memory;
import php.runtime.env.Context;
import php.runtime.env.Environment;
import php.runtime.lang.Closure;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ParameterEntity;

import java.lang.reflect.InvocationTargetException;

public class ClosureEntity extends ClassEntity {
    protected boolean returnReference;
    public ParameterEntity[] parameters;
    public ParameterEntity[] uses;

    protected ObjectMemory singleton;
    protected GeneratorEntity generatorEntity;

    public ClosureEntity(Context context) {
        super(context);
        setName(Closure.class.getSimpleName());
        setType(Type.CLOSURE);
    }

    public boolean isReturnReference() {
        return returnReference;
    }

    @Override
    public boolean isHiddenInCallStack() {
        return true;
    }

    public void setReturnReference(boolean returnReference) {
        this.returnReference = returnReference;
    }

    public void setParameters(ParameterEntity[] parameters) {
        this.parameters = parameters;
    }

    public void setUses(ParameterEntity[] uses) {
        this.uses = uses;
    }

    public ObjectMemory getSingleton() {
        return singleton;
    }

    @Override
    public String getName() {
        return parent.getName();
    }

    @Override
    public String getLowerName() {
        return parent == null ? super.getLowerName() : parent.getLowerName();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ClosureEntity;
    }

    @Override
    public void setNativeClazz(Class<?> nativeClazz) {
        this.nativeClazz = nativeClazz;
        if (!nativeClazz.isInterface()){
            try {
                this.nativeConstructor = nativeClazz.getConstructor(Environment.class, ClassEntity.class, Memory.class, String.class, Memory[].class);
                this.nativeConstructor.setAccessible(true);

                //if (uses == null || uses.length == 0)
                singleton = new ObjectMemory((Closure) this.nativeConstructor.newInstance(null, this, Memory.NULL, null, null));
                //else
                  //  singleton = null;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getTargetException());
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public GeneratorEntity getGeneratorEntity() {
        return generatorEntity;
    }

    public void setGeneratorEntity(GeneratorEntity generatorEntity) {
        this.generatorEntity = generatorEntity;
    }
}
