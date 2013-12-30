package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import static ru.regenix.jphp.runtime.annotation.Reflection.Name;

@Name("stdClass")
public class StdClass extends BaseObject {
    public StdClass(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public StdClass(Environment env){
        super(env, env.scope.stdClassEntity);
    }
}
