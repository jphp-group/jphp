package php.runtime.ext.java;

import php.runtime.Memory;
import php.runtime.env.Environment;
import php.runtime.lang.BaseException;
import php.runtime.memory.ObjectMemory;
import php.runtime.memory.StringMemory;
import php.runtime.reflection.ClassEntity;

import static php.runtime.annotation.Reflection.*;

@Name("php\\lang\\JavaException")
public class JavaException extends BaseException {
    protected Throwable throwable;

    public JavaException(Environment env, ClassEntity clazz) {
        super(env, clazz);
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
        clazz.setProperty(this, "message", new StringMemory(throwable.toString()));
    }

    @Signature
    public Memory getJavaException(Environment env, Memory... args){
        return new ObjectMemory(JavaObject.of(env, throwable));
    }
}
