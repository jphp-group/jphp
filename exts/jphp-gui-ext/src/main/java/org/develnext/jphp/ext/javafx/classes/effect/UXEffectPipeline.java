package org.develnext.jphp.ext.javafx.classes.effect;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import org.develnext.jphp.ext.javafx.JavaFXExtension;
import org.develnext.jphp.ext.javafx.support.effect.EffectPipeline;
import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Abstract;
import php.runtime.annotation.Reflection.Getter;
import php.runtime.annotation.Reflection.Name;
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

import java.util.List;

@Abstract
@Name(JavaFXExtension.NS + "effect\\UXEffectPipeline")
public class UXEffectPipeline extends BaseWrapper<EffectPipeline> implements Countable, Iterator, ArrayAccess {
    interface WrappedInterface {
        void clear();
        void add(Effect effect);
        void addAll(List<Effect> effects);
        void remove(Effect effect);
        void has(Effect effect);
        void disable(Effect effect);
        void enable(Effect effect);
        boolean isEnabled(Effect effect);
        List<Effect> all();
    }

    protected Node node;
    private int index = -1;

    public UXEffectPipeline(Environment env, EffectPipeline wrappedObject) {
        super(env, wrappedObject);
    }

    public UXEffectPipeline(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public UXEffectPipeline(Environment env, final Node node) {
        super(env, new EffectPipeline());
        this.node = node;

        getWrappedObject().chainedEffectProperty()
                .addListener((observable, oldValue, newValue) -> Platform.runLater(() -> node.setEffect(newValue)));

        node.effectProperty()
                .addListener((observable, oldValue, newValue) -> {
            if (newValue != getWrappedObject().getChainedEffect()) {
                Platform.runLater(() -> node.setEffect(getWrappedObject().getChainedEffect()));
            }
        });
    }

    @Getter
    public int getCount() {
        return getWrappedObject().count();
    }

    @Signature
    public void insert(int i, Effect effect) {
        getWrappedObject().add(i, effect);
    }

    @Override
    public Memory count(Environment env, Memory... args) {
        return LongMemory.valueOf(getWrappedObject().count());
    }

    @Override
    @Signature
    public Memory current(Environment env, Memory... args) {
        if (index == -1) {
            return Memory.NULL;
        }

        return Memory.wrap(env, getWrappedObject().all().get(index));
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
        if (index >= getWrappedObject().count()) {
            index = -1;
        }

        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory rewind(Environment env, Memory... args) {
        index = getWrappedObject().count() == 0 ? -1 : 0;
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory valid(Environment env, Memory... args) {
        return index >= 0 && index < getWrappedObject().count() ? Memory.TRUE : Memory.FALSE;
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
    public Memory offsetExists(Environment environment, Memory... memories) {
        List list = getWrappedObject().all();
        int index = memories[0].toInteger();

        return index >= 0 && index < list.size() ? Memory.TRUE : Memory.FALSE;
    }

    @Override
    public Memory offsetGet(Environment environment, Memory... memories) {
        List list = getWrappedObject().all();
        int index = memories[0].toInteger();

        return index >= 0 && index < list.size() ? Memory.wrap(environment, list.get(index)) : Memory.NULL;
    }

    @Override
    public Memory offsetSet(Environment environment, Memory... memories) {
        if (memories[0].isNull()) {
            if (memories[0].instanceOf(UXEffect.class)) {
                getWrappedObject().add((Effect) Memory.unwrap(environment, memories[1]));
            } else {
                environment.exception("Unable to set non-effect value");
            }
        } else {
            environment.exception("Unable to modify the list");
        }
        return Memory.NULL;
    }

    @Override
    public Memory offsetUnset(Environment environment, Memory... memories) {
        List<Effect> list = getWrappedObject().all();
        int index = memories[0].toInteger();

        if (index >= 0 && index < list.size()) {
            this.getWrappedObject().remove(list.get(index));
        }

        return Memory.NULL;
    }
}
