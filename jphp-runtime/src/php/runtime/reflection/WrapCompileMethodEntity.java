package php.runtime.reflection;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.ext.support.Extension;
import php.runtime.lang.BaseWrapper;
import php.runtime.lang.IObject;

public class WrapCompileMethodEntity extends CompileMethodEntity {
    public WrapCompileMethodEntity(Extension extension) {
        super(extension);
    }

    @Override
    public Memory invokeDynamic(IObject _this, Environment env, Memory... arguments) throws Throwable {
        return super.invokeDynamic(((BaseWrapper)_this).getWrappedObject(), env, arguments);
    }
}
