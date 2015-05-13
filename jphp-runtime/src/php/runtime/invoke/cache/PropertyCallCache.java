package php.runtime.invoke.cache;

import php.runtime.reflection.PropertyEntity;

public class PropertyCallCache extends CallCache<PropertyEntity> {
    @Override
    public Item[] newArrayData(int length) {
        return new Item[length];
    }

    @Override
    public Item[][] newArrayArrayData(int length) {
        return new Item[length][];
    }
}
