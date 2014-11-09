package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ArrayMemoryOperation extends MemoryOperation {
    protected final MemoryOperation operation;
    protected final Class<?> arrayElementClass;
    protected final Class<?> arrayClass;

    public ArrayMemoryOperation(Class<?> arrayClass) {
        this.operation = MemoryOperation.get(this.arrayElementClass = arrayClass.getComponentType(), null);
        this.arrayClass = arrayClass;
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { arrayClass };
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object convert(Environment env, TraceInfo trace, Memory arg) {
        ForeachIterator iterator = arg.getNewIterator(env);
        if (iterator == null) {
            return null;
        }

        List tmp = new ArrayList();
        while (iterator.next()) {
            tmp.add(operation.convert(env, trace, iterator.getValue()));
        }

        Object[] r = (Object[]) Array.newInstance(arrayElementClass, tmp.size());
        for (int i = 0; i < r.length; i++) {
            r[i] = tmp.get(i);
        }

        return arrayClass.cast(r);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Memory unconvert(Environment env, TraceInfo trace, Object arg) {
        ArrayMemory r = new ArrayMemory();
        for (Object el : (Object[]) arg) {
            r.add(operation.unconvert(env, trace, el));
        }

        return r.toConstant();
    }
}
