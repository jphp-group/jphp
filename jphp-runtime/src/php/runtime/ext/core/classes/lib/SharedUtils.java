package php.runtime.ext.core.classes.lib;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.Arg;
import php.runtime.annotation.Reflection.Name;
import php.runtime.annotation.Reflection.Signature;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.invoke.Invoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.spl.Countable;
import php.runtime.lang.spl.Traversable;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.LongMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.*;

import static php.runtime.annotation.Reflection.Nullable;

@Name("php\\util\\Shared")
public class SharedUtils extends BaseObject {
    protected static final Map<String, SharedValue> globalValue = new HashMap<String, SharedValue>();

    public SharedUtils(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    private void __construct() {
    }

    @Signature
    public static void resetAll() {
        synchronized (globalValue) {
            globalValue.clear();
        }
    }

    @Signature
    public static SharedValue reset(String name) {
        synchronized (globalValue) {
            return globalValue.remove(name);
        }
    }

    @Signature
    public static SharedValue value(Environment env, String name) throws Throwable {
        return value(env, name, null);
    }

    @Signature
    public static SharedValue value(Environment env, String name, @Nullable Invoker creator) throws Throwable {
        SharedValue value = globalValue.get(name);

        if (value != null) {
            return value;
        }

        synchronized (globalValue) {
            value = new SharedValue(env, (Memory) null);

            SharedValue oldValue = globalValue.get(name);
            if (oldValue != null) {
                return oldValue;
            }

            if (creator != null) {
                value.set(creator.call());
            }


            globalValue.put(name, value);
            return value;
        }
    }


    @Name("php\\util\\SharedMemory")
    abstract public static class SharedMemory extends BaseObject {
        public SharedMemory(Environment env) {
            super(env);
        }

        public SharedMemory(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        synchronized public Memory synchronize(Invoker sync) throws Throwable {
            if (sync.getArgumentCount() > 0) {
                return sync.call(ObjectMemory.valueOf(this));
            } else {
                return sync.call();
            }
        }
    }

    @Name("php\\lang\\ThreadLocal")
    public static class SharedThreadLocal extends BaseObject {
        private ThreadLocal<Memory> local;

        public SharedThreadLocal(Environment env, ThreadLocal<Memory> local) {
            super(env);
            this.local = local;
        }

        public SharedThreadLocal(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        public Memory __debugInfo(Environment env, Memory... args) {
            ArrayMemory info = new ArrayMemory();
            info.refOfIndex("*value").assign(local.get());
            return info.toConstant();
        }

        @Signature(@Arg(value = "value", optional = @Reflection.Optional("null")))
        public Memory __construct(Environment env, Memory... args) {
            this.local = new ThreadLocal<Memory>() {
                @Override
                protected Memory initialValue() {
                    return args[0];
                }
            };

            return Memory.NULL;
        }

        @Signature
        public Memory get(Environment env, Memory... args) {
            return this.local.get();
        }

        @Signature(@Arg("value"))
        public Memory set(Environment env, Memory... args) {
            this.local.set(args[0]);
            return Memory.NULL;
        }

        @Signature
        public Memory remove(Environment env, Memory... args) {
            this.local.remove();
            return Memory.NULL;
        }
    }

    @Name("php\\util\\SharedValue")
    public static class SharedValue extends SharedMemory {
        protected Memory value = null;

        public SharedValue(Environment env, Memory value) {
            super(env);
            this.value = value;
        }

        public SharedValue(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        synchronized public void __construct() {
            value = null;
        }

        @Signature
        synchronized public void __construct(Memory value) {
            this.value = value;
        }

        @Signature
        synchronized public Memory __debugInfo(Environment env, Memory... args) {
            ArrayMemory info = new ArrayMemory();
            info.refOfIndex("*value").assign(value);
            return info.toConstant();
        }

        @Signature
        synchronized public boolean isEmpty() {
            return value == null;
        }

        @Signature
        synchronized public Memory get() {
            return value == null ? Memory.UNDEFINED : value;
        }

        @Signature
        public Memory set(Memory value) {
            return set(value, true);
        }

        @Signature
        synchronized public Memory set(Memory value, boolean override) {
            Memory result = this.value;

            if (value == null || override) {
                this.value = value;
            }

            return result == null ? Memory.UNDEFINED : result;
        }

        @Signature
        synchronized public Memory remove() {
            Memory result = value;
            value = null;
            return result == null ? Memory.UNDEFINED : result;
        }

        @Signature
        synchronized public Memory getAndSet(Invoker update) throws Throwable {
            Memory result = value == null ? Memory.UNDEFINED : value;

            value = update.call(result);

            return result;
        }

        @Signature
        synchronized public Memory setAndGet(Invoker update) throws Throwable {
            value = value == null ? Memory.UNDEFINED : value;
            value = update.call(value);

            return value;
        }

        @Signature
        synchronized public Memory synchronize(Invoker sync) throws Throwable {
            if (sync.getArgumentCount() > 0) {
                return sync.call(ObjectMemory.valueOf(this));
            } else {
                return sync.call();
            }
        }

        @Signature
        synchronized public void __clone(Environment env, TraceInfo trace) throws Throwable {
            if (value == null) {
                this.value = null;
            } else if (value.isObject()) {
                this.value = value.clone(env, trace);
            } else {
                this.value = value.toImmutable();
            }
        }
    }

    @Name("php\\util\\SharedCollection")
    abstract public static class SharedCollection extends SharedMemory implements Countable, Traversable {
        public SharedCollection(Environment env) {
            super(env);
        }

        public SharedCollection(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        abstract public boolean isEmpty();

        @Signature
        abstract public void clear();
    }

    @Name("php\\util\\SharedQueue")
    public static class SharedQueue extends SharedCollection {
        protected Queue<Memory> queue;

        public SharedQueue(Environment env, Queue<Memory> queue) {
            super(env);
            this.queue = queue;
        }

        public SharedQueue(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        public void __construct() {
            queue = new LinkedList<Memory>();
        }

        @Signature
        synchronized public Memory __debugInfo(Environment env, Memory... args) {
            ArrayMemory info = new ArrayMemory();
            info.refOfIndex("*queue").assign(ArrayMemory.ofCollection(queue));
            return info.toConstant();
        }

        @Signature
        public void __construct(ForeachIterator iterator) {
            queue = new LinkedList<Memory>();

            while (iterator.next()) {
                queue.add(iterator.getValue().toImmutable());
            }
        }

        @Signature
        synchronized public boolean isEmpty() {
            return queue.isEmpty();
        }

        @Override
        @Signature
        synchronized public void clear() {
            queue.clear();
        }

        @Signature
        synchronized public boolean add(Memory value) {
            return queue.offer(value);
        }

        @Signature
        synchronized public Memory remove() {
            return queue.remove();
        }

        @Signature
        synchronized public Memory peek() {
            return queue.peek();
        }

        @Signature
        synchronized public Memory poll() {
            return queue.poll();
        }

        @Override
        @Signature
        synchronized public Memory count(Environment env, Memory... args) {
            return LongMemory.valueOf(queue.size());
        }

        @Override
        synchronized public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
            final Object mutex = this;

            return new ForeachIterator(getReferences, getKeyReferences, false) {
                private Iterator<Memory> iterator;
                protected int index;

                @Override
                protected boolean init() {
                    reset();

                    synchronized (mutex) {
                        return !queue.isEmpty();
                    }
                }

                @Override
                protected boolean nextValue() {
                    synchronized (mutex) {
                        if (iterator.hasNext()) {
                            index += 1;
                            currentKeyMemory = LongMemory.valueOf(index);
                            currentKey = currentKeyMemory;
                            currentValue = iterator.next();

                            if (!getReferences) {
                                currentValue = currentValue.toValue();
                            }

                            return true;
                        } else {
                            return false;
                        }
                    }
                }

                @Override
                protected boolean prevValue() {
                    return false;
                }

                @Override
                public void reset() {
                    synchronized (mutex) {
                        currentKeyMemory = Memory.CONST_INT_M1;
                        currentKey = currentKeyMemory;
                        index = -1;
                        iterator = queue.iterator();
                    }
                }
            };
        }

        @Override
        public ForeachIterator getNewIterator(Environment env) {
            return getNewIterator(env, false, false);
        }
    }

    @Name("php\\util\\SharedStack")
    public static class SharedStack extends SharedCollection {
        protected Stack<Memory> stack;

        public SharedStack(Environment env, Stack<Memory> stack) {
            super(env);
            this.stack = stack;
        }

        public SharedStack(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        public void __construct(ForeachIterator iterator) {
            stack = new Stack<Memory>();

            while (iterator.next()) {
                stack.push(iterator.getValue().toImmutable());
            }
        }

        @Signature
        public void __construct() {
            stack = new Stack<Memory>();
        }

        @Signature
        synchronized public Memory __debugInfo(Environment env, Memory... args) {
            ArrayMemory info = new ArrayMemory();
            info.refOfIndex("*stack").assign(ArrayMemory.ofCollection(stack));
            return info.toConstant();
        }

        @Override
        @Signature
        synchronized public Memory count(Environment env, Memory... args) {
            return LongMemory.valueOf(stack.size());
        }

        @Signature
        synchronized public Memory push(Memory arg) {
            return stack.push(arg);
        }

        @Signature
        synchronized public boolean isEmpty() {
            return stack.empty();
        }

        @Signature
        synchronized public Memory pop() {
            if (stack.empty()) {
                return Memory.NULL;
            }

            return stack.pop();
        }

        @Signature
        synchronized public Memory peek() {
            if (stack.empty()) {
                return Memory.NULL;
            }

            return stack.peek();
        }

        @Signature
        synchronized public void clear() {
            stack.clear();
        }

        @Signature
        synchronized public void __clone() {
            Stack<Memory> old = this.stack;

            this.stack = new Stack<Memory>();
            stack.addAll(old);
        }

        @Override
        synchronized public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
            final Object mutex = this;

            return new ForeachIterator(getReferences, getKeyReferences, false) {
                protected Iterator<Memory> iterator;
                protected int index;

                @Override
                protected boolean init() {
                    reset();
                    synchronized (mutex) {
                        return !stack.isEmpty();
                    }
                }

                @Override
                protected boolean nextValue() {
                    synchronized (mutex) {
                        if (iterator.hasNext()) {
                            index += 1;
                            currentKeyMemory = LongMemory.valueOf(index);
                            currentKey = currentKeyMemory;
                            currentValue = iterator.next();

                            if (!getReferences) {
                                currentValue = currentValue.toValue();
                            }

                            return true;
                        } else {
                            return false;
                        }
                    }
                }

                @Override
                protected boolean prevValue() {
                    return false;
                }

                @Override
                public void reset() {
                    synchronized (mutex) {
                        index = -1;
                        currentKeyMemory = Memory.CONST_INT_M1;
                        currentKey = currentKeyMemory;
                        iterator = stack.iterator();
                    }
                }
            };
        }

        @Override
        public ForeachIterator getNewIterator(Environment env) {
            return getNewIterator(env, false, false);
        }
    }

    @Name("php\\util\\SharedMap")
    public static class SharedMap extends SharedCollection {
        protected Map<String, Memory> map;

        public SharedMap(Environment env, Map<String, Memory> map) {
            super(env);
            this.map = map;
        }

        public SharedMap(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        @Signature
        public void __construct(ForeachIterator iterator) {
            map = new LinkedHashMap<String, Memory>();

            while (iterator.next()) {
                map.put(iterator.getKey().toString(), iterator.getValue().toImmutable());
            }
        }

        @Signature
        public void __construct() {
            map = new LinkedHashMap<String, Memory>();
        }

        @Signature
        synchronized public Memory __debugInfo(Environment env, Memory... args) {
            ArrayMemory info = new ArrayMemory();
            info.refOfIndex("*map").assign(ArrayMemory.ofMap(map));
            return info.toConstant();
        }

        @Override
        @Signature
        synchronized public boolean isEmpty() {
            return map.isEmpty();
        }

        @Signature
        synchronized public boolean has(String key) {
            return map.containsKey(key);
        }

        @Signature
        synchronized public Memory count(Environment env, Memory... args) {
            return LongMemory.valueOf(map.size());
        }

        @Signature
        synchronized public Memory get(String key) {
            return get(key, Memory.UNDEFINED);
        }

        @Signature
        synchronized public Memory getOrCreate(String key, Invoker create) throws Throwable {
            Memory result = map.get(key);

            if (result == null) {
                result = create.call();
                map.put(key, result);
            }

            return result;
        }

        @Signature
        synchronized public Memory get(String key, Memory defaultValue) {
            Memory result = map.get(key);
            return result == null ? defaultValue : result;
        }

        @Signature
        synchronized public Memory set(String key, Memory value, boolean override) {
            if (override || map.get(key) == null) {
                Memory result = map.put(key, value);
                return result == null ? Memory.UNDEFINED : result;
            }

            return Memory.NULL;
        }

        @Signature
        public Memory set(String key, Memory value) {
            return set(key, value, true);
        }

        @Signature
        synchronized public Memory remove(String key) {
            Memory memory = map.remove(key);
            return memory == null ? Memory.UNDEFINED : memory;
        }

        @Signature
        synchronized public void clear() {
            map.clear();
        }

        @Signature
        synchronized public void __clone() {
            this.map = new LinkedHashMap<String, Memory>(map);
        }

        @Override
        synchronized public ForeachIterator getNewIterator(Environment env, boolean getReferences, boolean getKeyReferences) {
            final Object mutex = this;

            return new ForeachIterator(getReferences, getKeyReferences, false) {

                private Iterator<Map.Entry<String, Memory>> entries;

                @Override
                protected boolean init() {
                    reset();
                    synchronized (mutex) {
                        return !map.isEmpty();
                    }
                }

                @Override
                protected boolean nextValue() {
                    synchronized (mutex) {
                        if (entries.hasNext()) {
                            Map.Entry<String, Memory> entry = entries.next();

                            currentKey = entry.getKey();
                            currentKeyMemory = StringMemory.valueOf(currentKey.toString());

                            currentValue = entry.getValue();

                            if (!getReferences) {
                                currentValue = currentValue.toValue();
                            }

                            return true;
                        }

                        return false;
                    }
                }

                @Override
                protected boolean prevValue() {
                    return false;
                }

                @Override
                public void reset() {
                    synchronized (mutex) {
                        entries = map.entrySet().iterator();
                    }
                }
            };
        }

        @Override
        public ForeachIterator getNewIterator(Environment env) {
            return getNewIterator(env, false, false);
        }
    }
}
