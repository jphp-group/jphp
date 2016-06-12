package org.develnext.jphp.core.syntax;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

public class ExpressionInfo {
    protected Set<String> types;

    public ExpressionInfo() {
        types = new TreeSet<>();
    }

    public boolean addType(String type) {
        return types.add(type);
    }

    public boolean addTypes(Collection<String> types) {
        return this.types.addAll(types);
    }

    public Set<String> getTypes() {
        return types;
    }
}
