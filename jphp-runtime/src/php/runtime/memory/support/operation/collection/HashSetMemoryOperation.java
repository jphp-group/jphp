package php.runtime.memory.support.operation.collection;

import java.util.HashSet;
import java.util.Set;

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
