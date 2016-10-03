package php.runtime.memory.support.operation.map;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.ForeachIterator;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.StringMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.util.Map;
import java.util.Properties;

public class PropertiesMemoryOperation extends MemoryOperation<Properties> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Properties.class };
    }

    @Override
    public Properties convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg == Memory.NULL) {
            return null;
        }

        Properties properties = new Properties();

        ForeachIterator iterator = arg.getNewIterator(env);

        while (iterator.next()) {
            if (iterator.getValue().isNull()) {
                properties.setProperty(iterator.getKey().toString(), null);
            } else {
                properties.setProperty(iterator.getKey().toString(), iterator.getValue().toString());
            }
        }

        return properties;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Properties arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        ArrayMemory result = new ArrayMemory(true);

        for (Map.Entry<Object, Object> entry : arg.entrySet()) {
            result.putAsKeyString(
                    entry.getKey().toString(),
                    entry.getValue() == null ? Memory.NULL : StringMemory.valueOf(entry.getValue().toString())
            );
        }

        return result.toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
