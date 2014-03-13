package php.runtime.common;

import java.util.HashSet;
import java.util.Set;

final public class CollectionUtils {
    private CollectionUtils() {}

    public static <T> Set<T> newSet(T... args) {
        Set<T> r = new HashSet<T>();
        for(T e : args)
            r.add(e);
        return r;
    }
}
