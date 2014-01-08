package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.lang.Resource;
import ru.regenix.jphp.runtime.lang.spl.iterator.Iterator;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryStringUtils;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.PropertyEntity;

public class ObjectMemory extends Memory {

    public IObject value;

    public ObjectMemory() {
        super(Type.OBJECT);
    }

    public ObjectMemory(IObject object){
        this();
        this.value = object;
    }

    public static Memory valueOf(IObject object){
        return new ObjectMemory(object);
    }

    public ClassEntity getReflection(){
        return value.getReflection();
    }

    public ArrayMemory getProperties(){
        return value.getProperties();
    }

    @Override
    public int getPointer(boolean absolute) {
        return value.getPointer();
    }

    @Override
    public int getPointer() {
        return value.getPointer();
    }

    @Override
    public boolean isObject() {
        return true;
    }

    @Override
    public boolean isResource() {
        return value instanceof Resource;
    }

    @Override
    public long toLong() {
        return 0;
    }

    @Override
    public double toDouble() {
        return 0;
    }

    @Override
    public boolean toBoolean() {
        return true;
    }

    @Override
    public Memory toNumeric() {
        return CONST_INT_0;
    }

    @Override
    public String toString() {
        ClassEntity entity = value.getReflection();
        if (entity.methodMagicToString != null){
            Environment env = value.getEnvironment();
            if (env == null)
                return "Object";

            // We can't get real trace info from toString method :(
            env.pushCall(
                    entity.methodMagicToString.getTrace(),
                    value, null, entity.methodMagicToString.getName(), entity.getName(), null
            );
            try {
                Memory result = entity.methodMagicToString.invokeDynamic(value, env, null);
                if (!result.isString())
                    env.error(
                            ErrorType.E_RECOVERABLE_ERROR, "Methods %s must return a string value",
                            entity.methodMagicToString.getSignatureString(false)
                    );
                return result.toString();
            } catch (RuntimeException e) {
                throw e;
            } catch (Throwable e){
                throw new RuntimeException(e);
            } finally {
                env.popCall();
            }
        } else
            return "Object";
    }

    @Override
    public Memory inc() {
        return CONST_INT_1;
    }

    @Override
    public Memory dec() {
        return CONST_INT_M1;
    }

    @Override
    public Memory negative() {
        return CONST_INT_0;
    }

    @Override
    public Memory plus(Memory memory) {
        return toNumeric().plus(memory);
    }

    @Override
    public Memory minus(Memory memory) {
        return toNumeric().minus(memory);
    }

    @Override
    public Memory mul(Memory memory) {
        return toNumeric().mul(memory);
    }

    @Override
    public Memory div(Memory memory) {
        return toNumeric().div(memory);
    }

    @Override
    public boolean equal(Memory memory) {
        return false;
    }

    @Override
    public boolean notEqual(Memory memory) {
        return false;
    }

    @Override
    public boolean smaller(Memory memory) {
        return false;
    }

    @Override
    public boolean smallerEq(Memory memory) {
        return false;
    }

    @Override
    public boolean greater(Memory memory) {
        return false;
    }

    @Override
    public boolean greaterEq(Memory memory) {
        return false;
    }

    @Override
    public byte[] getBinaryBytes() {
        return MemoryStringUtils.getBinaryBytes(this);
    }

    @Override
    public boolean identical(Memory memory) {
        return memory.type == Type.OBJECT && getPointer() == memory.getPointer();
    }

    @Override
    public boolean identical(long value) {
        return false;
    }

    @Override
    public boolean identical(double value) {
        return false;
    }

    @Override
    public boolean identical(boolean value) {
        return false;
    }

    @Override
    public boolean identical(String value) {
        return false;
    }

    @Override
    public ForeachIterator getNewIterator(final Environment env, boolean getReferences, boolean getKeyReferences) {
        if (value instanceof Iterator){
            final Iterator iterator = (Iterator)value;
            final String className = value.getReflection().getName();

            return new ForeachIterator(getReferences, getKeyReferences, false) {
                @Override
                protected boolean init() {
                    env.pushCall(null, ObjectMemory.this.value, null, "rewind", className, null);
                    try {
                        return iterator.rewind(env).toBoolean();
                    } finally {
                        env.popCall();
                    }
                }

                @Override
                protected boolean prevValue() {
                    return false;
                }

                @Override
                protected boolean nextValue() {
                    return true;
                }

                @Override
                public boolean next() {
                    boolean valid = false;

                    env.pushCall(null, ObjectMemory.this.value, null, "valid", className, null);
                    try {
                        valid = iterator.valid(env).toBoolean();
                        if (valid) {
                            env.pushCall(null, ObjectMemory.this.value, null, "key", className, null);
                            try {
                                currentKey = iterator.key(env).toImmutable();

                                env.pushCall(null, ObjectMemory.this.value, null, "current", className, null);
                                try {
                                    currentValue = iterator.current(env);
                                    if (!getReferences)
                                        currentValue = currentValue.toImmutable();
                                } finally {
                                    env.popCall();
                                }
                            } finally {
                                env.popCall();
                            }
                        }
                    } finally {
                        env.popCall();
                    }

                    env.pushCall(null, ObjectMemory.this.value, null, "next", className, null);
                    try {
                        iterator.next(env);
                    } finally {
                        env.popCall();
                    }
                    return valid;
                }

                @Override
                public Object getKey() {
                    return currentKey;
                }

                @Override
                public Memory getMemoryKey() {
                    return (Memory)currentKey;
                }
            };
        }
        return null;
    }

    @Override
    public boolean instanceOf(Environment env, String className, String lowerClassName) {
        ClassEntity origin = value.getReflection();
        ClassEntity what   = env.fetchClass(className, lowerClassName, true);
        if (what == null) {
            /*env.triggerError(new FatalException(
                    Messages.ERR_FATAL_CLASS_NOT_FOUND.fetch(className),
                    trace
            ));*/
            return false;
        }

        return origin.isInstanceOf(what);
    }

    @Override
    public Memory toObject(Environment env) {
        return this;
    }

    @Override
    public Memory clone(Environment env, TraceInfo trace) throws Throwable {
        return new ObjectMemory(value.getReflection().cloneObject(value, env, trace));
    }

    @Override
    public Memory toArray() {
        ArrayMemory result = new ArrayMemory();
        ArrayMemory props = value.getProperties();
        ForeachIterator iterator = props.foreachIterator(false, false);
        ClassEntity reflection = value.getReflection();

        while (iterator.next()){
            Object key = iterator.getKey();
            Memory value = iterator.getValue().toImmutable();

            if (key instanceof String){
                String keyS = (String)key;
                PropertyEntity prop = reflection.properties.get(keyS);

                if (prop == null || prop.getModifier() == Modifier.PUBLIC){
                    result.refOfIndex(keyS).assign(iterator.getValue().toImmutable());
                } else {
                    if (prop.getModifier() == Modifier.PROTECTED){
                        result.refOfIndex("\0*\0" + keyS).assign(value);
                    } else {
                        result.refOfIndex("\0" + prop.getClazz().getName() + "\0" + keyS).assign(value);
                    }
                }
            } else {
                result.refOfIndex(iterator.getMemoryKey()).assign(value);
            }
        }

        return result.toConstant();
    }
}
