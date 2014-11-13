package php.runtime.memory.support.operation.map;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HashMapMemoryOperation extends MapMemoryOperation {
    public HashMapMemoryOperation(Type... genericTypes) {
        super(genericTypes);
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[]{HashMap.class};
    }

    @Override
    protected Map createHashMap() {
        return new HashMap();
    }
}
