package php.runtime.invoke.cache;

import php.runtime.env.CompileScope;
import php.runtime.reflection.support.Entity;

abstract public class CallCache<T extends Entity> {
    protected T[][] cache;

    public T get(CompileScope scope, int index) {
        if (cache == null) {
            return null;
        }

        T[] data = scope.id < cache.length ? cache[scope.id] : null;
        if (data == null)
            return null;

        if (index >= data.length)
            return null;

        return data[index];
    }

    abstract public T[] newArrayData(int length);
    abstract public T[][] newArrayArrayData(int length);

    @SuppressWarnings("unchecked")
    public void put(CompileScope scope, int index, T entity) {
        int id = scope.id;
        if (cache == null) {
            cache = newArrayArrayData(id + 1);
        }

        if (id >= cache.length) {
            synchronized (cache) {
                T[][] newCache = newArrayArrayData(id + 1);
                System.arraycopy(cache, 0, newCache, 0, cache.length);
                cache = newCache;
            }
        }

        T[] data = cache[id];

        if (data == null) {
            cache[id] = data = newArrayData(index + 1);
        } else {
            if (index >= data.length) {
                synchronized (cache) {
                    T[] newData = newArrayData(index + 1);
                    System.arraycopy(data, 0, newData, 0, data.length);
                    cache[id] = data = newData;
                }
            }
        }

        data[index] = entity;
    }
}
