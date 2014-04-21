package org.develnext.jphp.genapi.template;

import org.develnext.jphp.genapi.description.ArgumentDescription;
import org.develnext.jphp.genapi.description.ClassDescription;
import org.develnext.jphp.genapi.description.FunctionDescription;
import org.develnext.jphp.genapi.description.MethodDescription;

import java.io.File;
import java.util.Collection;

abstract public class BaseTemplate {
    protected final StringBuilder sb = new StringBuilder();

    public String printClass(ClassDescription description) {
        sb.delete(0, sb.length());

        onBeforeClass(description);
        print(description);
        onAfterClass(description);

        onBeforeMethods(description);
        for (MethodDescription el : description.getMethods()) {
            onBeforeMethod(el);
            print(el);

            for(ArgumentDescription arg : el.getArguments()) {
                onBeforeArgument(arg, el);
                print(arg);
                onAfterArgument(arg, el);
            }

            onAfterMethod(el);
        }
        onAfterMethods(description);

        return sb.toString();
    }

    protected String printFunction(FunctionDescription description) {
        sb.delete(0, sb.length());

        onBeforeFunction(description);
        print(description);

        Collection<ArgumentDescription> args = description.getArguments();
        for(ArgumentDescription arg : args) {
            onBeforeArgument(arg, description);
            print(arg);
            onAfterArgument(arg, description);
        }

        onAfterFunction(description);
        return sb.toString();
    }

    protected void onBeforeClass(ClassDescription desc) { }
    protected void onAfterClass(ClassDescription desc) {  }

    protected void onBeforeFunction(FunctionDescription desc) { }
    protected void onAfterFunction(FunctionDescription desc) { }

    protected void onBeforeMethods(ClassDescription desc) { }
    protected void onAfterMethods(ClassDescription desc) { }
    protected void onBeforeMethod(MethodDescription desc) { }
    protected void onAfterMethod(MethodDescription desc) { }

    protected void onBeforeArgument(ArgumentDescription desc, FunctionDescription function) { }
    protected void onAfterArgument(ArgumentDescription desc, FunctionDescription function) { }

    abstract protected void print(ClassDescription description);
    abstract protected void print(MethodDescription description);
    abstract protected void print(FunctionDescription description);
    abstract protected void print(ArgumentDescription description);

    public void onEnd(File targetDirectory) { }
}
