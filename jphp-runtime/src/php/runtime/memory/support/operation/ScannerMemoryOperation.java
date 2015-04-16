package php.runtime.memory.support.operation;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.core.classes.util.WrapScanner;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.support.MemoryOperation;
import php.runtime.reflection.ParameterEntity;

import java.util.Scanner;

public class ScannerMemoryOperation extends MemoryOperation<Scanner> {
    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { Scanner.class };
    }

    @Override
    public Scanner convert(Environment env, TraceInfo trace, Memory arg) throws Throwable {
        if (arg.isNull()) {
            return null;
        }

        return arg.toObject(WrapScanner.class).getScanner();
    }

    @Override
    public Memory unconvert(Environment env, TraceInfo trace, Scanner arg) throws Throwable {
        if (arg == null) {
            return Memory.NULL;
        }

        return ObjectMemory.valueOf(new WrapScanner(env, arg));
    }

    @Override
    public void applyTypeHinting(ParameterEntity parameter) {
        parameter.setTypeNativeClass(WrapScanner.class);
    }
}
