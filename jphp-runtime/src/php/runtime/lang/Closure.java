package php.runtime.lang;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.common.collections.map.HashedMap;
import php.runtime.env.Environment;
import php.runtime.exceptions.support.ErrorType;
import php.runtime.invoke.Invoker;
import php.runtime.invoke.ObjectInvokeHelper;
import php.runtime.lang.support.IStaticVariables;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.ReferenceMemory;
import php.runtime.reflection.ClassEntity;
import php.runtime.reflection.ParameterEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static php.runtime.annotation.Reflection.*;

@Name("Closure")
public abstract class Closure extends BaseObject implements IStaticVariables, Cloneable {
    protected Memory[] uses;
    private Map<String, ReferenceMemory> statics;
    protected Memory self = Memory.NULL;
    protected String scope = null;

    /*public Closure(Environment env, ClassEntity closure, Memory self, Memory[] uses) {
        this(env, closure, self, null, uses);
    }*/

    public Closure(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public Closure(Environment env, ClassEntity closure, Memory self, String scope, Memory[] uses) {
        super(closure);
        this.self = self;
        this.scope = scope;

        if (env != null && (this.scope == null || this.scope.isEmpty())) {
            this.scope = env.getLateStatic();
        }

        if (this.scope != null && this.scope.isEmpty()) {
            this.scope = null;
        }

        this.uses = uses;
    }

    @Signature
    abstract public Memory __invoke(Environment env, Memory... args) throws Throwable;

    public Memory[] getUses() {
        return uses == null ? new Memory[0] : uses;
    }

    @Signature({@Arg("prop"), @Arg("value")})
    public Memory __set(Environment env, Memory... args){
        env.error(ErrorType.E_ERROR, "Closure object cannot have properties");
        return Memory.NULL;
    }

    @Signature({@Arg("prop")})
    public Memory __get(Environment env, Memory... args){
        return __set(env, args);
    }

    @Signature({@Arg("prop")})
    public Memory __unset(Environment env, Memory... args){
        return __set(env, args);
    }

    @Signature({@Arg("prop")})
    public Memory __isset(Environment env, Memory... args){
        return __set(env, args);
    }

    @Override
    public Memory getStatic(String name){
        if (statics == null)
            return null;

        return statics.get(name);
    }

    public String getScope() {
        return scope;
    }

    public Memory getSelf() {
        return self;
    }

    public Memory getOrCreateStatic(String name, Memory initValue){
        if (statics == null)
            statics = new HashMap<>();

        ReferenceMemory result = statics.get(name);
        if (result == null) {
            result = new ReferenceMemory(initValue);
            statics.put(name, result);
        }

        return result;
    }

    @Signature({
            @Arg("newThis"),
            @Arg(value = "parameters", type = HintType.VARARG, optional = @Optional("null"))
    })
    public Memory call(Environment env, Memory... args) throws Throwable {
        ParameterEntity.validateTypeHinting(env, 1, args, HintType.OBJECT, true);

        Closure newClosure = (Closure) this.clone();
        newClosure.self = args[0];
        newClosure.scope = newClosure.self.toValue(ObjectMemory.class).getReflection().getName();

        return ObjectInvokeHelper.invokeMethod(
                newClosure, newClosure.getReflection().methodMagicInvoke, env, env.trace(),
                Arrays.copyOfRange(args, 1, args.length)
        );
    }

    @Signature({@Arg("newThis"), @Arg(value = "newScope", optional = @Optional("static"))})
    public Memory bindTo(Environment env, Memory... args) throws CloneNotSupportedException {
        ParameterEntity.validateTypeHinting(env, 1, args, HintType.OBJECT, true);

        Closure newClosure = (Closure) this.clone();
        newClosure.self = args[0];
        newClosure.scope = args[1].toString();
        return new ObjectMemory(newClosure);
    }

    @Signature({
            @Arg(value = "closure", typeClass = "Closure"),
            @Arg(value = "newThis"),
            @Arg(value = "newScope", optional = @Optional("static"))
    })
    public static Memory bind(Environment env, Memory... args) throws CloneNotSupportedException {
        ParameterEntity.validateTypeHinting(env, 2, args, HintType.OBJECT, true);
        Closure closure = args[0].toObject(Closure.class);

        Closure newClosure = (Closure) closure.clone();
        newClosure.self = args[0];
        newClosure.scope = args[1].toString();
        return new ObjectMemory(newClosure);
    }

    @Signature(@Arg(value = "callable", type = HintType.CALLABLE))
    public static Memory fromCallable(Environment env, Memory... args) {
        Invoker invoker = Invoker.create(env, args[0]);

        return ObjectMemory.valueOf(new ClosureInvoker(env, invoker));
    }

    @Name("php\\lang\\ClosureInvoker")
    public static class ClosureInvoker extends Closure {
        private Invoker invoker;

        public ClosureInvoker(Environment env, ClassEntity clazz) {
            super(env, clazz);
        }

        public ClosureInvoker(Environment env, Invoker invoker) {
            super(env, env.fetchClass(ClosureInvoker.class), Memory.NULL, "", new Memory[0]);
            this.invoker = invoker;
        }

        @Signature
        private void __construct() {
        }

        @Override
        @Signature
        public Memory __invoke(Environment env, Memory... args) throws Throwable {
            return invoker.forEnvironment(env).call(args);
        }

        @Override
        public Memory getOrCreateStatic(String name) {
            return Memory.NULL;
        }
    }
}
