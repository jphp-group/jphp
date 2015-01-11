package php.runtime.ext.core.classes.util;

import php.runtime.Memory;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Property;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.ext.CoreExtension;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.reflection.ClassEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Abstract
@Name(CoreExtension.NAMESPACE + "util\\Collection")
public class WrapCollection extends BaseWrapper<Collection> implements Countable, Iterator {
    interface WrappedInterface {
        @Property boolean empty();

        void clear();
        boolean add(Object value);
        boolean remove(Object value);
        boolean contains(Object value);
    }

    protected ThreadLocal<java.util.Iterator> currentIterator = new ThreadLocal<java.util.Iterator>();
    protected ThreadLocal<Object> currentValue = new ThreadLocal<Object>();
    protected ThreadLocal<Integer> currentKey = new ThreadLocal<Integer>();

    public WrapCollection(Environment env, Collection wrappedObject) {
        super(env, wrappedObject);
    }

    public WrapCollection(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public boolean addAll(Environment env, ForeachIterator iterator) {
        List collection = new ArrayList();

        while (iterator.next()) {
            collection.add(Memory.unwrap(env, iterator.getValue()));
        }

        return getWrappedObject().addAll(collection);
    }

    @Signature
    public boolean removeAll(Environment env, ForeachIterator iterator) {
        List collection = new ArrayList();

        while (iterator.next()) {
            collection.add(Memory.unwrap(env, iterator.getValue()));
        }

        return getWrappedObject().removeAll(collection);
    }

    @Override
    public Memory count(Environment env, Memory... args) {
        return LongMemory.valueOf(getWrappedObject().size());
    }

    @Override
    public Memory current(Environment env, Memory... args) {
        return valid(env).toBoolean() ? Memory.wrap(env, currentValue.get()) : Memory.NULL;
    }

    @Override
    public Memory key(Environment env, Memory... args) {
        return valid(env).toBoolean() ? LongMemory.valueOf(currentKey.get()) : Memory.NULL;
    }

    @Override
    public Memory next(Environment env, Memory... args) {
        java.util.Iterator iterator = currentIterator.get();

        if (iterator.hasNext()) {
            currentValue.set(currentIterator.get().next());

            Integer key = currentKey.get();

            currentKey.set(key == null ? 0 : key + 1);
        } else {
            currentIterator.set(null);
        }

        return Memory.NULL;
    }

    @Override
    public Memory rewind(Environment env, Memory... args) {
        currentIterator.set(getWrappedObject().iterator());

        next(env);

        return Memory.NULL;
    }

    @Override
    public Memory valid(Environment env, Memory... args) {
        return currentIterator.get() == null || !currentIterator.get().hasNext() ? Memory.FALSE : Memory.TRUE;
    }

    @Override
    public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
        return ObjectMemory.valueOf(this).getNewIterator(env, getReferences, getKeyReferences);
    }

    @Override
    public ForeachIterator getNewIterator(Environment env) {
        return ObjectMemory.valueOf(this).getNewIterator(env);
    }
}
