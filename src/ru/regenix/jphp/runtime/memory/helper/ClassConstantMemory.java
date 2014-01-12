package ru.regenix.jphp.runtime.memory.helper;

import ru.regenix.jphp.common.Messages;
import ru.regenix.jphp.exceptions.support.ErrorType;
import ru.regenix.jphp.runtime.env.Environment;
import ru.regenix.jphp.runtime.env.TraceInfo;
import ru.regenix.jphp.runtime.memory.ReferenceMemory;
import ru.regenix.jphp.runtime.memory.support.Memory;
import ru.regenix.jphp.runtime.reflection.ClassEntity;
import ru.regenix.jphp.runtime.reflection.ConstantEntity;

public class ClassConstantMemory extends ReferenceMemory {
    private final String name;
    private final String lowerName;

    private final String className;
    private final String classLowerName;

    public ClassConstantMemory(String className, String name){
        super();
        this.className = className;
        this.classLowerName = className.toLowerCase();
        this.name = name;
        this.lowerName = name.toLowerCase();
    }

    @Override
    public Memory toImmutable(Environment env, TraceInfo trace) {
        ClassEntity classEntity = env.fetchClass(className, classLowerName, true);
        if (classEntity == null) {
            env.error(trace, ErrorType.E_ERROR, Messages.ERR_CLASS_NOT_FOUND, className);
            return NULL;
        }

        ConstantEntity entity = classEntity.findConstant(name);
        if (entity == null){
            env.error(trace, ErrorType.E_ERROR, Messages.ERR_UNDEFINED_CLASS_CONSTANT, className + "::" + name);
            return NULL;
        }

        return entity.getValue();
    }
}
