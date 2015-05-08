package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.ObservableList;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.ArrayAccess;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Abstract
@Reflection.Name(JavaFXExtension.NAMESPACE + "UXList")
public class UXList<T> extends BaseWrapper<ObservableList<T>> implements Iterator, Countable, ArrayAccess {
    private int index = -1;

    public UXList(Environment env, ObservableList<T> wrappedObject) {
        super(env, wrappedObject);
    }

    public UXList(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    @SuppressWarnings("unchecked")
    public boolean add(Environment env, Memory object) {
        return getWrappedObject().add((T) Memory.unwrap(env, object));
    }

    @Signature
    @SuppressWarnings("unchecked")
    public boolean remove(Environment env, Memory object) {
        return getWrappedObject().remove((T) Memory.unwrap(env, object));
    }

    @Signature
    public void clear() {
        getWrappedObject().clear();
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        if (index == -1) {
            return Memory.NULL;
        }

        return Memory.wrap(env, getWrappedObject().get(index));
    }

    @Override
    @Signature
    public Memory key(Environment env, Memory... args) {
        return index == -1 ? Memory.FALSE : LongMemory.valueOf(index);
    }

    @Override
    @Signature
    public Memory next(Environment env, Memory... args) {
        index++;
        if (index >= getWrappedObject().size()) {
            index = -1;
        }

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        index = getWrappedObject().isEmpty() ? -1 : 0;
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        return index >= 0 && index < getWrappedObject().size() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }

    @Override
    public Memory count(Environment environment, Memory... memories) {
        return LongMemory.valueOf(getWrappedObject().size());
    }

    @Override
    public Memory offsetExists(Environment environment, Memory... memories) {
        ObservableList list = getWrappedObject();
        int index = memories[0].toInteger();

        return index >= 0 && index < list.size() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public Memory offsetGet(Environment environment, Memory... memories) {
        ObservableList list = getWrappedObject();
        int index = memories[0].toInteger();

        return index >= 0 && index < list.size() ? Memory.wrap(environment, list.get(index)) : Memory.NULL;
    }

    @Override
    public Memory offsetSet(Environment environment, Memory... memories) {
        if (memories[0].isNull()) {
            getWrappedObject().add((T) Memory.unwrap(environment, memories[1]));
        } else {
            environment.exception("Unable to modify the list");
        }
        return Memory.NULL;
    }

    @Override
    public Memory offsetUnset(Environment environment, Memory... memories) {
        ObservableList list = getWrappedObject();
        int index = memories[0].toInteger();

        if (index >= 0 && index < list.size()) {
            list.remove(index);
        }

        return Memory.NULL;
    }
}
