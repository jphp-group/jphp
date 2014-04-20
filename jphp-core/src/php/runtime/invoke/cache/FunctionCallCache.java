package php.runtime.invoke.cache;

import php.runtime.reflection.FunctionEntity;

public class FunctionCallCache extends CallCache<FunctionEntity> {

    @Override
    public FunctionEntity[] newArrayData(int length) {
        return new FunctionEntity[length];
    }

    @Override
    public FunctionEntity[][] newArrayArrayData(int length) {
        return new FunctionEntity[length][];
    }
}
