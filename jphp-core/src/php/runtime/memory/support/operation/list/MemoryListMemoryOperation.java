package php.runtime.memory.support.operation.list;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MemoryListMemoryOperation extends MemoryOperation<List<Memory>> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { List.class};
    }

    @Override
    public Type[] getGenericTypes() {
        return new Type[]{ Memory.class };
    }

    @Override
    public List<Memory> convert(Environment env, TraceInfo trace, Memory arg) {
        ForeachIterator iterator = arg.getNewIterator(env);
        List<Memory> result = new ArrayList<Memory>();

        while (iterator.next()) {
            result.add(iterator.getValue().toImmutable());
        }

        return result;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, List<Memory> arg) {
        ArrayMemory result = new ArrayMemory();
        for (Memory el : arg) {
            result.add(el.toImmutable());
        }

        return result.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
