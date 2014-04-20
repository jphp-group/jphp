package php.runtime.invoke.cache;

import php.runtime.reflection.MethodEntity;

public class MethodCallCache extends CallCache<MethodEntity> {

    @Override
    public MethodEntity[] newArrayData(int length) {
        return new MethodEntity[length];
    }

    @Override
    public MethodEntity[][] newArrayArrayData(int length) {
        return new MethodEntity[length][];
    }
}
