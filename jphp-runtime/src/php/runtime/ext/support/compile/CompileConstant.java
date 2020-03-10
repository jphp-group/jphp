package php.runtime.ext.support.compile;

import php.runtime.Memory;

public class CompileConstant {
    public final String name;
    public final Memory value;
    public final boolean dynamicly;

    public CompileConstant(String name, Memory value, boolean dynamicly) {
        this.name = name;
        this.value = value;
        this.dynamicly = dynamicly;
    }

    public CompileConstant(String name, Object value, boolean dynamicly){
        this(name, Memory.wrap(null, value), dynamicly);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompileConstant)) return false;

        CompileConstant that = (CompileConstant) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
