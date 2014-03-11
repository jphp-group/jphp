package php.runtime.ext.support.compile;

import php.runtime.annotation.Reflection;
import php.runtime.ext.support.Extension;

public class CompileClass {
    protected final Class<?> nativeClass;
    protected String name;
    protected String lowerName;
    protected Extension extension;

    public CompileClass(Extension extension, Class<?> nativeClass) {
        this.nativeClass = nativeClass;
        this.extension = extension;

        Reflection.Name name = nativeClass.getAnnotation(Reflection.Name.class);
        this.name = name == null ? nativeClass.getSimpleName() : name.value();
        this.lowerName = this.name.toLowerCase();
    }

    public Class<?> getNativeClass() {
        return nativeClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLowerName() {
        return lowerName;
    }

    public Extension getExtension() {
        return extension;
    }
}
