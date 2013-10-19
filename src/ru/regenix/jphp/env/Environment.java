package ru.regenix.jphp.env;

import java.util.HashSet;
import java.util.Set;

public class Environment {
    private Set<String> includePaths;

    public Environment() {
        this.includePaths = new HashSet<String>();
    }

    public Set<String> getIncludePaths() {
        return includePaths;
    }

    public void addIncludePath(String value){
        includePaths.add(value);
    }

    public void setIncludePaths(Set<String> includePaths) {
        this.includePaths = includePaths;
    }
}
