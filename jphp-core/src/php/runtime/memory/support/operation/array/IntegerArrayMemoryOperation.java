package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.DoubleMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.util.ArrayList;
import java.util.List;

public class IntegerArrayMemoryOperation extends MemoryOperation {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { int[].class };
    }

    @Override
    public int[] convert(Environment env, TraceInfo trace, Memory arg) {
        if (arg.isArray()) {
            return arg.toValue(ArrayMemory.class).toIntArray();
        } else {
            ForeachIterator iterator = arg.getNewIterator(env);
            if (iterator == null) {
                return null;
            }

            List<Integer> tmp = new ArrayList<Integer>();
            while (iterator.next()) {
                tmp.add(iterator.getValue().toInteger());
            }

            int[] r = new int[tmp.size()];
            for (int i = 0; i < r.length; i++) {
                r[i] = tmp.get(i);
            }

            return r;
        }
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Object arg) {
        return unconvert(env, trace, (int[]) arg);
    }

    private Memory unconvert(Environment env, TraceInfo trace, int[] arg) {
        ArrayMemory r = new ArrayMemory();
        for (int el : arg) {
            r.add(el);
        }

        return r.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
