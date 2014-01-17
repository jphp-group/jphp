package php.runtime.lang;


import php.runtime.annotation.Reflection;
import php.runtime.Memory;

@Reflection.Ignore
public interface IStaticVariables {
    Memory getStatic(String name);
    Memory getOrCreateStatic(String name);
}
