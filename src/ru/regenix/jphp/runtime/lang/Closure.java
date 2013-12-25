package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.exceptions.FatalException;
import ru.regenix.jphp.runtime.annotation.Reflection;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

public abstract class Closure extends BaseObject {
    protected Memory[] uses;
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
        env.triggerError(new FatalException(
                "Closure object cannot have properties",
                env.peekCall(0).trace
        ));
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
}
