package php.runtime.invoke.cache;

import php.runtime.reflection.MethodEntity;

public class MethodCallCache extends CallCache<MethodEntity> {
    @Override
    public Item[] newArrayData(int length) {
        return new Item[length];
    }

    @Override
    public Item[][] newArrayArrayData(int length) {
        return new Item[length][];
    }
}
