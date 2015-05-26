package php.runtime.invoke.cache;

import php.runtime.env.Environment;
import php.runtime.reflection.support.Entity;

import java.lang.ref.WeakReference;

abstract public class CallCache<T extends Entity> {
    protected Item[][] cache;

    public T get(Environment env, int index) {
        if (cache == null) {
            return null;
        }

        Item[] data = env.id < cache.length ? cache[env.id] : null;
        if (data == null)
            return null;

        if (index >= data.length)
            return null;

        Item item = data[index];
        if (item == null) {
            return null;
        }

        if (item.env.get() != env) {
            data[index] = null;
            return null;
        }

        return (T) item.data;
    }

    abstract public Item[] newArrayData(int length);
    abstract public Item[][] newArrayArrayData(int length);

    @SuppressWarnings("unchecked")
    synchronized public void put(Environment env, int index, T entity) {
        int id = env.id;

        if (cache == null) {
            cache = newArrayArrayData(id + 1);
        }

        if (id >= cache.length) {
            Item[][] newCache = newArrayArrayData(id + 1);
            System.arraycopy(cache, 0, newCache, 0, cache.length);
            cache = newCache;
        }

        Item[] data = cache[id];

        if (data == null) {
            cache[id] = data = newArrayData(index + 1 + 10);
        } else {
            if (index >= data.length) {
                Item[] newData = newArrayData(index + 1 + 10);
                System.arraycopy(data, 0, newData, 0, data.length);
                cache[id] = data = newData;
            }
        }

        data[index] = new Item(entity, env);
    }

    protected static class Item {
        Entity data;
        WeakReference<Environment> env;

        public Item(Entity data, Environment env) {
            this.data = data;
            this.env = new WeakReference<Environment>(env);
        }
    }
}
