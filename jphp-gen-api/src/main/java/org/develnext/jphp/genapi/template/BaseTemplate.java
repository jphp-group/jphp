package org.develnext.jphp.genapi.template;

import org.develnext.jphp.genapi.description.*;

import java.io.File;
import java.util.Collection;

abstract public class BaseTemplate {
    protected final StringBuilder sb = new StringBuilder();
    protected final String language;
    protected final String languageName;

    public BaseTemplate(String language, String languageName) {
        this.language = language;
        this.languageName = languageName;
    }

    protected String subString(String s, int offset, int maxLen) {
        int len = s.length();
        if (offset + maxLen > len)
            maxLen = len;

        return s.substring(offset, offset + maxLen);
    }

    protected String getDescription(String source, String lang) {
        StringBuilder origin = new StringBuilder();
        StringBuilder translated = null;
        boolean breakOrigin = false;
        for(int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);

            if (ch == '-' && i + 6 < source.length() + 1 && subString(source, i, 6).matches("^--[A-Za-z]{2}--$")) {
                if (translated != null)
                    break;

                String name = source.substring(i + 2, i + 4);
                if (name.equalsIgnoreCase(lang))
                    translated = new StringBuilder();
                else
                    breakOrigin = true;
                i += 6;
            } else {
                if (translated == null) {
                    if (!breakOrigin)
                        origin.append(source.charAt(i));
                } else
                    translated.append(source.charAt(i));
            }
        }

        return translated != null ? translated.toString().trim() : origin.toString().trim();
    }

    public String printClass(ClassDescription description) {
        sb.delete(0, sb.length());

        onBeforeClass(description);
        print(description);
        onAfterClass(description);

        if (!description.getConstants().isEmpty()) {
            onBeforeConstants(description);

            for(ConstantDescription el : description.getConstants()) {
                onBeforeConstant(el);
                print(el);
                onAfterConstant(el);
            }

            onAfterConstants(description);
        }

        if (!description.getProperties().isEmpty()) {
            onBeforeProperties(description);

            for(PropertyDescription el : description.getProperties()) {
                onBeforeProperty(el);
                print(el);
                onAfterProperty(el);
            }

            onAfterProperties(description);
        }

        if (!description.getMethods().isEmpty()) {
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
        }

        onAfterClassBody(description);
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

    protected void onBeforeConstants(ClassDescription desc) {}
    protected void onAfterConstants(ClassDescription desc) {}
    protected void onBeforeConstant(ConstantDescription desc) { }
    protected void onAfterConstant(ConstantDescription desc) { }

    protected void onBeforeProperties(ClassDescription desc) { }
    protected void onAfterProperties(ClassDescription desc) { }
    protected void onBeforeProperty(PropertyDescription desc) { }
    protected void onAfterProperty(PropertyDescription desc) { }

    protected void onBeforeMethods(ClassDescription desc) { }
    protected void onAfterMethods(ClassDescription desc) { }
    protected void onBeforeMethod(MethodDescription desc) { }
    protected void onAfterMethod(MethodDescription desc) { }

    protected void onAfterClassBody(ClassDescription desc) { }

    protected void onBeforeArgument(ArgumentDescription desc, FunctionDescription function) { }
    protected void onAfterArgument(ArgumentDescription desc, FunctionDescription function) { }

    abstract protected void print(ClassDescription description);
    abstract protected void print(ConstantDescription description);
    abstract protected void print(PropertyDescription description);
    abstract protected void print(MethodDescription description);
    abstract protected void print(FunctionDescription description);
    abstract protected void print(ArgumentDescription description);

    public void onEnd(File targetDirectory) { }
}
