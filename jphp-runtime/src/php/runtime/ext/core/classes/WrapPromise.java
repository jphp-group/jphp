package php.runtime.ext.core.classes;

import php.runtime.Memory;
import php.runtime.annotation.Reflection;
import php.runtime.annotation.Reflection.*;
import php.runtime.common.Callback;
import php.runtime.env.Environment;
import php.runtime.ext.java.JavaException;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.RunnableInvoker;
import php.runtime.lang.BaseObject;
import php.runtime.lang.ForeachIterator;
import php.runtime.lang.IObject;
import php.runtime.lang.exception.BaseBaseException;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Name("php\\concurrent\\Promise")
public class WrapPromise extends BaseObject {
    enum State {PENDING, FULFILLED, REJECTED}

    static class Subscriber {
        public WrapPromise subPromise;
        public Invoker onFulfilled;
        public Invoker onRejected;

        public Subscriber(WrapPromise subPromise, Invoker onFulfilled, Invoker onRejected) {
            this.subPromise = subPromise;
            this.onFulfilled = onFulfilled;
            this.onRejected = onRejected;
        }
    }

    private State state = State.PENDING;
    private Memory value = Memory.NULL;
    private List<Subscriber> subscribers = new LinkedList<>();

    public WrapPromise(Environment env) {
        super(env);
    }

    public WrapPromise(Environment env, Callback<Memory, Memory[]> executor) {
        super(env);

        if (executor != null) {
            __construct(env, new RunnableInvoker(env, executor));
        }
    }



    public WrapPromise(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    @Signature
    public void __construct(Environment env) {
        __construct(env, null);
    }

    @Signature
    public void __construct(Environment env, @Nullable Invoker executor) {
        if (executor != null) {
            WrapInvoker makeFulfill = new WrapInvoker(env, ArrayMemory.of(ObjectMemory.valueOf(this), StringMemory.valueOf("makeFulfill")));
            WrapInvoker makeReject = new WrapInvoker(env, ArrayMemory.of(ObjectMemory.valueOf(this), StringMemory.valueOf("makeReject")));

            try {
                executor.call(ObjectMemory.valueOf(makeFulfill), ObjectMemory.valueOf(makeReject));
            } catch (Throwable e) {
                try {
                    if (e instanceof IObject) {
                        makeReject.call(env, ObjectMemory.valueOf((IObject) e));
                    } else {
                        makeReject.call(env, ObjectMemory.valueOf(new JavaException(env, e)));
                    }
                } catch (Exception e2) {
                    env.forwardThrow(e2);
                }
            }
        }
    }

    @Signature
    private void makeFulfill(Environment env, Memory result) throws Exception {
        if (this.state != State.PENDING) {
            throw new IllegalStateException("This promise is already resolved, and you\'re not allowed to resolve a promise more than once");
        }

        this.state = State.FULFILLED;
        this.value = result.toImmutable();

        for (Subscriber subscriber : subscribers) {
            this.invokeCallback(env, subscriber.subPromise, subscriber.onFulfilled);
        }
    }

    @Signature
    private void makeReject(Environment env, @Arg(typeClass = "Throwable") Memory error) throws Exception {
        if (this.state != State.PENDING) {
            throw new IllegalStateException("This promise is already resolved, and you\'re not allowed to resolve a promise more than once");
        }
        this.state = State.REJECTED;
        this.value = error;

        for (Subscriber subscriber : subscribers) {
            this.invokeCallback(env, subscriber.subPromise, subscriber.onRejected);
        }
    }

    private void invokeCallback(Environment env, WrapPromise subPromise, Invoker callback) throws Exception {
        if (callback != null) {
            try {
                Memory result = callback.call(this.value);

                if (result.instanceOf(WrapPromise.class)) {
                    result.toObject(WrapPromise.class).then(
                            env,
                            Invoker.create(env, ArrayMemory.ofAny(env, subPromise, "makeFulfill")),
                            Invoker.create(env, ArrayMemory.ofAny(env, subPromise, "makeReject"))
                    );
                } else {
                    subPromise.makeFulfill(env, result);
                }
            } catch (Throwable e) {
                if (e instanceof IObject) {
                    subPromise.makeReject(env, ObjectMemory.valueOf((IObject) e));
                } else {
                    subPromise.makeReject(env, ObjectMemory.valueOf(new JavaException(env, e)));
                }
            }
        } else {
            if (this.state == State.FULFILLED) {
                subPromise.makeFulfill(env, this.value);
            } else {
                subPromise.makeReject(env, this.value);
            }
        }
    }

    @Signature
    public WrapPromise then(Environment env, @Nullable @Optional("null") Invoker onFulfilled, @Nullable @Optional("null") Invoker onRejected) throws Exception {
        WrapPromise subPromise = new WrapPromise(env);

        switch (this.state) {
            case PENDING:
                this.subscribers.add(new Subscriber(subPromise, onFulfilled, onRejected));
                break;
            case FULFILLED:
                this.invokeCallback(env, subPromise, onFulfilled);
                break;
            case REJECTED:
                this.invokeCallback(env, subPromise, onRejected);
                break;
        }

        return subPromise;
    }

    @Name("catch")
    @Signature
    public WrapPromise _catch(Environment env, @Nullable @Optional("null") Invoker onRejected) throws Exception {
        return this.then(env, null, onRejected);
    }

    @Signature
    public Memory wait(Environment env) {
        while (this.state == State.PENDING) {
            // loop.
        }

        if (this.state == State.FULFILLED) return this.value;
        if (this.state == State.REJECTED) {
            env.__throwException(this.value.toObject(BaseBaseException.class));
        }

        return Memory.NULL;
    }

    @Signature
    public static WrapPromise resolve(Environment env, Memory result) {
        if (result.instanceOf(WrapPromise.class)) {
            WrapPromise promise = new WrapPromise(env);
            promise.__construct(env, new RunnableInvoker(env, new Callback<Memory, Memory[]>() {
                @Override
                public Memory call(Memory[] args) {
                    try {
                        return ObjectMemory.valueOf(
                                promise.then(env, Invoker.create(env, args[0]), Invoker.create(env, args[1]))
                        );
                    } catch (Exception e) {
                        env.forwardThrow(e);
                        return Memory.NULL;
                    }
                }
            }));

            return promise;
        } else {
            return new WrapPromise(env, new Callback<Memory, Memory[]>() {
                @Override
                public Memory call(Memory[] args) {
                    // resolve.
                    return Invoker.create(env, args[0]).callAny(result);
                }
            });
        }
    }

    @Signature
    public static WrapPromise reject(Environment env, @Arg(typeClass = "Throwable") Memory error) {
        return new WrapPromise(env, new Callback<Memory, Memory[]>() {
            @Override
            public Memory call(Memory[] args) {
                // reject.
                return Invoker.create(env, args[1]).callAny(error);
            }
        });
    }

    @Signature
    public static WrapPromise race(Environment env, ForeachIterator promises) {
        List<WrapPromise> list = new LinkedList<>();

        while (promises.next()) {
            Memory value = promises.getValue();

            if (value.instanceOf(WrapPromise.class)) {
                list.add(value.toObject(WrapPromise.class));
            }
        }

        if (list.isEmpty()) {
            return resolve(env, Memory.NULL);
        }

        return new WrapPromise(env, new Callback<Memory, Memory[]>() {
            @Override
            public Memory call(Memory[] args) {
                final boolean[] done = {false};

                Invoker resolve = Invoker.create(env, args[0]);
                Invoker reject = Invoker.create(env, args[1]);

                for (WrapPromise promise : list) {
                    try {
                        promise.then(env, new RunnableInvoker(env, new Callback<Memory, Memory[]>() {
                            @Override
                            public Memory call(Memory[] args) {
                                if (!done[0]) {
                                    done[0] = true;
                                    resolve.callNoThrow(args[0]);
                                }

                                return Memory.NULL;
                            }
                        }), new RunnableInvoker(env, new Callback<Memory, Memory[]>() {
                            @Override
                            public Memory call(Memory[] memories) {
                                if (!done[0]) {
                                    done[0] = true;
                                    reject.callNoThrow(args[0]);
                                }

                                return Memory.NULL;
                            }
                        }));
                    } catch (Exception e) {
                        env.forwardThrow(e);
                    }
                }

                return Memory.NULL;
            }
        });
    }

    @Signature
    public static WrapPromise all(Environment env, ForeachIterator promises) {
        Map<String, WrapPromise> list = new LinkedHashMap<>();

        while (promises.next()) {
            Memory value = promises.getValue();

            if (value.instanceOf(WrapPromise.class)) {
                list.put(promises.getStringKey(), value.toObject(WrapPromise.class));
            }
        }

        if (list.isEmpty()) {
            return resolve(env, Memory.NULL);
        }

        int max = list.size();
        ArrayMemory result = ArrayMemory.createListed(max);

        for (String key : list.keySet()) {
            result.put(key, Memory.NULL);
        }

        return new WrapPromise(env, new Callback<Memory, Memory[]>() {
            @Override
            public Memory call(Memory[] args) {
                final boolean[] done = {false};

                Invoker resolve = Invoker.create(env, args[0]);
                Invoker reject = Invoker.create(env, args[1]);

                final int[] count = {0};

                for (Map.Entry<String, WrapPromise> entry : list.entrySet()) {
                    WrapPromise promise = entry.getValue();
                    String key = entry.getKey();

                    try {
                        promise.then(env, new RunnableInvoker(env, new Callback<Memory, Memory[]>() {
                            @Override
                            public Memory call(Memory[] args) {
                                if (!done[0]) {
                                    result.put(key, args[0]);
                                    count[0]++;

                                    if (max == count[0]) {
                                        done[0] = true;
                                        resolve.callNoThrow(result);
                                    }
                                }

                                return Memory.NULL;
                            }
                        }), new RunnableInvoker(env, new Callback<Memory, Memory[]>() {
                            @Override
                            public Memory call(Memory[] memories) {
                                if (!done[0]) {
                                    done[0] = true;
                                    reject.callNoThrow(args[0]);
                                }

                                return Memory.NULL;
                            }
                        }));
                    } catch (Exception e) {
                        env.forwardThrow(e);
                    }
                }

                return Memory.NULL;
            }
        });
    }
}
