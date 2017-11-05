package php.runtime.memory.support.operation.collection;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.operation.GenericMemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class EnumerationMemoryOperation extends GenericMemoryOperation<Enumeration> {
    public EnumerationMemoryOperation(Type... genericTypes) {
        super(genericTypes);

        if (genericTypes == null) {
            operations = new MemoryOperation[] { MemoryOperation.get(Memory.class, null) };
        }
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] {
                Enumeration.class
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Enumeration convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        Vector result = new Vector();

        for (Memory el : arg.getNewIterator(env)) {
            result.add(operations[0].convert(env, trace, el));
        }

        return result.elements();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Memory unconvert(Environment env, TraceInfo trace, Enumeration arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        ArrayMemory result = new ArrayMemory();
        while (arg.hasMoreElements()) {
            result.add(operations[0].unconvert(env, trace, arg.nextElement()));
        }

        return result.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
