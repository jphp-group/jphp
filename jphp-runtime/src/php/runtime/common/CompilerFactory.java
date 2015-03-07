package php.runtime.common;

import php.runtime.env.Context;
import php.runtime.env.Environment;

import java.lang.reflect.InvocationTargetException;

abstract public class CompilerFactory {
    abstract public AbstractCompiler getCompiler(Environment env, Context context) throws Throwable;
}
