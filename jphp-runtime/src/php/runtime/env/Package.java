package php.runtime.env;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Package {
    private Set<String> classes;
    private Set<String> functions;
    private Set<String> constants;

    public Package() {
        this.classes = new HashSet<>();
        this.functions = new HashSet<>();
        this.constants = new HashSet<>();
    }

    public boolean addClass(String className) {
        return classes.add(className);
    }

    public boolean addFunction(String functionName) {
        return functions.add(functionName);
    }

    public boolean addConstant(String constantName) {
        return constants.add(constantName);
    }

    public Package duplicate() {
        Package aPackage = new Package();
        aPackage.classes = new HashSet<>(classes);
        aPackage.functions = new HashSet<>(functions);
        aPackage.constants = new HashSet<>(constants);
        return aPackage;
    }

    public Collection<String> getClasses() {
        return classes;
    }

    public Collection<String> getFunctions() {
        return functions;
    }

    public Collection<String> getConstants() {
        return constants;
    }

    public boolean hasClass(String typeName) {
        return classes.contains(typeName);
    }

    public boolean hasFunction(String name) {
        return functions.contains(name);
    }

    public boolean hasConstant(String name) {
        return constants.contains(name);
    }
}
