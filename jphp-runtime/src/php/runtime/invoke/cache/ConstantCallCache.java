package php.runtime.invoke.cache;

import php.runtime.reflection.ConstantEntity;

public class ConstantCallCache extends CallCache<ConstantEntity> {
    @Override
    public Item[] newArrayData(int length) {
        return new Item[length];
    }

    @Override
    public Item[][] newArrayArrayData(int length) {
        return new Item[length][];
    }
}
