package php.runtime.env;

import php.runtime.reflection.MethodEntity;
import php.runtime.reflection.support.AbstractFunctionEntity;

public class TraceInfoCallCache {
    public Object self;
    public AbstractFunctionEntity callEntity;

    public TraceInfoCallCache() {
    }

    public TraceInfoCallCache(Object self, AbstractFunctionEntity callEntity) {
        this.self = self;
        this.callEntity = callEntity;
    }
}
