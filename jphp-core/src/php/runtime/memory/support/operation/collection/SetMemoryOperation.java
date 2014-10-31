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
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SetMemoryOperation extends GenericMemoryOperation<Set> {
    public SetMemoryOperation(Type... genericTypes) {
        super(genericTypes);

        if (genericTypes == null) {
            operations = new MemoryOperation[] { MemoryOperation.get(Memory.class, null) };
        }
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Set.class, TreeSet.class };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set convert(Environment env, TraceInfo trace, Memory arg) {
        Set result = createTreeSet();

        for (Memory el : arg.getNewIterator(env)) {
            result.add(operations[0].convert(env, trace, el));
        }

        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Memory unconvert(Environment env, TraceInfo trace, Set arg) {
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

    protected Set createTreeSet() {
        return new TreeSet();
    }
}
