package php.runtime.memory;

import java.util.HashMap;
import java.util.Map;

public class UninitializedMemory extends NullMemory {
    private final String arg;

    private static Map<String, UninitializedMemory> cache = new HashMap<>();

    protected UninitializedMemory(String arg) {
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

    @Override
    public boolean isUninitialized() {
        return true;
    }

    public static UninitializedMemory valueOf(String arg) {
        return cache.computeIfAbsent(arg, UninitializedMemory::new);
    }
}
