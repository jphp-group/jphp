package php.runtime.lang.support;


import php.runtime.Memory;
import php.runtime.annotation.Reflection;

@Reflection.Ignore
public interface IStaticVariables {
    Memory getStatic(String name);
    Memory getOrCreateStatic(String name);
}
