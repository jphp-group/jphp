package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.ext.support.Extension;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;

public class WrapCompileMethodEntity extends CompileMethodEntity {
    public WrapCompileMethodEntity(Extension extension) {
        super(extension);
    }

    @Override
    public Memory invokeDynamic(IObject _this, Environment env, TraceInfo trace, Memory... arguments) throws Throwable {
        BaseWrapper aThis = (BaseWrapper) _this;
        return super.invokeDynamic(aThis == null ? null : aThis.getWrappedObject(), env, trace, arguments);
    }
}
