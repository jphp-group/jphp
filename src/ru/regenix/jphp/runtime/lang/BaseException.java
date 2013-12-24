package ru.regenix.jphp.runtime.lang;

import ru.regenix.jphp.common.HintType;
import ru.regenix.jphp.common.Modifier;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.memory.ArrayMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;

import static ru.regenix.jphp.runtime.annotation.Reflection.*;


@Name("Exception")
@Signature({
       @Arg(value = "message", modifier = Modifier.PROTECTED, type = HintType.STRING),
       @Arg(value = "code", modifier = Modifier.PROTECTED, type = HintType.NUMERIC),
       @Arg(value = "previous", modifier = Modifier.PROTECTED, type = HintType.OBJECT),
       @Arg(value = "line", modifier = Modifier.PROTECTED, type = HintType.NUMERIC)
})
public class BaseException implements IObject {
    public final ArrayMemory __dynamicProperties__;
    public final ClassEntity __class__;

    public BaseException(Environment env, ClassEntity clazz) {
        this.__class__ = clazz;
        this.__dynamicProperties__ = new ArrayMemory();
    }

    @Signature({
            @Arg(value = "message", optional = @Optional(value = "", type = HintType.STRING)),
            @Arg(value = "code", optional = @Optional(value = "0", type = HintType.NUMERIC)),
            @Arg(value = "previous", optional = @Optional(value = "NULL"))
    })
    public Memory __construct(Environment env, Memory... args) {
        __dynamicProperties__.refOfIndex("message").assign(args[0].toString());
        __dynamicProperties__.refOfIndex("code").assign(args[1].toLong());
        __dynamicProperties__.refOfIndex("previous").assign(args[2]);
        return Memory.NULL;
    }

    @Override
    public ClassEntity getReflection() {
        return __class__;
    }

    @Override
    public ArrayMemory getProperties() {
        return __dynamicProperties__;
    }

    @Override
    final public int getPointer() {
        return super.hashCode();
    }
}
