package php.runtime.memory.support.operation.map;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapMemoryOperation extends MapMemoryOperation {
    public LinkedHashMapMemoryOperation(Type... genericTypes) {
        super(genericTypes);
    }

    @Override
    public Class<?>[] getOperationClasses() {
        return new Class<?>[]{LinkedHashMap.class};
    }

    @Override
    protected Map createHashMap() {
        return new LinkedHashMap();
    }
}
