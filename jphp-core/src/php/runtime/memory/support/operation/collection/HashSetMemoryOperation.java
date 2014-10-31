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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HashSetMemoryOperation extends SetMemoryOperation {

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[] { HashSet.class };
    }

    @Override
    protected Set createTreeSet() {
        return new HashSet();
    }
}
