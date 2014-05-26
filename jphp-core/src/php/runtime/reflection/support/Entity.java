package php.runtime.reflection.support;

import php.runtime.common.StringUtils;
import php.runtime.env.Context;
import php.runtime.env.TraceInfo;

import java.lang.reflect.InvocationTargetException;

abstract public class Entity {
    protected Context context;
    protected TraceInfo trace;
    protected String name;
    protected String lowerName;

    protected String shortName;
    protected String namespaceName;

    protected String internalName;

    protected byte[] data;

    public Entity(Context context) {
        this.context = context;
    }

    public boolean isLoaded(){
        return data != null;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public TraceInfo getTrace() {
        return trace;
    }

    public void setTrace(TraceInfo trace) {
        this.trace = trace;
    }

    public String getInternalName() {
        return internalName;
    }

    public void setInternalName(String internalName) {
        this.internalName = internalName;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getName() {
        return name;
    }

    public String getLowerName() {
        return lowerName;
    }

    public String getShortName() {
        return shortName;
    }

    public String getNamespaceName() {
        return namespaceName;
    }

    public void setName(String name) {
        this.name = name;
        this.lowerName = name.toLowerCase();

        String[] tmp = StringUtils.split(name, '\\');
        if (tmp.length == 0)
            this.shortName = name;
        else
            this.shortName = tmp[tmp.length - 1];

        if (tmp.length > 1)
            this.namespaceName = StringUtils.join(tmp, '\\', 0, tmp.length - 1);
    }

    public boolean isNamespace(){
        return this.namespaceName != null && !this.namespaceName.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        return !(hashCode() != entity.hashCode() || !lowerName.equals(entity.lowerName));
    }

    @Override
    public int hashCode() {
        return lowerName.hashCode();
    }

    public int getPointer(){
        return super.hashCode();
    }

    protected static Throwable getCause(InvocationTargetException e){
        while (e.getTargetException() instanceof InvocationTargetException){
            e = (InvocationTargetException)e.getTargetException();
        }
        return e.getCause();
    }
}
