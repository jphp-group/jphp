package php.runtime.invoke.cache;

import php.runtime.reflection.FunctionEntity;

public class FunctionCallCache extends CallCache<FunctionEntity> {
    @Override
    public Item[] newArrayData(int length) {
        return new Item[length];
    }

    @Override
    public Item[][] newArrayArrayData(int length) {
        return new Item[length][];
    }
}
