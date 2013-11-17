package ru.regenix.jphp.runtime.reflection;

import org.apache.commons.lang3.StringUtils;
import ru.regenix.jphp.runtime.env.Context;

abstract public class Entity {
    protected Context context;
    protected String name;
    protected String lowerName;

    protected String shortName;
    protected String namespaceName;

    public Entity(Context context) {
        this.context = context;
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

        if (!lowerName.equals(entity.lowerName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return lowerName.hashCode();
    }
}
