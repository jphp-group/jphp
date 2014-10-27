package php.runtime.memory.support.operation.list;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.util.WrapRegex;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class StringListMemoryOperation extends MemoryOperation<List<String>> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { List.class};
    }

    @Override
    public Type[] getGenericTypes() {
        return new Type[]{ String.class };
    }

    @Override
    public List<String> convert(Environment env, TraceInfo trace, Memory arg) {
        ForeachIterator iterator = arg.getNewIterator(env);
        List<String> result = new ArrayList<String>();

        if (iterator == null) {
            return null;
        }

        while (iterator.next()) {
            result.add(iterator.getValue().toString());
        }

        return result;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, List<String> arg) {
        ArrayMemory result = new ArrayMemory();
        for (String el : arg) {
            result.add(el);
        }

        return result.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.TRAVERSABLE);
    }
}
