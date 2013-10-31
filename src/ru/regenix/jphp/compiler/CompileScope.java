package ru.regenix.jphp.compiler;


import ru.regenix.jphp.compiler.jvm.runtime.invokedynamic.CallableValue;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CompileScope {

    protected Map<String, ClassStmtToken> classes;
    protected Map<String, MethodStmtToken> methods;

    private static Map<Class, Map<String, CallableValue>> classMethods =
            new HashMap<Class, Map<String, CallableValue>>();


    public CompileScope() {
        classes = new HashMap<String, ClassStmtToken>();
        methods = new HashMap<String, MethodStmtToken>();
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
