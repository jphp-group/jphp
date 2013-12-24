package ru.regenix.jphp.runtime.memory;

import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.lang.ForeachIterator;
import ru.regenix.jphp.runtime.lang.IObject;
import ru.regenix.jphp.runtime.lang.Resource;
import ru.regenix.jphp.runtime.lang.spl.iterator.Iterator;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.memory.support.MemoryStringUtils;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

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
    public Memory mod(Memory memory) {
        return toNumeric().mod(memory);
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
                    env.pushCall(null, ObjectMemory.this.value, null, "rewind", className);
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

                    env.pushCall(null, ObjectMemory.this.value, null, "valid", className);
                    try {
                        valid = iterator.valid(env).toBoolean();
                        if (valid) {
                            env.pushCall(null, ObjectMemory.this.value, null, "key", className);
                            try {
                                currentKey = iterator.key(env).toImmutable();

                                env.pushCall(null, ObjectMemory.this.value, null, "current", className);
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

                    env.pushCall(null, ObjectMemory.this.value, null, "next", className);
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
}
