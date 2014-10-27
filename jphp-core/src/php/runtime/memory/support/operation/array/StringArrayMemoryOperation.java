package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.util.ArrayList;
import java.util.List;

public class StringArrayMemoryOperation extends MemoryOperation<String[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { String[].class };
    }

    @Override
    public String[] convert(Environment env, TraceInfo trace, Memory arg) {
        if (arg.isArray()) {
            return arg.toValue(ArrayMemory.class).toStringArray();
        } else {
            ForeachIterator iterator = arg.getNewIterator(env);
            if (iterator == null) {
                return null;
            }

            List<String> tmp = new ArrayList<String>();
            while (iterator.next()) {
                tmp.add(iterator.getValue().toString());
            }

            return tmp.toArray(new String[tmp.size()]);
        }
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, String[] arg) {
        ArrayMemory r = new ArrayMemory(arg);
        return r.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
