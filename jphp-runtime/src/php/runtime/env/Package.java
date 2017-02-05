package php.runtime.env;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Package {
    private final String name;
    private Set<String> classes;
    private Set<String> functions;
    private Set<String> constants;

    public Package(String name) {
        this.name = name;
        this.classes = new HashSet<>();
        this.functions = new HashSet<>();
        this.constants = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    synchronized public boolean addClass(String className) {
        return classes.add(className);
    }

    synchronized public boolean addFunction(String functionName) {
        return functions.add(functionName);
    }

    synchronized public boolean addConstant(String constantName) {
        return constants.add(constantName);
    }

    public Package duplicate() {
        Package aPackage = new Package(name);
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
}
