package org.develnext.jphp.ext.javafx.classes;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.common.Callback;
import php.runtime.common.Messages;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.exception.BaseTypeError;
import php.runtime.lang.spl.ArrayAccess;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.lang.support.ICloneableObject;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.support.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

@Abstract
@Name(JavaFXExtension.NS + "UXList")
public class UXList<T> extends BaseWrapper<ObservableList<T>> implements Iterator, Countable, ArrayAccess, ICloneableObject<UXList<T>> {
    private int index = -1;
    private Class<?> unwrapClass = null;
    private Callback<T, Memory> unwrapCallback;
    private Callback<Memory, T> wrapCallback;

    public UXList(Environment env, ObservableList<T> wrappedObject) {
        super(env, wrappedObject);
    }

    public UXList(Environment env, ObservableList<T> wrappedObject, Class<?> unwrapClass) {
        super(env, wrappedObject);
        this.unwrapClass = unwrapClass;
    }

    public UXList(Environment env, ObservableList<T> wrappedObject, Callback<T, Memory> unwrapCallback, Callback<Memory, T> wrapCallback) {
        super(env, wrappedObject);
        this.unwrapCallback = unwrapCallback;
        this.wrapCallback = wrapCallback;
    }

    public UXList(Environment env, ObservableList<T> wrappedObject, Class<T> unwrapClass, Callback<T, Memory> unwrapCallback, Callback<Memory, T> wrapCallback) {
        super(env, wrappedObject);
        this.unwrapClass = unwrapClass;
        this.unwrapCallback = unwrapCallback;
        this.wrapCallback = wrapCallback;
    }

    public UXList(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @SuppressWarnings("unchecked")
    protected Memory wrap(Environment env, T object) {
        if (wrapCallback != null) {
            return wrapCallback.call(object);
        }

        return Memory.wrap(env, object);
    }

    @SuppressWarnings("unchecked")
    protected T unwrap(Environment env, Memory object, String methodName) {
        T unwrap = unwrapCallback != null ? unwrapCallback.call(object) : (T) Memory.unwrap(env, object);

        if (unwrap == null) {
            //env.exception(BaseTypeError.class, "Argument passed to UXList::%s() must be not NULL");
        }

        if (unwrapClass != null && unwrap != null && !unwrapClass.isAssignableFrom(unwrap.getClass())) {
            Class<? extends BaseWrapper> wrapper = MemoryOperation.getWrapper(unwrapClass);

            String message = String.format(
                    "Argument passed to UXList::%s() must be of the type %s type",
                    methodName,
                    wrapper == null ? unwrapClass.getName() : ReflectionUtils.getClassName(wrapper)
            );

            env.exception(BaseTypeError.class, message);
        }

        return unwrap;
    }

    @Signature
    public Memory __debugInfo(Environment env, Memory... args) {
        ArrayMemory r = new ArrayMemory();

        ForeachIterator iterator = getNewIterator(env);

        while (iterator.next()) {
            r.add(iterator.getValue().toImmutable());
        }

        return r.toConstant();
    }

    @Signature
    @SuppressWarnings("unchecked")
    public int indexOf(Environment env, Memory object) {
        return getWrappedObject().indexOf(unwrap(env, object, "indexOf"));
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void replace(Environment env, Memory object, Memory newObject) {
        int index = indexOf(env, object);

        if (index != -1) {
            getWrappedObject().add(index, unwrap(env, newObject, "replace"));
            getWrappedObject().remove(index + 1);
        }
    }

    @Signature
    public boolean has(Environment env, Memory object) {
        for (T t : getWrappedObject()) {
            if (wrap(env, t).equal(object)) {
                return true;
            }
        }

        return false;
    }

    @Signature
    public void set(Environment env, int index, Memory object) {
        getWrappedObject().set(index, unwrap(env, object, "set"));
    }

    @Signature
    @SuppressWarnings("unchecked")
    public boolean add(Environment env, Memory object) {
        return getWrappedObject().add(unwrap(env, object, "add"));
    }

    @Signature
    @SuppressWarnings("unchecked")
    public void insert(Environment env, int index, Memory object) {
        if (index >= 0) {
            getWrappedObject().add(index, unwrap(env, object, "insert"));
            return;
        }

        throw new IllegalArgumentException("index must be greater or equal to 0");
    }

    @Signature
    public void addAll(Environment env, ForeachIterator iterator) throws Throwable {
        if (iterator == null) {
            return;
        }

        while (iterator.next()) {
            env.invokeMethod(this, "add", iterator.getValue().toImmutable());
        }
    }

    @Signature
    public void setAll(Environment env, ForeachIterator iterator) throws Throwable {
        if (iterator == null) {
            return;
        }

        env.invokeMethod(this, "clear");

        while (iterator.next()) {
            env.invokeMethod(this, "add", iterator.getValue().toImmutable());
        }
    }

    @Signature
    public void insertAll(Environment env, int index, ForeachIterator iterator) throws Throwable {
        List<T> list = new ArrayList<>();

        while (iterator.next()) {
            list.add(unwrap(env, iterator.getValue(), "insertAll"));
        }

        if (index >= 0) {
            getWrappedObject().addAll(index, list);
        } else {
            throw new IllegalArgumentException("index must be greater or equal to 0");
        }
    }

    @Signature
    @SuppressWarnings("unchecked")
    public boolean remove(Environment env, Memory object) {
        return getWrappedObject().remove(unwrap(env, object, "remove"));
    }


    @Signature
    @SuppressWarnings("unchecked")
    public boolean removeByIndex(Environment env, int index) {
        try {
            getWrappedObject().remove(index);
            return true;
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    @Signature
    public void clear() {
        getWrappedObject().clear();
    }

    @Signature
    public Memory last(Environment env) {
        ObservableList<T> list = getWrappedObject();

        if (list.isEmpty()) {
            return Memory.NULL;
        } else {
            return wrap(env, list.get(list.size() - 1));
        }
    }

    @Signature
    public boolean isEmpty() {
        return getWrappedObject().isEmpty();
    }

    @Signature
    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        if (index == -1) {
            return Memory.NULL;
        }

        return wrap(env, getWrappedObject().get(index));
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

    @Getter
    public int getCount() {
        return getWrappedObject().size();
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

        return index >= 0 && index < list.size() ? wrap(environment, (T) list.get(index)) : Memory.NULL;
    }

    @Override
    public Memory offsetSet(Environment environment, Memory... memories) {
        if (memories[0].isNull()) {
            getWrappedObject().add(unwrap(environment, memories[1], "offsetSet"));
        } else {
            insert(environment, memories[0].toInteger(), memories[1]);
            removeByIndex(environment, memories[0].toInteger() + 1);
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

    @Signature
    public void addListener(Invoker invoker) {
        getWrappedObject().addListener((ListChangeListener<T>) c -> {
            invoker.callAny();
        });
    }

    @Signature
    public Memory toArray(Environment env) {
        ArrayMemory list = ArrayMemory.createListed(getCount());

        for (T t : getWrappedObject()) {
            list.add(wrap(env, t));
        }

        return list.toConstant();
    }

    @Override
    @SuppressWarnings("unchecked")
    public UXList<T> __clone(Environment environment, TraceInfo traceInfo) {
        ObservableList<T> list = FXCollections.observableList(getWrappedObject());

        return new UXList<>(environment, list, (Class<T>) this.unwrapClass, this.unwrapCallback, this.wrapCallback);
    }
}
