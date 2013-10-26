package ru.regenix.jphp;

import ru.regenix.jphp.compiler.CompileScope;
import ru.regenix.jphp.compiler.jvm.JvmCompiler;
import ru.regenix.jphp.compiler.jvm.runtime.memory.LongMemory;
import ru.regenix.jphp.compiler.jvm.runtime.memory.Memory;
import ru.regenix.jphp.env.Context;
import ru.regenix.jphp.env.Environment;
import ru.regenix.jphp.lexer.Tokenizer;
import ru.regenix.jphp.lexer.tokens.Token;
import ru.regenix.jphp.syntax.SyntaxAnalyzer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Environment environment = new Environment();
        Context context = new Context(environment, null);

        Tokenizer tokenizer = new Tokenizer(context, "class MyClass { static function test(){ $y = '20 '; return $x = $y . 'a'; } }");

        SyntaxAnalyzer analyzer = new SyntaxAnalyzer(tokenizer);
        JvmCompiler compiler = new JvmCompiler(new CompileScope(), context, analyzer.getTree());

        MyClassLoader classLoader = new MyClassLoader();
        compiler.compile();

        Class<?> clazz = classLoader.loadClass("MyClass", compiler.getClasses().get(0).getCw().toByteArray());
        Method method = clazz.getMethod("test", Environment.class, Memory[].class);

        Memory memory = null;
        long t = System.currentTimeMillis();
        for(int i = 0; i < 1000000; i++){
            memory = (Memory) method.invoke(null, environment, new Memory[]{});
        }
        System.out.println(memory.toString());
        System.out.println(System.currentTimeMillis() - t);
    }
}
