package php.runtime.lang;

import php.runtime.Memory;
import php.runtime.env.CallStack;
import php.runtime.env.CallStackItem;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.exceptions.CriticalException;
import php.runtime.exceptions.FinallyException;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.lang.spl.iterator.Iterator;
import php.runtime.lang.support.IManualDestructable;
import php.runtime.memory.KeyValueMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.helper.GeneratorEntity;
import php.runtime.util.generator.YieldAdapterIterator;

import java.util.NoSuchElementException;

import static php.runtime.annotation.Reflection.*;

@Name("Generator")
@Final
abstract public class Generator extends BaseObject implements Iterator, IManualDestructable {
    private enum ClosedType { DEFAULT, MANUAL }

    protected Memory genReturn = null;
    protected Memory self;
    protected final Memory[] uses;

    protected boolean isInit = false;
    protected int counter = 0;

    protected boolean valid = true;
    protected final YieldAdapterIterator<Bucket> iterator;
    protected final php.runtime.util.generator.Generator<Bucket> gen;

    protected final static ThreadLocal<Generator> currentGenerator = new ThreadLocal<>();

    protected CallStackItem callStackItem;
    protected CallStack callStack;

    protected Throwable lastThrowable = null;
    protected RuntimeException newThrow = null;

    protected boolean busy;
    protected ClosedType closed;

    public Generator(final Environment env, ClassEntity generator, Memory self, Memory[] uses) {
        super(env, generator);
        if (generator == null) {
            throw new CriticalException("Unable to create generator");
        }

        env.registerObjectInGC(this);

        this.self = self;
        this.uses = uses;

        CallStackItem stackItem = env.peekCall(0);
        this.callStack     = env.getCallStack();
        this.callStackItem = stackItem == null ? null : new CallStackItem(stackItem);

        gen = new php.runtime.util.generator.Generator<Bucket>() {
            @Override
            protected void run(YieldAdapterIterator<Bucket> yieldAdapter) {
                try {
                    currentGenerator.set(Generator.this);
                    env.__replaceCallStack(Generator.this.callStack);

                    Generator.this._run(env);
                } catch (Throwable e) {
                    lastThrowable = e;
                    Generator.this.setCurrent(Memory.NULL);
                }
            }
        };
        iterator = gen.iterator();
    }

    abstract protected Memory _run(Environment env, Memory... args);

    protected Memory _next(Environment env) {
        if (busy) {
            env.error(env.trace(), "Cannot resume an already running generator");
        }

        boolean x2 = false;
        if (callStackItem != null) {
            env.pushCall(new CallStackItem(callStackItem));
            x2 = true;
        }

        try {
            counter += 1;
            busy = true;
            return iterator.next().getValue();
        } catch (NoSuchElementException e) {
            valid = false;
            callStackItem = null;
        } finally {
            if (x2) env.popCall();

            busy = false;
            checkThrow();
        }
        return null;
    }

    protected void checkNewThrow() {
        if (newThrow != null) {
            try {
                throw newThrow;
            } finally {
                this.newThrow = null;
            }
        }
    }

    protected void checkThrow() {
        if (lastThrowable != null) {
            try {
                if (lastThrowable instanceof RuntimeException) {
                    throw (RuntimeException)lastThrowable;
                }
                throw new RuntimeException(lastThrowable);
            } finally {
                this.lastThrowable = null;
            }
        }
    }

    public boolean isReturnReferences() {
        return ((GeneratorEntity)getReflection()).isReturnReference();
    }

    @Override
    public void onManualDestruct(Environment env) {
        closed = ClosedType.MANUAL;
    }

    @Signature
    public Memory __destruct(Environment env, Memory... args) {
        if (isInit) {
            if (closed == null) {
                closed = ClosedType.DEFAULT;
            }
            newThrow = new FinallyException();
            _next(env);
        }
        return Memory.NULL;
    }

    @Override
    @Signature
    public Memory next(Environment env, Memory... args) {
        _next(env);
        return Memory.NULL;
    }

    @Signature(@Arg("value"))
    synchronized public Memory send(Environment env, Memory... args) {
        if (!isInit) {
           rewind(env);
        }

        Bucket current = iterator.getCurrentValue();
        if (current == null) {
            iterator.setCurrentValue(new Bucket(args[0]));
        } else {
            current.pushValue(args[0]);
        }

        return _next(env);
    }

    @Name("throw")
    @Signature(@Arg(value = "throwable", nativeType = BaseException.class))
    synchronized public Memory _throw(Environment env, Memory... args) {
        if (valid) {
            newThrow = args[0].toObject(BaseException.class);
            ((BaseException)newThrow).setTraceInfo(env, env.trace());
            return _next(env);
        } else {
            env.__throwException(args[0].toObject(BaseException.class));
        }
        return Memory.NULL;
    }

    @Signature
    @Override
    public Memory rewind(Environment env, Memory... args) {
        if (!valid) {
            env.exception("Cannot traverse an already closed generator");
        }

        if (counter > 1) {
            env.exception("Cannot rewind a generator that was already run");
        }

        if (!isInit) {
            counter = 0;
            _next(env);
            isInit = true;
        }

        return Memory.NULL;
    }

    @Signature
    @Override
    public Memory valid(Environment env, Memory... args) {
        return valid ? Memory.TRUE : Memory.FALSE;
    }

    @Signature
    @Override
    public Memory current(Environment env, Memory... args) {
        if (!isInit) {
            rewind(env);
        }

        if (iterator.getCurrentValue() == null) {
            return Memory.NULL;
        }

        return iterator.getCurrentValue().getValue();
    }

    protected Memory __current() {
        Bucket current = iterator.getCurrentValue();
        return current == null ? Memory.NULL : current.getValue();
    }

    @Signature
    @Override
    public Memory key(Environment env, Memory... args) {
        if (!isInit) {
            rewind(env);
        }

        if (iterator.getCurrentValue() == null) {
            return Memory.NULL;
        }

        return iterator.getCurrentValue().getKey();
    }

    @Signature
    final public Memory __clone(Environment env, Memory... args) {
        env.error(ErrorType.E_ERROR, "Trying to clone an uncloneable object of class " + getReflection().getName());
        return Memory.NULL;
    }

    @Signature
    public Memory __wakeup(Environment env, Memory... args) {
        env.exception("Unserialization of 'Generator' is not allowed");
        return Memory.NULL;
    }

    @Signature
    private Memory __sleep(Environment env, Memory... args) {
        env.exception(env.trace(), "Serialization of 'Generator' is not allowed");
        return Memory.NULL;
    }

    @Signature
    public Memory getReturn(Environment env, Memory... args) {
        return genReturn == null ? Memory.NULL : genReturn;
    }

    public CallStackItem getCallStackItem() {
        return callStackItem;
    }

    protected void _setValid(boolean valid) {
        checkNewThrow();

        this.valid = valid;
        if (!valid) {
            callStackItem = null;
        }
    }

    protected Memory yield(Environment env, TraceInfo trace) {
        return yield(env, trace, Memory.NULL);
    }

    protected Bucket setCurrent(Memory value) {
        boolean returnRef = (((GeneratorEntity)getReflection()).isReturnReference());

        Bucket current = iterator.getCurrentValue();
        if (value instanceof KeyValueMemory) {
            if (current != null) {
                current.setKey(((KeyValueMemory) value).key);
                current.setValue(returnRef ? new ReferenceMemory(value) : value.toValue());
            } else {
                current = new Bucket(((KeyValueMemory) value).key, returnRef ? new ReferenceMemory(value) : value.toValue());
            }
        } else {
            if (returnRef) {
                value = new ReferenceMemory(value);
            }

            if (current != null) {
                current.pushValue(value);
            } else {
                current = new Bucket(Memory.CONST_INT_0, value);
            }
        }

        return current;
    }

    protected Memory yieldFrom(Environment env, TraceInfo trace) {
        return yieldFrom(env, trace, Memory.NULL);
    }

    protected Memory yieldFrom(Environment env, TraceInfo trace, Memory iterable) {
        if (closed == ClosedType.MANUAL) {
            env.error(trace, "Cannot yield from finally in a force-closed generator");
        }

        ForeachIterator iterator = iterable.getNewIterator(env);
        if (iterator == null) {
            env.error(trace, "Can use \"yield from\" only with arrays and Traversables");
            return Memory.NULL;
        }

        Generator subGenerator = null;

        if (iterable.instanceOf("Generator", "generator")) {
            subGenerator = iterable.toObject(Generator.class);
        }

        Bucket current = null;

        if (subGenerator != null) {
            if (!subGenerator.isInit) {
                if (subGenerator.counter < 1) {
                    subGenerator.rewind(env);
                } else {
                    subGenerator._next(env);
                }
            }

            while (subGenerator.valid){
                checkNewThrow();

                current = setCurrent(subGenerator.__current());
                subGenerator._next(env);

                gen.yield(current);

                checkNewThrow();
            }
        } else {
            while (iterator.next()) {
                checkNewThrow();

                current = setCurrent(iterator.getValue());
                gen.yield(current);

                checkNewThrow();
            }
        }

        if (subGenerator != null) {
            Memory genReturn = subGenerator.genReturn;

            if (genReturn != null) {
                return genReturn;
            }
        }

        return current != null ? current.getValue() : Memory.NULL;
    }

    protected Memory yield(Environment env, TraceInfo trace, Memory value) {
        if (closed == ClosedType.MANUAL) {
            env.error(trace, "Cannot yield from finally in a force-closed generator");
        }

        checkNewThrow();

        Bucket current = setCurrent(value);
        gen.yield(current);

        checkNewThrow();
        return current.getValue();
    }

    protected Memory yield(Memory key, Memory value) {
        return gen.yield(new Bucket(key, value)).getValue();
    }

    protected Memory setReturn(Environment env, TraceInfo trace, Memory result) {
        return genReturn = result;
    }

    public static Generator current() {
        return currentGenerator.get();
    }

    private static class Bucket {
        private Memory key;
        private Memory value;

        private Bucket(Memory value) {
            this.key = Memory.CONST_INT_0;
            this.value = value;
        }

        private Bucket(Memory key, Memory value) {
            this.key = key;
            this.value = value;
        }

        public Memory getKey() {
            return key;
        }

        public void setKey(Memory key) {
            this.key = key;
        }

        public Memory getValue() {
            return value;
        }

        public void setValue(Memory value) {
            this.value = value;
        }

        public void pushValue(Memory value) {
            key = key.inc();
            this.value = value;
        }
    }
}
