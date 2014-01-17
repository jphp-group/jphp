package php.runtime.lang;

import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.lib.collections.map.HashedMap;
import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.memory.ReferenceMemory;
import php.runtime.Memory;
import php.runtime.reflection.ClassEntity;

import java.util.Map;

public abstract class Closure extends BaseObject implements IStaticVariables {
    protected Memory[] uses;
    private Map<String, ReferenceMemory> statics;
    protected Memory self = Memory.NULL;
    protected String scope = null;

    public Closure(ClassEntity closure, Memory self, Memory[] uses) {
        super(closure);
        this.self = self;
        this.uses = uses;
    }

    @Reflection.Signature
    abstract public Memory __invoke(Environment env, Memory... args);

    public Memory[] getUses() {
        return uses == null ? new Memory[0] : uses;
    }

    @Reflection.Signature({@Reflection.Arg("prop"), @Reflection.Arg("value")})
    public Memory __set(Environment env, Memory... args){
        env.error(ErrorType.E_ERROR, "Closure object cannot have properties");
        return Memory.NULL;
    }

    @Reflection.Signature({@Reflection.Arg("prop")})
    public Memory __get(Environment env, Memory... args){
        return __set(env, args);
    }

    @Reflection.Signature({@Reflection.Arg("prop")})
    public Memory __unset(Environment env, Memory... args){
        return __set(env, args);
    }

    @Reflection.Signature({@Reflection.Arg("prop")})
    public Memory __isset(Environment env, Memory... args){
        return __set(env, args);
    }

    @Override
    public Memory getStatic(String name){
        if (statics == null)
            return null;

        return statics.get(name);
    }

    public Memory getOrCreateStatic(String name, Memory initValue){
        if (statics == null)
            statics = new HashedMap<String, ReferenceMemory>();

        ReferenceMemory result = statics.get(name);
        if (result == null) {
            result = new ReferenceMemory(initValue);
            statics.put(name, result);
        }
        return result;
    }
}
