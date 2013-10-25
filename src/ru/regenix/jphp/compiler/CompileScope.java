package ru.regenix.jphp.compiler;


import ru.regenix.jphp.compiler.jvm.runtime.invokedynamic.CallableValue;
import ru.regenix.jphp.compiler.jvm.runtime.Memory;
import ru.regenix.jphp.compiler.jvm.runtime.PHPObject;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.tokens.stmt.ClassStmtToken;
import ru.regenix.jphp.lexer.tokens.stmt.MethodStmtToken;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CompileScope {

    protected Map<String, ClassStmtToken> classes;
    protected Map<String, MethodStmtToken> methods;

    private static Map<Class<? extends PHPObject>, Map<String, CallableValue>> classMethods =
            new HashMap<Class<? extends PHPObject>, Map<String, CallableValue>>();


    public CompileScope() {
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

    public static void registerClass(Class<? extends PHPObject> clazz){
        Map<String, CallableValue> map = new HashMap<String, CallableValue>();
        for (Method method : clazz.getMethods()){
            try {
                Method m = clazz.getMethod(method.getName(), PHPObject.class, Environment.class, Memory[].class);
                map.put(method.getName(), new CallableValue(m));
            } catch (NoSuchMethodException e) {
                // ..
            }
        }
        classMethods.put(clazz, map);
    }

    public static CallableValue findMethod(Class<? extends PHPObject> clazz, String methodName){
        return classMethods.get(clazz).get(methodName);
    }
}
