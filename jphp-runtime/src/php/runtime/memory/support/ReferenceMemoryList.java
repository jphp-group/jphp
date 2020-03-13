package php.runtime.memory.support;

import php.runtime.memory.ReferenceMemory;

import java.util.ArrayList;

public class ReferenceMemoryList extends ArrayList<ReferenceMemory> {
    public ReferenceMemoryList(int initialCapacity) {
        super(initialCapacity);
    }

    public ReferenceMemoryList() {
        super();
    }
}
