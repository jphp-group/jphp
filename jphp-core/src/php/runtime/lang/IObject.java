package php.runtime.lang;

import php.runtime.annotation.Reflection;
import php.runtime.env.Environment;
import php.runtime.memory.ArrayMemory;
import php.runtime.reflection.ClassEntity;

@Reflection.Ignore
public interface IObject {
    ClassEntity getReflection();
    ArrayMemory getProperties();
    Environment getEnvironment();
    int getPointer();

    boolean isMock();
    void setAsMock();

    boolean isFinalized();
    void doFinalize();
}
