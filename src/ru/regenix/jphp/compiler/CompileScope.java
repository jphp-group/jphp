package ru.regenix.jphp.compiler;

import ru.regenix.jphp.compiler.common.Extension;
import ru.regenix.jphp.compiler.common.compile.*;
import ru.regenix.jphp.compiler.jvm.runtime.invokedynamic.CallableValue;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompileScope {

    protected Map<String, ClassStmtToken> classes;
    protected Map<String, MethodStmtToken> methods;
    protected Map<String, Extension> extensions;

    protected Map<String, CompileConstant> constants;
    protected Map<String, CompileFunction> functions;

    private static Map<Class, Map<String, CallableValue>> classMethods =
            new HashMap<Class, Map<String, CallableValue>>();


    public CompileScope() {
        classes = new HashMap<String, ClassStmtToken>();
        methods = new HashMap<String, MethodStmtToken>();
        extensions = new LinkedHashMap<String, Extension>();

        constants = new HashMap<String, CompileConstant>();
        functions = new HashMap<String, CompileFunction>();
    }

    public void registerExtension(Extension extension){
        extension.onRegister(this);
        constants.putAll(extension.getConstants());
        functions.putAll(extension.getFunctions());
        extensions.put(extension.getName(), extension);
    }

    public int addClass(ClassStmtToken clazz){
        String name = clazz.getFulledName().toLowerCase();
        classes.put(name, clazz);
        return classes.size() - 1;
    }

    public int addMethod(MethodStmtToken method){
        String name = (method.getClazz().getFulledName() + "@" + method.getName().getName())
                .toLowerCase();

        methods.put(name, method);
        return methods.size() - 1;
    }

    public int addMethod(ClassStmtToken clazz, String name){
        methods.put(clazz.getFulledName() + "@" + name, null);
        return methods.size() - 1;
    }

    public CompileConstant findConstant(String name){
        return constants.get(name);
    }

    public CompileFunction findFunction(String name){
        return functions.get(name.toLowerCase());
    }

    public static void registerClass(Class clazz){
        Map<String, CallableValue> map = new HashMap<String, CallableValue>();
        for (Method method : clazz.getMethods()){
            map.put(method.getName(), new CallableValue(method));
        }
        classMethods.put(clazz, map);
    }

    public static CallableValue findMethod(Class clazz, String methodName){
        return classMethods.get(clazz).get(methodName);
    }
}
