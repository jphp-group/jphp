package php.runtime.lang.support;

import php.runtime.env.Environment;
import php.runtime.env.TraceInfo;
import php.runtime.lang.IObject;

import static php.runtime.annotation.Reflection.Ignore;

@Ignore
public interface ICloneableObject<T extends IObject> {
    T __clone(Environment env, TraceInfo trace);
}
