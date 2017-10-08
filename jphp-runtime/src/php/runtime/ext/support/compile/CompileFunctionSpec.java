package php.runtime.ext.support.compile;

import php.runtime.ext.support.Extension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CompileFunctionSpec {
    protected String name;
    protected boolean asImmutable;
    protected String lowerName;
    protected List<Method> methods = new LinkedList<>();

    public CompileFunctionSpec(String name) {
        this(name, false);
    }

    public CompileFunctionSpec(String name, boolean asImmutable) {
        this.name = name;
        this.asImmutable = asImmutable;
        this.lowerName = name.toLowerCase();
    }

    public void addMethod(Method method) {
        methods.add(method);
    }

    public String getName() {
        return name;
    }

    public String getLowerName() {
        return lowerName;
    }

    public CompileFunction toFunction() {
        CompileFunction function = new CompileFunction(name);

        for (Method method : methods) {
            function.addMethod(method, asImmutable);
        }

        return function;
    }
}
