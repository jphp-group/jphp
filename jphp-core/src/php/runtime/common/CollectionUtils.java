package php.runtime.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

final public class CollectionUtils {
    private CollectionUtils() {}

    public static <T> Set<T> newSet(T... args) {
        Set<T> r = new HashSet<T>();
        Collections.addAll(r, args);
        return r;
    }
}
