package php.runtime.memory.support.operation.map;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.memory.support.operation.GenericMemoryOperation;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class MapMemoryOperation extends GenericMemoryOperation<Map> {
    public MapMemoryOperation(Type... genericTypes) {
        super(genericTypes);

        if (genericTypes == null) {
            operations = new MemoryOperation[]{
                    MemoryOperation.get(Memory.class, null),
                    MemoryOperation.get(Memory.class, null)
            };
        }
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[]{Map.class};
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        Map result = this.createHashMap();

        ForeachIterator iterator = arg.getNewIterator(env);

        while (iterator.next()) {
            if (operations.length == 2) {
                result.put(
                        operations[0].convert(env, trace, iterator.getMemoryKey()),
                        operations[1].convert(env, trace, iterator.getValue())
                );
            } else {
                result.put(
                        Memory.unwrap(env, iterator.getMemoryKey(), true),
                        Memory.unwrap(env, iterator.getValue(), true)
                );
            }
        }

        return result;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Map arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        ArrayMemory result = new ArrayMemory();

        for (Object _entry : arg.entrySet()) {
            Map.Entry entry = (Map.Entry) _entry;

            Memory key = operations.length > 0
                    ? operations[0].unconvert(env, trace, entry.getKey())
                    : Memory.wrap(env, entry.getKey());

            Memory value = operations.length > 1
                    ? operations[1].unconvert(env, trace, entry.getValue())
                    : Memory.wrap(env, entry.getValue());

            result.refOfIndex(key).assign(value);
        }

        return result.toConstant();
    }

    protected Map createHashMap() {
        return new LinkedHashMap();
    }
}
