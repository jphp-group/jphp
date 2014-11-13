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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListMemoryOperation extends GenericMemoryOperation<List> {
    public ListMemoryOperation(Type... genericTypes) {
        super(genericTypes);

        if (genericTypes == null) {
            operations = new MemoryOperation[] { MemoryOperation.get(Memory.class, null) };
        }
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { List.class, ArrayList.class, Collection.class };
    }

    @Override
    @SuppressWarnings("unchecked")
    public List convert(Environment env, TraceInfo trace, Memory arg) {
        List result = new ArrayList();

        for (Memory el : arg.getNewIterator(env)) {
            result.add(operations[0].convert(env, trace, el));
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Memory unconvert(Environment env, TraceInfo trace, List arg) {
        ArrayMemory result = new ArrayMemory();
        for (Object el : arg) {
            result.add(operations[0].unconvert(env, trace, el));
        }

        return result.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
