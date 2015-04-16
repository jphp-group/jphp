package php.runtime.memory.support.operation.array;

import php.runtime.Memory;
import php.runtime.common.HintType;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.memory.ArrayMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

public class CharArrayMemoryOperation extends MemoryOperation<char[]> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { char[].class };
    }

    @Override
    public char[] convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        char[] result = new char[((ArrayMemory)arg).size()];

        int i = 0;
        for (Memory el : (ArrayMemory)arg) {
            result[i++] = el.toChar();
        }

        return result;
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, char[] arg) throws Throwable {
        return ArrayMemory.ofChars(arg).toConstant();
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setType(HintType.ARRAY);
    }
}
